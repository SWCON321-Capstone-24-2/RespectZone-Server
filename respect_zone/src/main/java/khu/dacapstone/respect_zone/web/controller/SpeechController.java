package khu.dacapstone.respect_zone.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import khu.dacapstone.respect_zone.apiPayload.ApiResponse;
import khu.dacapstone.respect_zone.domain.Sentence;
import khu.dacapstone.respect_zone.domain.Speech;
import khu.dacapstone.respect_zone.domain.enums.SentenceType;
import khu.dacapstone.respect_zone.repository.SpeechRepository;
import khu.dacapstone.respect_zone.service.SentenceCommandService;
import khu.dacapstone.respect_zone.service.SentenceQueryService;
import khu.dacapstone.respect_zone.service.SpeechCommandService;
import khu.dacapstone.respect_zone.service.SpeechQueryService;
import khu.dacapstone.respect_zone.web.dto.ModelApiResponseDto;
import khu.dacapstone.respect_zone.web.dto.SentenceAnalysisNoKafkaResponseDto;
import khu.dacapstone.respect_zone.web.dto.SentenceAnalysisResponseDto;
import khu.dacapstone.respect_zone.web.dto.SentenceRequestDto;
import khu.dacapstone.respect_zone.web.dto.SentenceResponseDto;
import khu.dacapstone.respect_zone.web.dto.SpeechRequestDto;
import khu.dacapstone.respect_zone.web.dto.SpeechResponseDto;

@RestController
@RequestMapping("/api/speech")
public class SpeechController {

    private final SpeechRepository repository;
    private final SpeechQueryService speechQueryService;
    private final SentenceQueryService sentenceQueryService;
    private final SpeechCommandService speechCommandService;
    private final SentenceCommandService sentenceCommandService;

    @Value("${model.api.url}") // 환경 변수 주입
    private String modelApiUrl;

    // RestTemplate 빈 주입
    @Autowired
    private RestTemplate restTemplate;

    public SpeechController(SpeechRepository repository, SpeechQueryService speechQueryService,
            SentenceQueryService sentenceQueryService, SpeechCommandService speechCommandService,
            SentenceCommandService sentenceCommandService) {
        this.repository = repository;
        this.speechQueryService = speechQueryService;
        this.sentenceQueryService = sentenceQueryService;
        this.speechCommandService = speechCommandService;
        this.sentenceCommandService = sentenceCommandService;
    }

    // 모든 Speech를 가져오는 메서드 (테스트용)
    @GetMapping("/all")
    public ApiResponse<List<Speech>> getAllSpeech() {
        try {
            List<Speech> speeches = repository.findAll();
            return ApiResponse.onSuccess(speeches);
        } catch (Exception e) {
            return ApiResponse.onFailure("500", "모든 Speech 데이터를 가져오는데 실패했습니다.", null);
        }
    }

    // 해당 기기의 모든 Speech 데이터를 가져오는 메서드
    @GetMapping
    public ApiResponse<List<Speech>> getAllSpeechByDeviceId(@RequestHeader("deviceId") String deviceId) {
        try {
            List<Speech> speeches = speechQueryService.getAllSpeechByDeviceId(deviceId);
            return ApiResponse.onSuccess(speeches);
        } catch (Exception e) {
            return ApiResponse.onFailure("500", "해당 기기의 Speech 데이터를 가져오는데 실패했습니다.", null);
        }
    }

    // id와 deviceId를 기반으로 Speech 데이터를 삭제하는 메서드
    @DeleteMapping("/{id}")
    public ApiResponse<SpeechResponseDto.deleteResultDto> deleteSpeechById(
            @PathVariable(name = "id") Long id,
            @RequestHeader("deviceId") String deviceId) {
        speechQueryService.deleteSpeech(id, deviceId);
        return ApiResponse.onSuccess(
                SpeechResponseDto.deleteResultDto.builder()
                        .id(id)
                        .build());
    }

    // deviceId에 해당하는 Speech 목록을 불러온 상황에서, 특정 Speech id의 Sentence 목록을 가져오는 메서드
    @GetMapping("/{id}/sentences")
    public ApiResponse<SentenceResponseDto.getSentencesBySpeechIdDto> getSentencesBySpeechId(
            @PathVariable(name = "id") Long id,
            @RequestHeader("deviceId") String deviceId) {
        speechQueryService.checkSpeech(id, deviceId);
        List<Sentence> sentences = sentenceQueryService.getSentencesBySpeechId(id, deviceId);
        return ApiResponse.onSuccess(new SentenceResponseDto.getSentencesBySpeechIdDto(sentences));
    }

    // 녹음 시작. Speech 객체 생성 후 DB에 저장
    @PostMapping
    public ApiResponse<SpeechResponseDto.createResultDto> createSpeech(
            @RequestHeader("deviceId") String deviceId,
            @RequestBody SpeechRequestDto.CreateSpeechDto requestDto) {
        try {
            Speech savedSpeech = speechCommandService.createSpeech(deviceId, requestDto.getTimestamp());
            return ApiResponse.onSuccess(SpeechResponseDto.createResultDto.builder()
                    .id(savedSpeech.getId())
                    .build());
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure("400", e.getMessage(), null);
        } catch (Exception e) {
            return ApiResponse.onFailure("500", "Speech 생성에 실패했습니다.", null);
        }
    }

    // 녹음 중에 Sentence를 받아서 저장하는 메서드
    @PostMapping("/{speechId}/sentence")
    public ApiResponse<SentenceAnalysisResponseDto> saveSentence(
            @PathVariable(name = "speechId") Long speechId,
            @RequestHeader("deviceId") String deviceId,
            @RequestBody SentenceRequestDto.saveSentenceDto requestDto) {
        try {
            speechQueryService.checkSpeech(speechId, deviceId);
            Speech speech = speechQueryService.getSpeech(speechId, deviceId);

            // 모델 API 요청 데이터 구성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("text", requestDto.getSentence());
            requestBody.put("speechId", speechId.toString());
            requestBody.put("timestamp", requestDto.getTimestamp().toString());

            // 모델 API 호출
            ResponseEntity<ModelApiResponseDto> modelResponse = restTemplate.postForEntity(
                    modelApiUrl + "/predict_text",
                    requestBody,
                    ModelApiResponseDto.class);

            if (!modelResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("모델 API 호출 실패");
            }

            // 분석 결과 변환
            ModelApiResponseDto rawResult = modelResponse.getBody();
            SentenceAnalysisNoKafkaResponseDto convertedResult = new SentenceAnalysisNoKafkaResponseDto(
                    rawResult.getPred_label(),
                    rawResult.getPred_score(),
                    rawResult.getScores());

            SentenceAnalysisResponseDto analysisResult = convertedResult.toSentenceAnalysisResponseDto(speechId,
                    requestDto.getSentence());

            // GOOD_SENTENCE가 아닌 경우에만 저장
            if (analysisResult.getSentenceType() != SentenceType.GOOD_SENTENCE) {
                Sentence sentence = sentenceCommandService.saveSentence(
                        speechId,
                        requestDto.getSentence(),
                        analysisResult.getSentenceType(),
                        requestDto.getTimestamp());
            }

            return ApiResponse.onSuccess(analysisResult);

        } catch (Exception e) {
            return ApiResponse.onFailure("500", "Sentence 분석 및 저장에 실패했습니다: " + e.getMessage(), null);
        }
    }

    // 녹음이 끝났을 때 Speech 객체를 최종적으로 저장하는 메서드
    @PostMapping("/{id}")
    public ApiResponse<SpeechResponseDto.saveResultDto> saveSpeech(
            @PathVariable(name = "id") Long id,
            @RequestHeader("deviceId") String deviceId,
            @RequestBody SpeechRequestDto.SaveSpeechDto requestDto) {
        try {
            Speech savedSpeech = speechCommandService.saveSpeech(id, deviceId, requestDto.getTimestamp());
            int sentenceCount = savedSpeech.getSentences().size();

            return ApiResponse.onSuccess(SpeechResponseDto.saveResultDto.builder()
                    .id(savedSpeech.getId())
                    .recordingTime(savedSpeech.getRecordingTime())
                    .burningCount(savedSpeech.getBurningCount())
                    .sentenceCount(sentenceCount)
                    .build());
        } catch (Exception e) {
            return ApiResponse.onFailure("500", "Speech 저장에 실패했습니다.", null);
        }
    }

}
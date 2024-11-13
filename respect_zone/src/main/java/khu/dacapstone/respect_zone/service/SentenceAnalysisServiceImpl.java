package khu.dacapstone.respect_zone.service;

import org.springframework.stereotype.Service;

import khu.dacapstone.respect_zone.domain.enums.SentenceType;

@Service
public class SentenceAnalysisServiceImpl implements SentenceAnalysisService {

    @Override
    public SentenceType analyzeSentence(String sentence) {
        // 여기에 실제 문장 분석 로직을 구현
        // 임시로 간단한 예시 구현
        if (sentence.contains("바보") || sentence.contains("멍청이")) {
            return SentenceType.SWEAR_EXPRESSION;
        } else if (sentence.contains("여자") || sentence.contains("남자")) {
            return SentenceType.GENDER_HATE;
        } else if (sentence.contains("늙은") || sentence.contains("어린")) {
            return SentenceType.AGE_HATE;
        } else if (sentence.contains("혐오")) {
            return SentenceType.OTHER_HATE;
        }
        return SentenceType.GOOD_SENTENCE;
    }
}
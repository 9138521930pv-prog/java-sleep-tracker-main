package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MinSleep implements SleepAnalyzer {

    public AnalysisResult apply(List<SleepSession> s) {
        long min = s.stream()
                .mapToLong(SleepSession::minutes)
                .min()
                .orElse(0);
        return new AnalysisResult("Минимальная длительность сна (мин)", String.valueOf(min));
    }
}

package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxSleep implements SleepAnalyzer {

    public AnalysisResult apply(List<SleepSession> s) {
        long max = s.stream()
                .mapToLong(SleepSession::minutes)
                .max()
                .orElse(0);
        return new AnalysisResult("Максимальная длительность сна (мин)", String.valueOf(max));
    }
}

package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class CsSleep implements SleepAnalyzer {

    public AnalysisResult apply(List<SleepSession> s) {
        double avg = s.stream()
                .filter(ss -> ss.state() != SleepState.BAD)
                .mapToLong(SleepSession::minutes)
                .average()
                .orElse(0.0);
        return new AnalysisResult("Типичная длительность сна (мин)", String.format("%.1f", avg));
    }
}

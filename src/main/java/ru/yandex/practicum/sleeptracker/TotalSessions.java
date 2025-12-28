package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class TotalSessions implements SleepAnalyzer {

    public AnalysisResult apply(List<SleepSession> s) {
        return new AnalysisResult("Всего сессий сна", String.valueOf(s.size()));
    }
}
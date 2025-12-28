package ru.yandex.practicum.sleeptracker;

import java.util.List;

public interface SleepAnalyzer {
    AnalysisResult apply(List<SleepSession> sessions);
}
package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadSleepState implements SleepAnalyzer {

    public AnalysisResult apply(List<SleepSession> s) {
        long bad = s.stream()
                .filter(ss -> ss.state() == SleepState.BAD)
                .count();
        return new AnalysisResult("Количество сессий с плохим качеством сна", String.valueOf(bad));
    }
}


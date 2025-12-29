package ru.yandex.practicum.sleeptracker;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class SleepNights implements SleepAnalyzer {

    public AnalysisResult apply(List<SleepSession> s) {
        if (s.isEmpty()) return new AnalysisResult("Бессонных ночей", "0");

        var nights = s.stream()
                .filter(SleepSession::coversNightWindow)
                .map(ss -> {
                    var date = ss.start().toLocalDate();
                    return ss.start().toLocalDate().isBefore(ss.end().toLocalDate())
                            ? date : date.minusDays(1);
                })
                .distinct()
                .sorted()
                .toList();

        if (nights.isEmpty()) return new AnalysisResult("Бессонных ночей", "0");

        long totalDays = ChronoUnit.DAYS.between(nights.get(0), nights.get(nights.size() - 1)) + 1;
        long noSleep = totalDays - nights.size();

        return new AnalysisResult("Бессонных ночей", String.valueOf(noSleep));
    }
}
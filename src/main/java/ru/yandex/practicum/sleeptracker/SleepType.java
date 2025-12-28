package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;

public class SleepType implements SleepAnalyzer {

    public AnalysisResult apply(List<SleepSession> sessions) {
        var nightList = sessions.stream()
                .filter(SleepSession::isNightSleep)
                .toList();

        if (nightList.isEmpty()) {
            return new AnalysisResult("Тип сна", "Голубь");
        }

        long sov = 0;
        long javor = 0;
        long golub = 0;

        for (var session : nightList) {
            LocalTime start = session.start().toLocalTime();
            LocalTime end = session.end().toLocalTime();

            if (start.isAfter(LocalTime.of(23, 0)) && end.isAfter(LocalTime.of(9, 0))) {
                sov++;
            }
            else if (start.isBefore(LocalTime.of(22, 0)) && end.isBefore(LocalTime.of(7, 0))) {
                javor++;
            }
            else {
                golub++;
            }
        }

        if (sov > javor && sov > golub) {
            return new AnalysisResult("Тип сна", "Сова");
        } else if (javor > sov && javor > golub) {
            return new AnalysisResult("Тип сна", "Жаворонок");
        } else {
            return new AnalysisResult("Тип сна", "Голубь");
        }
    }
}


package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleepType implements SleepAnalyzer {

    @Override
    public AnalysisResult apply(List<SleepSession> sessions) {
        var nightList = sessions.stream()
                .filter(SleepSession::isNightSleep)
                .toList();

        if (nightList.isEmpty()) {
            return new AnalysisResult("Тип сна", "Голубь");
        }

        Map<String, Long> typeCounts = nightList.stream()
                .map(this::classifySession)
                .filter(type -> type != null)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        long maxCount = typeCounts.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        List<String> maxTypes = typeCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .toList();

        String dominantType = (maxTypes.size() == 1)
                ? maxTypes.get(0)
                : "Голубь";

        return new AnalysisResult("Тип сна", dominantType);
    }

    private String classifySession(SleepSession session) {
        if (session == null || !session.isNightSleep()) {
            return null;
        }

        LocalTime start = session.start().toLocalTime();
        LocalTime end = session.end().toLocalTime();

        if (start == null || end == null) {
            return null;
        }

        if (start.isAfter(LocalTime.of(23, 0)) && end.isAfter(LocalTime.of(9, 0))) {
            return "Сова";
        } else if (start.isBefore(LocalTime.of(22, 0)) && end.isBefore(LocalTime.of(7, 0))) {
            return "Жаворонок";
        } else {
            return "Голубь";
        }
    }
}
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

        // Группируем сеансы по типу и считаем количество в каждой группе
        Map<String, Long> typeCounts = nightList.stream()
                .map(this::classifySession)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        // Определяем тип с максимальным количеством
        String dominantType = Map.of(
                        "Сова", typeCounts.getOrDefault("Сова", 0L),
                        "Жаворонок", typeCounts.getOrDefault("Жаворонок", 0L),
                        "Голубь", typeCounts.getOrDefault("Голубь", 0L)
                ).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Голубь"); // запасной вариант (маловероятен)

        return new AnalysisResult("Тип сна", dominantType);
    }

    // Вспомогательный метод: определяет тип для одного сеанса
    private String classifySession(SleepSession session) {
        LocalTime start = session.start().toLocalTime();
        LocalTime end = session.end().toLocalTime();

        if (start.isAfter(LocalTime.of(23, 0)) && end.isAfter(LocalTime.of(9, 0))) {
            return "Сова";
        } else if (start.isBefore(LocalTime.of(22, 0)) && end.isBefore(LocalTime.of(7, 0))) {
            return "Жаворонок";
        } else {
            return "Голубь";
        }
    }
}

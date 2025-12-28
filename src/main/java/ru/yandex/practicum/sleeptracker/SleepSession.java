package ru.yandex.practicum.sleeptracker;

import java.time.*;

public record SleepSession(LocalDateTime start, LocalDateTime end, SleepState state) {

    public long minutes() {
        return Duration.between(start, end).toMinutes();
    }

    public boolean coversNightWindow() {
        LocalDate night = start.toLocalDate();
        LocalDateTime windowStart = night.atStartOfDay().minusHours(6);
        LocalDateTime windowEnd = night.plusDays(1).atStartOfDay().plusHours(12);
        return !end.isBefore(windowStart) && !start.isAfter(windowEnd);
    }

    public boolean isNightSleep() {
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();

        if (start.toLocalDate().isBefore(end.toLocalDate())) {
            return true;
        }

        return startTime.isBefore(LocalTime.of(6, 0)) ||
                endTime.isAfter(LocalTime.of(0, 0)) && endTime.isBefore(LocalTime.of(6, 0));
    }
}
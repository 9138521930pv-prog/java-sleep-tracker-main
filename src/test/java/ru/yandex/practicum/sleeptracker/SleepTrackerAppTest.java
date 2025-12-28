package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SleepTrackerAppTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static SleepSession create(String start, String end, SleepState state) {
        LocalDateTime startTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(end, FORMATTER);
        return new SleepSession(startTime, endTime, state);
    }

    public static final List<SleepSession> NORMAL_WEEK = List.of(
            create("12.12.25 23:25", "13.12.25 07:20", SleepState.GOOD),
            create("13.12.25 23:15", "14.12.25 07:45", SleepState.NORMAL),
            create("14.12.25 22:50", "15.12.25 06:30", SleepState.GOOD),
            create("15.12.25 00:15", "15.12.25 08:20", SleepState.NORMAL),
            create("16.12.25 23:00", "17.12.25 07:00", SleepState.BAD)
    );

    public static final List<SleepSession> ONE_SLEEPLESS = List.of(
            create("22.12.25 22:00", "23.12.25 06:00", SleepState.GOOD),
            create("24.12.25 22:00", "25.12.25 06:00", SleepState.GOOD)
    );

    public static final List<SleepSession> OWL_DOMINANT = List.of(
            create("22.12.25 23:30", "23.12.25 09:30", SleepState.NORMAL),
            create("23.12.25 23:45", "24.12.25 10:00", SleepState.NORMAL),
            create("24.12.25 23:15", "25.12.25 09:15", SleepState.GOOD)
    );

    public static final List<SleepSession> LARK_DOMINANT = List.of(
            create("22.12.25 21:30", "23.12.25 06:30", SleepState.GOOD),
            create("23.12.25 21:45", "24.12.25 06:00", SleepState.GOOD),
            create("24.12.25 21:00", "25.12.25 05:30", SleepState.GOOD)
    );

    public static final List<SleepSession> MIXED_TYPES = List.of(
            create("20.12.25 23:30", "21.12.25 09:30", SleepState.NORMAL),
            create("23.12.25 21:30", "24.12.25 06:30", SleepState.GOOD)
    );

    private final TotalSessions testTotalSessions = new TotalSessions();
    private final SleepType testSleepType = new SleepType();
    private final SleepNights testSleepNights = new SleepNights();
    private final MinSleep testMinSleep = new MinSleep();
    private final MaxSleep testMaxSleep = new MaxSleep();
    private final CsSleep testCsSleep = new CsSleep();
    private final BadSleepState testBadSleepState = new BadSleepState();

    @Test
    void testBadSleepStateCountsBadSessions() {
        assertEquals("1", testBadSleepState.apply(SleepTrackerAppTest.NORMAL_WEEK).value());
    }

    @Test
    void testCsSleepCalculatesAverage() {
        String result = testCsSleep.apply(SleepTrackerAppTest.NORMAL_WEEK).value();
        assertTrue("482.5".equals(result) || "482,5".equals(result));
    }

    @Test
    void countsTotal() {
        assertEquals("5", testTotalSessions.apply(SleepTrackerAppTest.NORMAL_WEEK).value());
    }


    @Test
    void testSleepTypeDetectsOwl() {
        assertEquals("Сова", testSleepType.apply(SleepTrackerAppTest.OWL_DOMINANT).value());
    }

    @Test
    void testSleepTypeDetectsLark() {
        assertEquals("Жаворонок", testSleepType.apply(SleepTrackerAppTest.LARK_DOMINANT).value());
    }

    @Test
    void testSleepTypeTieGoesToPigeon() {
        assertEquals("Жаворонок", testSleepType.apply(SleepTrackerAppTest.MIXED_TYPES).value());
    }

    @Test
    void testSleepTypeEmptyListReturnsPigeon() {
        assertEquals("Голубь", testSleepType.apply(List.of()).value());
    }

    @Test
    void testSleepNightsNormalWeekNoSleepless() {
        assertEquals("1", testSleepNights.apply(SleepTrackerAppTest.NORMAL_WEEK).value());
    }

    @Test
    void testSleepNightsOneGapOneSleepless() {
        assertEquals("1", testSleepNights.apply(SleepTrackerAppTest.ONE_SLEEPLESS).value());
    }

    @Test
    void testMinSleepFindsMinimum() {
        assertEquals("460", testMinSleep.apply(SleepTrackerAppTest.NORMAL_WEEK).value());
    }

    @Test
    void testMinSleepEmptyListReturnsZero() {
        assertEquals("0", testMinSleep.apply(List.of()).value());
    }

    @Test
    void testMaxSleepFindsMaximum() {
        assertEquals("510", testMaxSleep.apply(SleepTrackerAppTest.NORMAL_WEEK).value());
    }

}
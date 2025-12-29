package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class SleepTrackerApp {

    public static void main(String[] args) throws Exception {
        String path;
        if (args.length == 0) {
            path = createDefaultLogFile();
            System.out.println("Файл не указан — создан временный: " + path);
        } else {
            path = args[0];
        }

        var sessions = load(path);

        ANALYZERS.forEach(analyzer -> {
            AnalysisResult r = analyzer.apply(sessions);
            System.out.println(r.description() + ": " + r.value());
        });
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static final List<SleepAnalyzer> ANALYZERS = List.of(
            new TotalSessions(),
            new MinSleep(),
            new MaxSleep(),
            new CsSleep(),
            new BadSleepState(),
            new SleepNights(),
            new SleepType()
    );

    private static final String DEFAULT_LOG = """
            08.12.25 23:30;09.12.25 07:30;GOOD
            09.12.25 23:15;10.12.25 07:45;NORMAL
            10.12.25 22:50;11.12.25 06:30;GOOD
            11.12.25 00:15;12.12.25 08:20;NORMAL
            12.12.25 23:00;13.12.25 07:00;BAD
            13.12.25 01:30;14.12.25 10:15;GOOD
            15.12.25 02:00;15.12.25 11:00;NORMAL
            """;

    private static String createDefaultLogFile() throws IOException {
        Path temp = Files.createTempFile("sleep_log_", ".txt");
        Files.writeString(temp, DEFAULT_LOG.strip());
        temp.toFile().deleteOnExit();
        return temp.toString();
    }

    private static List<SleepSession> load(String path) throws Exception {
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            return lines
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(line -> {
                        try {
                            String[] p = line.split(";");
                            LocalDateTime start = LocalDateTime.parse(p[0], FORMATTER);
                            LocalDateTime end = LocalDateTime.parse(p[1], FORMATTER);
                            SleepState state = SleepState.valueOf(p[2]);
                            return new SleepSession(start, end, state);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Ошибка парсинга строки: " + line, e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл '" + path + "': " + e.getMessage());
            throw e;
        }
    }
}
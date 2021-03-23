package nl.djj.swgoh_bot_v2.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private final String className = this.getClass().getSimpleName();
    private static final String ERROR_PREFIX = "ERROR";
    private static final String DEBUG_PREFIX = "DEBUG";
    private static final String INFO_PREFIX = "INFO";

    private final boolean betaMode;

    public Logger(final boolean betaMode) {
        this.betaMode = betaMode;
        if (betaMode){
            debug(className, "Beta mode active");
        }
    }

    public void error(final String className, final String message) {
        System.err.println(ANSI_RED + ERROR_PREFIX + ": " + className + ": " + message + ANSI_RESET);
        appendToFile(ERROR_PREFIX, className + ": " + message);
    }

    public void info(final String className, final String message) {
        System.out.println(ANSI_BLUE + INFO_PREFIX + ": " + className + ": " + message + ANSI_RESET);
        appendToFile(INFO_PREFIX, className + ": " + message);
    }

    public void debug(final String className, final String message) {
        if (betaMode) {
            System.out.println(ANSI_GREEN + DEBUG_PREFIX + ": " + className + ": " + message + ANSI_RESET);
        }
    }

    private void appendToFile(final String fileType, final String content) {
        try {
            File file = new File("log/" + fileType + "_" + getDate(false) + ".log");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(getDate(true) + ": " + content);
            br.newLine();
            br.close();
            fr.close();
        } catch (final IOException exception) {
            error(className, exception.getMessage());
        }
    }

    private String getDate(final boolean includeTime) {
        String timeFormat = "dd-MM-yyyy";
        if (includeTime) {
            timeFormat += " HH:mm:ss";
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(timeFormat);
        return dtf.format(LocalDateTime.now());
    }
}

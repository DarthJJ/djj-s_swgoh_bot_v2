package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.entities.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * @author DJJ
 */
public class Logger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private final transient String className = this.getClass().getSimpleName();
    private static final String ERROR_PREFIX = "ERROR";
    private static final String DEBUG_PREFIX = "DEBUG";
    private static final String INFO_PREFIX = "INFO";
    private static final String PERMISSION_PREFIX = "PERMISSION";
    private static final String COMMAND_PREFIX = "COMMAND";
    private static final String SEPARATOR = ": ";
    private final transient boolean debugMode;

    /**
     * Constructor.
     *
     * @param debugMode if true, logs debug statements.
     */
    public Logger(final boolean debugMode) {
        this.debugMode = debugMode;
        if (debugMode) {
            debug(className, "Beta mode active");
        }
    }

    /**
     * Call to log an error.
     *
     * @param className the className.
     * @param methodName the method name.
     * @param message   the message.
     */
    public void error(final String className, final String methodName, final String message) {
        if (debugMode) {
            System.err.println(ANSI_RED + ERROR_PREFIX + SEPARATOR + className + SEPARATOR + methodName + SEPARATOR + message + ANSI_RESET);
        }
        appendToFile(ERROR_PREFIX, className + SEPARATOR + message);
    }

    /**
     * Call to log an info.
     *
     * @param className the classname.
     * @param message   the message.
     */
    public void info(final String className, final String message) {
        if (debugMode) {
            System.out.println(ANSI_BLUE + INFO_PREFIX + SEPARATOR + className + SEPARATOR + message + ANSI_RESET);
        }
        appendToFile(INFO_PREFIX, className + SEPARATOR + message);
    }

    /**
     * Call to log a debug .
     *
     * @param className the classname.
     * @param message   the message.
     */
    public void debug(final String className, final String message) {
        if (debugMode) {
            System.out.println(ANSI_GREEN + DEBUG_PREFIX + SEPARATOR + className + SEPARATOR + message + ANSI_RESET);
        }
        appendToFile(DEBUG_PREFIX, message);
    }

    /**
     * Call to log a permission violation.
     *
     * @param message the message.
     */
    public void permission(final Message message) {
        if (debugMode) {
            System.out.println(ANSI_YELLOW + PERMISSION_PREFIX + SEPARATOR + message.getAuthor() + SEPARATOR + message.getCommand() + SEPARATOR + message.getFlag() + SEPARATOR + message.getArgs() + ANSI_RESET);
        }
        appendToFile(PERMISSION_PREFIX, message.getAuthor() + SEPARATOR + message.getCommand() + SEPARATOR + message.getFlag() + SEPARATOR + message.getArgs());
    }

    /**
     * Call to log a command used.
     *
     * @param message the message.
     */
    public void command(final Message message) {
        if (debugMode) {
            System.out.println(ANSI_GREEN + COMMAND_PREFIX + SEPARATOR + message.getAuthor() + SEPARATOR + message.getCommand() + SEPARATOR + message.getFlag() + SEPARATOR + message.getArgs() + ANSI_RESET);
        }
        appendToFile(COMMAND_PREFIX, message.getAuthor() + SEPARATOR + message.getCommand() + SEPARATOR + message.getFlag() + SEPARATOR + message.getArgs());
    }

    private void appendToFile(final String fileType, final String content) {
        final File file = new File("log/" + fileType + "_" + StringHelper.getCurrentDateAsString() + ".log");
        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("Couldn't create log file");
            }
        } catch (final IOException exception) {
            error(className, "appendToFile", exception.getMessage());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(StringHelper.getCurrentDateTimeAsString() + SEPARATOR + content);
            writer.newLine();
            writer.flush();
        } catch (final IOException exception) {
            error(className, "appendToFile", exception.getMessage());
        }
    }
}

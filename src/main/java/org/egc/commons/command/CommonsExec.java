package org.egc.commons.command;

import org.apache.commons.exec.*;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 使用 apache.commons.exec 执行命令行程序
 * </pre>
 * TODO 测试
 * 参考 http://wuhongyu.iteye.com/blog/461477
 *
 * @author houzhiwei
 * @date 2017 /9/16 10:46
 */
public class CommonsExec {

    private static final Logger log = LoggerFactory.getLogger(CommonsExec.class);
    private static final String UTF8 = "UTF-8";
    private static final String GBK = "GBK";
    private static final String ISO_8859_1 = "ISO-8859-1";
    /**
     * The constant OUT.
     */
    public static final String OUT = "out";
    /**
     * The constant ERROR.
     */
    public static final String ERROR = "error";
    /**
     * The constant EXIT_VALUE.
     */
    public static final String EXIT_VALUE = "exitValue";

    /**
     * <pre>
     * Execute command line use {@link Executor#execute(CommandLine)} API.
     * Default exit Value is 0 on success
     * <pre/>
     *
     * @param cmd use {@link CommandLine#addArgument(String)} or {@link CommandLine#parse(String)} to parse command string to CommandLine <p/>
     * @return 0 : success; 1: failed
     * @throws IOException the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static int exec(CommandLine cmd) throws IOException, InterruptedException {
        return exec(cmd, 0);
    }

    /**
     * Exec int.
     *
     * @param commandLine the command line
     * @return 0 : success; 1: failed
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static int exec(String commandLine) throws IOException, InterruptedException {
        CommandLine cmd = CommandLine.parse(commandLine);
        return exec(cmd, 0);
    }

    /**
     * <pre>
     * Execute command line use {@link Executor#execute(CommandLine)} API.
     * use {@link CommandLine#addArgument(String)} to add command arguments
     * </pre>
     *
     * @param cmd       the CommandLine
     * @param exitValue the exit value to be considered as successful execution <b>if it is not 0<b/>
     * @return 0 : success; 1: failed
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static int exec(CommandLine cmd, int exitValue) throws IOException, InterruptedException {
        Executor executor = new DefaultExecutor();
        // 设置超时时间，毫秒
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
        executor.setWatchdog(watchdog);
        executor.setExitValue(exitValue);
        int exit = executor.execute(cmd);
        return exit;
    }

    /**
     * Execute command line and return output string
     *
     * @param cmd the cmd
     * @return command line output with key <b> out</b> and<b>  error</b>
     * @throws IOException the io exception
     */
    public static Map<String, Object> execWithOutput(CommandLine cmd) throws IOException {
        return execWithOutput(cmd, null);
    }

    /**
     * Exec with output map.
     *
     * @param commandLine the command line
     * @return command line output with key {@link CommonsExec#OUT} and {@link CommonsExec#ERROR} and {@link CommonsExec#EXIT_VALUE}
     * @throws IOException the io exception
     */
    public static Map<String, Object> execWithOutput(String commandLine) throws IOException {
        CommandLine cmd = CommandLine.parse(commandLine);
        return execWithOutput(cmd, null);
    }

    /**
     * Exec with output string.
     *
     * @param cmd         the command line string
     * @param envKeyValues the environmental variable list in format: <b>key=value</b>
     * @return the output string map with key {@link CommonsExec#OUT} and {@link CommonsExec#ERROR} and {@link CommonsExec#EXIT_VALUE}
     * @throws IOException the io exception
     */
    public static Map<String, Object> execWithOutput(String cmd, List<String> envKeyValues) throws IOException {
        CommandLine commandLine = CommandLine.parse(cmd);
        return execWithOutput(commandLine, envKeyValues);
    }

    /**
     * Exec with output string.
     *
     * @param cmd          the {@link CommandLine}
     * @param envKeyValues the environmental variable list in format: <b>key=value</b>
     * @return the output string map with key {@link CommonsExec#OUT} and {@link CommonsExec#ERROR} and {@link CommonsExec#EXIT_VALUE}
     * @throws IOException the io exception
     */
    public static Map<String, Object> execWithOutput(CommandLine cmd, List<String> envKeyValues) throws IOException {

        Executor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
        executor.setWatchdog(watchdog);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        executor.setStreamHandler(streamHandler);

        int exitValue = 0;
        if (envKeyValues == null || envKeyValues.size() == 0) {
            exitValue = executor.execute(cmd);
        } else {
            Map env = EnvironmentUtils.getProcEnvironment();
            for (String kv : envKeyValues) {
                EnvironmentUtils.addVariableToEnvironment(env, kv);
            }
            exitValue = executor.execute(cmd, env);
        }
        Map out = new HashMap();
        out.put(OUT, outputStream.toString(UTF8));
        out.put(ERROR, errorStream.toString(UTF8));
        out.put(EXIT_VALUE, exitValue);
        return out;
    }
}
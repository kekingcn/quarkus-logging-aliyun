package cn.keking.common.log;

import com.aliyun.openservices.aliyun.log.producer.*;
import com.aliyun.openservices.aliyun.log.producer.errors.LogSizeTooLargeException;
import com.aliyun.openservices.aliyun.log.producer.errors.ProducerException;
import com.aliyun.openservices.aliyun.log.producer.errors.TimeoutException;
import com.aliyun.openservices.log.common.LogItem;
import org.jboss.logging.Logger;
import org.jboss.logmanager.ExtLogRecord;
import org.slf4j.MDC;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * @author kl : http://kailing.pub
 * @version 1.0
 * @date 2020/9/17
 */
public class AliyunLogHandler extends Handler {

    private static final Logger LOG = Logger.getLogger(AliyunLogHandler.class);

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String AUTH_USERNAME = "username";

    private AliyunLogConfig config;
    private Producer producer;

    public AliyunLogHandler(AliyunLogConfig config) {
        this.config = config;
        this.start();
    }

    @Override
    public void publish(LogRecord record) {
        ExtLogRecord logRecord = (ExtLogRecord) record;
        List<LogItem> logItems = new ArrayList<>();
        LogItem item = new LogItem();
        logItems.add(item);
        item.SetTime((int) (record.getMillis() / 1000));
        item.PushBack("time", DTF.format(LocalDateTime.now()));
        item.PushBack("level", record.getLevel().getName());

        item.PushBack("thread", logRecord.getThreadName());
        item.PushBack(AUTH_USERNAME, MDC.get(AUTH_USERNAME));
        String source = record.getSourceClassName() +"#"+ record.getSourceMethodName();
        item.PushBack("location", source);
        String message = record.getMessage();
        item.PushBack("message", message);

        String throwable = this.getThrowableStr(record.getThrown());
        if (throwable != null) {
            item.PushBack("throwable", throwable);
        }
        try {
            producer.send(
                    config.name.get(),
                    config.logstore.get(),
                    config.topic.get(),
                    null,
                    logItems,
                    new AliYunAppenderCallback(config, logItems));
        } catch (InterruptedException e) {
            LOG.warn("The current thread has been interrupted during send logs.");
        } catch (Throwable e) {
            if (e instanceof LogSizeTooLargeException) {
                LOG.error("The size of log is larger than the maximum allowable size, e={}", e);
            } else if (e instanceof TimeoutException) {
                LOG.error("The time taken for allocating memory for the logs has surpassed., e={}", e);
            } else {
                LOG.error("Failed to send log, logItems={}, e=", logItems, e);
            }
        }
    }

    @Override
    public void flush() {
        //
    }

    private void start() {
        ProjectConfigs projectConfigs = new ProjectConfigs();
        ProjectConfig projectConfig = new ProjectConfig(
                config.name.get(),
                config.endpoint.get(),
                config.accessKeyId.get(),
                config.accessKeySecret.get(),
                null,
                config.userAgent.get());
        projectConfigs.put(projectConfig);
        ProducerConfig producerConfig = new ProducerConfig(projectConfigs);
        this.producer = new LogProducer(producerConfig);
    }

    @Override
    public void close() throws SecurityException {
        try {
            this.producer.close();
        } catch (InterruptedException | ProducerException e) {
            e.printStackTrace();
        }
    }

    private String getThrowableStr(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString(); // stack trace as a string
    }
}

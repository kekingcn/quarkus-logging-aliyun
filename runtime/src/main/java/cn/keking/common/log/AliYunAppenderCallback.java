package cn.keking.common.log;

import com.aliyun.openservices.aliyun.log.producer.Callback;
import com.aliyun.openservices.aliyun.log.producer.Result;
import com.aliyun.openservices.log.common.LogItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Common Aliyun appender producer callback
 *
 * @Author wanglaomo
 * @Date 2019/4/3
 **/
public class AliYunAppenderCallback implements Callback {

    private final Logger logger = LoggerFactory.getLogger(AliYunAppenderCallback.class);

    private final String project;

    private final String logStore;

    private final String topic;

    private final List<LogItem> logItems;

    public AliYunAppenderCallback(AliyunLogConfig config, List<LogItem> logItems) {
        super();
        this.project = config.name.get();
        this.logStore = config.logstore.get();
        this.topic = config.topic.get();
        this.logItems = logItems;
    }

    @Override
    public void onCompletion(Result result) {
        if (!result.isSuccessful()) {
            logger.error("Failed to putLogs. project=" + project + " logStore=" + logStore +
                    " topic=" + topic + " logItems=" + logItems, result);
        }

    }
}

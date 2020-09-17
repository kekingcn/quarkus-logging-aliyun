package cn.keking.common.log;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;
import java.util.logging.Level;

/**
 * @author kl : http://kailing.pub
 * @version 1.0
 * @date 2020/9/17
 */
@ConfigRoot(phase = ConfigPhase.RUN_TIME, name = "log.aliyun")
public class AliyunLogConfig {

    /**
     * 是否开启阿里云日志
     */
    @ConfigItem(name = ConfigItem.PARENT,defaultValue = "false")
    boolean enable = false;

    /**
     * 日志级别
     */
    @ConfigItem(defaultValue = "INFO")
    public Level level = Level.INFO;

    /**
     * 项目名称
     */
    @ConfigItem
    public Optional<String> name;

    /**
     * 日志存储
     */
    @ConfigItem
    public Optional<String> logstore;

    /**
     * 阿里云日志接口
     */
    @ConfigItem
    public Optional<String> endpoint;

    /**
     * 阿里云日志accessKeyId
     */
    @ConfigItem
    public Optional<String> accessKeyId;

    /**
     * 阿里云日志accessKeySecret
     */
    @ConfigItem
    public Optional<String> accessKeySecret;

    /**
     * 阿里云日志userAgent
     */
    @ConfigItem
    public Optional<String> userAgent;

    /**
     * 日志的topic
     */
    @ConfigItem
    public Optional<String> topic;
}

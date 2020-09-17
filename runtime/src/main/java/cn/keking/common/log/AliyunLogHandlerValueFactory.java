package cn.keking.common.log;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Handler;

/**
 * @author kl : http://kailing.pub
 * @version 1.0
 * @date 2020/9/17
 */
@Recorder
public class AliyunLogHandlerValueFactory {
    private static final Logger LOG = Logger.getLogger(AliyunLogHandlerValueFactory.class);

    public RuntimeValue<Optional<Handler>> create(final AliyunLogConfig config){

        if (!config.enable) {
            LOG.info("阿里云日志集成未开启");
            // Disable
            return new RuntimeValue<>(Optional.empty());
        }

        LOG.info("阿里云日志集成已开启");
        // Init
        AliyunLogHandler handler = new AliyunLogHandler(config);
        handler.setLevel(config.level);
        try {
            handler.setEncoding(StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new RuntimeValue<>(Optional.of(handler));
    }
}

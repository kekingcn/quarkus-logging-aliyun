package cn.keking.common.log;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LogHandlerBuildItem;

/**
 * @author kl : http://kailing.pub
 * @version 1.0
 * @date 2020/9/17
 */
public class AliyunLogProcessor {
    private static final String FEATURE = "aliyunlog";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogHandlerBuildItem addAliyunLogHandler(final AliyunLogConfig config,
                                            final AliyunLogHandlerValueFactory handlerValueFactory) {
        return new LogHandlerBuildItem(handlerValueFactory.create(config));
    }

    @BuildStep
    ExtensionSslNativeSupportBuildItem activateSslNativeSupport() {
        return new ExtensionSslNativeSupportBuildItem(FEATURE);
    }
}

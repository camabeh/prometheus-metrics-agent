package com.fleury.metrics.agent;

import com.fleury.metrics.agent.config.ArgParser;
import com.fleury.metrics.agent.config.Configuration;
import com.fleury.metrics.agent.reporter.PrometheusMetricSystemFactory;
import com.fleury.metrics.agent.transformer.AnnotatedMetricClassTransformer;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import static com.fleury.metrics.agent.config.LoggerUtil.initializeLogging;

/**
 *
 * @author Will Fleury
 */
public class Agent {

    public static void premain(String args, Instrumentation instrumentation) throws InterruptedException, IOException {
        new HTTPServer(9898);
        ArgParser argParser = new ArgParser(args);

        initializeLogging(argParser.getLogConfigFilename());

        Configuration config = Configuration.createConfig(argParser.getConfigFilename());
        PrometheusMetricSystemFactory.INSTANCE.init(config.getSystem());

        instrumentation.addTransformer(
                new AnnotatedMetricClassTransformer(config),
                instrumentation.isRetransformClassesSupported());
    }
}

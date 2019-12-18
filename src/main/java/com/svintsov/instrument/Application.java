package com.svintsov.instrument;

import static com.google.common.collect.Lists.newArrayList;

import com.svintsov.instrument.cmd.CommandLineUtils;
import com.svintsov.instrument.config.SpringApplication;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * Application.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
public class Application {

    public static final String APPLICATION_NAME = "Financial instruments analyzer";

    private static final String CONFIG_BASE_PACKAGE = "com.svintsov.instrument";

    public static void main(String[] args) {
        log.info("Starting... Arguments are: {}", newArrayList(args));

        CommandLineUtils.parseCommandLine(args)
                .ifPresent(commandLineOptions -> performAnalysis(commandLineOptions.getFileFullPath(), commandLineOptions.getBlockSize()));
    }

    private static void performAnalysis(@NotNull String fileFullPath, @NotNull Integer blockSize) {

        log.info("Performing analysis... File name = {}, block size = {}", fileFullPath, blockSize);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CONFIG_BASE_PACKAGE);
        SpringApplication application = context.getBean(SpringApplication.class);
        application.performAnalysis(fileFullPath, blockSize);
    }

}

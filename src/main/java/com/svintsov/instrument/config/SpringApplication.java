package com.svintsov.instrument.config;

import com.svintsov.instrument.service.FileAnalyzerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * SpringApplication.
 *
 * @author Ilya_Svintsov
 */
@Component
@RequiredArgsConstructor
public class SpringApplication {

    private final FileAnalyzerService fileAnalyzerService;

    public void performAnalysis(String fullFilePath, Integer blockSize) {
        try {
            fileAnalyzerService.performAnalysis(getFile(fullFilePath), blockSize);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file");
        }
    }

    private File getFile(String fullFilePath) {
        File file = new File(fullFilePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException(String.format("%s is not a valid file", fullFilePath));
        }
        return file;
    }

}

package com.svintsov.instrument.service;

import java.io.IOException;

/**
 * FileReaderService.
 *
 * @author Ilya_Svintsov
 */
public interface FileAnalyzerService {

    void performAnalysis(String fullFilePath, Integer blockSize) throws IOException;

}

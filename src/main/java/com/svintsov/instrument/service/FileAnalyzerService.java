package com.svintsov.instrument.service;

import com.svintsov.instrument.model.BlockData;

import java.io.File;
import java.io.IOException;

/**
 * FileReaderService.
 *
 * @author Ilya_Svintsov
 */
public interface FileAnalyzerService {

    BlockData performAnalysis(File file, Integer blockSize) throws IOException;

}

package com.svintsov.instrument;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * FileReader.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
public class FileReader {

    public void readFile(@NotNull String fileName, @NotNull Long blockSize) {
        log.info("Reading file... File name = {}, block size = {}", fileName, blockSize);
    }

}

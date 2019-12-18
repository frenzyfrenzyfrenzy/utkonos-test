package com.svintsov.instrument.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.io.FileUtils.lineIterator;

import com.svintsov.instrument.model.BlockData;
import com.svintsov.instrument.model.InstrumentLine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * FileReaderServiceImpl.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileAnalyzerServiceImpl implements FileAnalyzerService {

    private static final String FILE_ENCODING = "UTF-8";

    private final ParserService parserService;

    @Override
    public void performAnalysis(String fullFilePath, Integer blockSize) throws IOException {
        log.info("Reading lines...");

        File file = new File(fullFilePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException(String.format("%s is not a valid file", fullFilePath));
        }

        try (LineIterator iterator = lineIterator(file, FILE_ENCODING)) {
            BlockData previousBlockData = null;
            List<String> currentBlock = newArrayList();
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                currentBlock.add(line);
                if (blockSize.equals(currentBlock.size())) {
                    previousBlockData = analyzeBlock(currentBlock, previousBlockData);
                    currentBlock.clear();
                }
            }
        }
    }

    private BlockData analyzeBlock(List<String> currentBlock, BlockData previousBlockData) {

        List<InstrumentLine> validLines = currentBlock.stream()
                .map(parserService::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Valid lines: {}", validLines);

        Double instrumentOneAverage = validLines.stream()
                .filter(instrumentLine -> instrumentLine.getInstrumentNumber().equals(1))
                .map(InstrumentLine::getPrice)
                .reduce(0.d, (average, current) -> (average + current) / 2);

        log.info("Instrument one average: {}", instrumentOneAverage);

        return new BlockData()
                .setInstrumentOneAveragePrice(instrumentOneAverage);
    }

}

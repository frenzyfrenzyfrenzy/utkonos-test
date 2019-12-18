package com.svintsov.instrument.service;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.nonNull;
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
import java.util.Map;
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
    public BlockData performAnalysis(File file, Integer blockSize) throws IOException {
        log.info("Reading lines...");

        try (LineIterator iterator = lineIterator(file, FILE_ENCODING)) {
            BlockData previousBlockData = null;
            List<String> currentBlock = newArrayList();
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                currentBlock.add(line);
                if (blockSize.equals(currentBlock.size()) || !iterator.hasNext()) {
                    previousBlockData = analyzeBlock(currentBlock, previousBlockData);
                    currentBlock.clear();
                }
            }
            return previousBlockData;
        }
    }

    private BlockData analyzeBlock(List<String> currentBlock, BlockData previousBlockData) {

        List<InstrumentLine> validLines = currentBlock.stream()
                .map(parserService::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Valid lines: {}", validLines);

        Map<Integer, Long> thisInstrumentsCounters = validLines.stream()
                .collect(Collectors.groupingBy(InstrumentLine::getInstrumentNumber, Collectors.counting()));

        Long thisInstrumentOneCounter = thisInstrumentsCounters.getOrDefault(1, 0L);

        Double thisInstrumentOneSum = validLines.stream()
                .filter(instrumentLine -> instrumentLine.getInstrumentNumber().equals(1))
                .map(InstrumentLine::getPrice)
                .reduce(Double::sum)
                .orElse(0.d);

        Double instrumentOneAverage;
        if (nonNull(previousBlockData)) {

            Long previousInstrumentOneCounter = previousBlockData.getInstrumentCounters().getOrDefault(1, 0L);
            Double previousInstrumentOneAverage = previousBlockData.getInstrumentOneAveragePrice();
            Double previousInstrumentOneSum = previousInstrumentOneAverage * previousInstrumentOneCounter.doubleValue();

            instrumentOneAverage = thisInstrumentOneCounter == 0 ? previousInstrumentOneAverage :
                    (thisInstrumentOneSum + previousInstrumentOneSum) / (thisInstrumentOneCounter.doubleValue() + previousInstrumentOneCounter);

            previousBlockData.getInstrumentCounters().put(1, thisInstrumentOneCounter + previousInstrumentOneCounter);
            previousBlockData.setInstrumentOneAveragePrice(instrumentOneAverage);

            return previousBlockData;
        } else {
            BlockData blockData = new BlockData();
            blockData.getInstrumentCounters().put(1, thisInstrumentOneCounter);

            instrumentOneAverage = thisInstrumentOneCounter == 0 ? 0 : thisInstrumentOneSum / thisInstrumentOneCounter.doubleValue();
            blockData.setInstrumentOneAveragePrice(instrumentOneAverage);

            return blockData;
        }
    }

}

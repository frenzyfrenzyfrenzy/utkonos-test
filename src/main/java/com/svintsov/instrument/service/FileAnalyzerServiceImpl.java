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
import java.util.stream.Collector;
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

        Map<Integer, Double> thisInstrumentsSums = validLines.stream()
                .collect(Collectors.groupingBy(InstrumentLine::getInstrumentNumber, Collectors.mapping(InstrumentLine::getPrice, Collectors.summingDouble(Double::doubleValue))));

        if (nonNull(previousBlockData)) {
            return updateBlockData(previousBlockData, thisInstrumentsCounters, thisInstrumentsSums);
        } else {
            return initializeBlockData(thisInstrumentsCounters, thisInstrumentsSums);
        }
    }

    private BlockData updateBlockData(BlockData previousBlockData,
                                      Map<Integer, Long> thisInstrumentsCounters,
                                      Map<Integer, Double> thisInstrumentsSums) {

        thisInstrumentsCounters.forEach((instrument, counter) -> {
            Double previousInstrumentAverage = previousBlockData.getInstrumentAverages().getOrDefault(instrument, 0.d);
            Long previousInstrumentCounter = previousBlockData.getInstrumentCounters().getOrDefault(instrument, 0L);
            Double thisInstrumentSum = thisInstrumentsSums.getOrDefault(instrument, 0.d);

            Double newInstrumentAverage =
                    (thisInstrumentSum + (previousInstrumentAverage * previousInstrumentCounter)) / (counter.doubleValue() + previousInstrumentCounter);

            previousBlockData.getInstrumentAverages().put(instrument, newInstrumentAverage);
            previousBlockData.getInstrumentCounters().merge(instrument, counter, Long::sum);
        });

        return previousBlockData;
    }

    private BlockData initializeBlockData(Map<Integer, Long> thisInstrumentsCounters, Map<Integer, Double> thisInstrumentsSums) {

        BlockData blockData = new BlockData();

        thisInstrumentsCounters.forEach((instrument, counter) -> {
            Double thisInstrumentSum = thisInstrumentsSums.getOrDefault(instrument, 0.d);
            Double newInstrumentAverage = (thisInstrumentSum) / (counter.doubleValue());
            blockData.getInstrumentAverages().put(instrument, newInstrumentAverage);
        });

        blockData.setInstrumentCounters(thisInstrumentsCounters);
        return blockData;
    }

}

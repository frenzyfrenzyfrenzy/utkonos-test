package com.svintsov.instrument.service;

import com.svintsov.instrument.BaseIntegrationTest;
import com.svintsov.instrument.model.BlockData;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

/**
 * FileAnalyzerServiceTest.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
public class FileAnalyzerServiceTest extends BaseIntegrationTest {

    @Autowired
    private FileAnalyzerService fileAnalyzerService;

    @Test
    public void testPerformAnalysisFirstAverage() throws IOException {
        File firstTestFile = getFileFromResources("first_test_file.txt");
        BlockData blockData = fileAnalyzerService.performAnalysis(firstTestFile, 10);

        log.info("Counters: {}", blockData.getInstrumentCounters());
        log.info("First average: {}", blockData.getInstrumentOneAveragePrice());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(blockData.getInstrumentCounters().get(1)).isEqualTo(35);
            softAssertions.assertThat(blockData.getInstrumentAverages().get(1)).isEqualTo(2.54382d, Offset.offset(.0001d));
        });
    }

    @Test
    public void testPerformAnalysisFirstAverageSparse() throws IOException {
        File firstTestFile = getFileFromResources("second_test_file.txt");
        BlockData blockData = fileAnalyzerService.performAnalysis(firstTestFile, 10);

        log.info("Counters: {}", blockData.getInstrumentCounters());
        log.info("First average: {}", blockData.getInstrumentOneAveragePrice());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(blockData.getInstrumentCounters().get(1)).isEqualTo(25);
            softAssertions.assertThat(blockData.getInstrumentAverages().get(1)).isEqualTo(2.567116, Offset.offset(.0001d));
        });
    }

    @Test
    public void testPerformAnalysisFirstAverageRandomSparse() throws IOException {
        File firstTestFile = getFileFromResources("third_test_file.txt");
        BlockData blockData = fileAnalyzerService.performAnalysis(firstTestFile, 10);

        log.info("Counters: {}", blockData.getInstrumentCounters());
        log.info("First average: {}", blockData.getInstrumentOneAveragePrice());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(blockData.getInstrumentCounters().get(1)).isEqualTo(12);
            softAssertions.assertThat(blockData.getInstrumentAverages().get(1)).isEqualTo(2.54247, Offset.offset(.0001d));
        });
    }
}
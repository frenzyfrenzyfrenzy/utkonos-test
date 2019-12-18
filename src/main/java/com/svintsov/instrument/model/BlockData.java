package com.svintsov.instrument.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * BlockData.
 *
 * @author Ilya_Svintsov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BlockData {

    private Map<Integer, Long> instrumentCounters = new HashMap<>();
    private Map<Integer, Double> instrumentAverages = new HashMap<>();

    private Double instrumentOneAveragePrice = 0.d;
    private Double instrumentTwoNovemberAveragePrice = 0.d;
    private Map<Integer, Double> lastTenPricesSumByInstrument = new HashMap<>();

}

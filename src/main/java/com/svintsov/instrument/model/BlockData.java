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

    private Map<Integer, Long> instrumentAllTimeCounters = new HashMap<>();
    private Map<Integer, Double> instrumentAllTimeAverages = new HashMap<>();

}

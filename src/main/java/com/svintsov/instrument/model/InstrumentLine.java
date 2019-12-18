package com.svintsov.instrument.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Line.
 *
 * @author Ilya_Svintsov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class InstrumentLine {

    private Integer instrumentNumber;
    private LocalDate date;
    private Double price;

}

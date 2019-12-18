package com.svintsov.instrument.service;

import com.svintsov.instrument.model.InstrumentLine;

import java.util.Optional;

/**
 * ValidatorService.
 *
 * @author Ilya_Svintsov
 */
public interface ParserService {

    Optional<InstrumentLine> parseLine(String line);

}

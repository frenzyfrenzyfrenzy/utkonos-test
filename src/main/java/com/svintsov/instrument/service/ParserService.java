package com.svintsov.instrument.service;

import com.svintsov.instrument.model.InstrumentLine;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * ValidatorService.
 *
 * @author Ilya_Svintsov
 */
public interface ParserService {

    Optional<InstrumentLine> parseLine(String line, Predicate<LocalDate> datePredicate);

}

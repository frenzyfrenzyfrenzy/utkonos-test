package com.svintsov.instrument.service;

import static java.util.regex.Pattern.matches;

import com.svintsov.instrument.model.InstrumentLine;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ValidatorServiceImpl.
 *
 * @author Ilya_Svintsov
 */
@Service
public class ParserServiceImpl implements ParserService {

    private static final String INSTRUMENT_LINE_PATTERN = "INSTRUMENT(\\d{1,3}),(\\d{2}-[A-Z][a-z]{2}-\\d{4}),(\\d*\\.\\d*)";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US);

    @Override
    public Optional<InstrumentLine> parseLine(String line, Predicate<LocalDate> datePredicate) {

        Pattern pattern = Pattern.compile(INSTRUMENT_LINE_PATTERN);
        Matcher matcher = pattern.matcher(line);

        if (!matcher.matches())
            return Optional.empty();

        try {
            Integer instrumentNumber = Integer.valueOf(matcher.group(1));
            LocalDate date = LocalDate.parse(matcher.group(2), DATE_TIME_FORMATTER);
            Double price = Double.valueOf(matcher.group(3));
            return Optional.of(
                    new InstrumentLine()
                            .setInstrumentNumber(instrumentNumber)
                            .setDate(date)
                            .setPrice(price));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

}

package com.svintsov.instrument.cmd;

import static com.svintsov.instrument.Application.APPLICATION_NAME;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Optional;

/**
 * CommandLineParser.
 *
 * @author Ilya_Svintsov
 */
public class CommandLineUtils {

    private static final String FILE_LONG_OPTION = "file";
    private static final String FILE_SHORT_OPTION = "f";
    private static final String BLOCK_SIZE_LONG_OPTION = "block";
    private static final String BLOCK_SIZE_SHORT_OPTION = "b";

    private static final Integer BLOCK_SIZE_DEFAULT_VALUE = 100;
    private static final Integer BLOCK_SIZE_MIN_VALUE = 10;

    public static Optional<CommandLineOptions> parseCommandLine(String[] args) {

        Options options = new Options();
        options.addOption(FILE_SHORT_OPTION, FILE_LONG_OPTION, true, "Input file full path")
                .addOption(BLOCK_SIZE_SHORT_OPTION, BLOCK_SIZE_LONG_OPTION, true, "Block size for read");

        String fileFullPath;
        Integer blockSize;
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            fileFullPath = Optional.ofNullable(cmd.getOptionValue(FILE_LONG_OPTION))
                    .orElseThrow(() -> new ParseException("File must be specified"));

            blockSize = Optional.ofNullable(cmd.getOptionValue(BLOCK_SIZE_LONG_OPTION))
                    .map(Integer::valueOf)
                    .filter(integer -> integer >= BLOCK_SIZE_MIN_VALUE)
                    .orElse(BLOCK_SIZE_DEFAULT_VALUE);

        } catch (ParseException | NumberFormatException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(APPLICATION_NAME, options);
            return Optional.empty();
        }

        return Optional.of(
                new CommandLineOptions()
                        .setBlockSize(blockSize)
                        .setFileFullPath(fileFullPath));
    }

}

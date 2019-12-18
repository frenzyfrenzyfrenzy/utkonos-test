package com.svintsov.instrument;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Application.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
@SuppressWarnings("ALL")
public class Application {

    public static void main(String[] args) {
        log.info("Starting... Arguments are: {}", args);

        Options options = new Options();
        options.addOption("f", "file", true, "Input file full path")
                .addOption("b", "block", true, "Block size for read");

        String fileFullPath;
        Long blockSize;
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            if (!cmd.hasOption("file")) {
                throw new ParseException("File must be specified");
            } else {
                fileFullPath = cmd.getOptionValue("file");
                blockSize = Long.valueOf(cmd.getOptionValue("block", "1000"));
            }
        } catch (ParseException| NumberFormatException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Financial instruments parser", options);
            return;
        }

        FileReader fileReader = new FileReader();
        fileReader.readFile(fileFullPath, blockSize);
    }

}

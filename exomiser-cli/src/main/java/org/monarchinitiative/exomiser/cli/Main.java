/*
 * The Exomiser - A tool to annotate and prioritize genomic variants
 *
 * Copyright (c) 2016-2020 Queen Mary University of London.
 * Copyright (c) 2012-2016 Charité Universitätsmedizin Berlin and Genome Research Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.monarchinitiative.exomiser.cli;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;


/**
 * Main class for calling off the command line in the Exomiser package.
 *
 * @author Jules Jacobsen <j.jacobsen@qmul.ac.uk>
 */
@SpringBootApplication
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Parse the input to check for help etc. in order to fail fast before launching the context.
        CommandLine commandLine = CommandLineOptionsParser.parse(args);
        if (commandLine.hasOption("help") || commandLine.getOptions().length == 0) {
            CommandLineOptionsParser.printHelpAndExit();
        }

        if (!hasInputFileOption(commandLine)) {
            logger.error("Missing an input file option!");
            CommandLineOptionsParser.printHelpAndExit();
        }
        //check file paths exist before launching.
        checkFilesExist(commandLine);

        // all ok so far - try launching the app
        Locale.setDefault(Locale.UK);
        SpringApplication.run(Main.class, args).close();

        logger.info("Exomising finished - Bye!");
    }

    private static boolean hasInputFileOption(CommandLine commandLine) {
        for (String s : CommandLineOptionsParser.fileDependentOptions()) {
            if (commandLine.hasOption(s)) {
                return true;
            }
        }
        return false;
    }

    private static void checkFilesExist(CommandLine commandLine) {
        for (String option : CommandLineOptionsParser.fileDependentOptions()) {
            if (commandLine.hasOption(option)) {
                Path optionPath = Paths.get(commandLine.getOptionValue(option));
                if (Files.notExists(optionPath)) {
                    logger.error("FATAL ERROR - {} file '{}' not found", option, optionPath);
                    System.exit(0);
                }
            }
        }
    }

}

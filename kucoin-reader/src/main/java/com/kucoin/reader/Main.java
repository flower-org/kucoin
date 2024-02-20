package com.kucoin.reader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
    public static void main(String[] args) {
        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // create the Options
        Options options = new Options();
        options.addOption("a", "all", false, "All tickers");

        Option tickerOption = new Option("t", "Ticker name");
        tickerOption.setLongOpt("ticker");
        tickerOption.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(tickerOption);

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("all")) {
                runAllTopics();
            } else if (line.hasOption("ticker")) {
                run(String.join(",", line.getOptionValues("ticker")));
            } else {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("KucoinReader", options);
            }
        } catch (ParseException pe) {
            System.out.println("Argument parsing error: " + pe.getMessage());

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("KucoinReader", options);
        }
    }

    static void runAllTopics() {
        KucoinReader pugep = KucoinReader.createAllTopics();
        pugep.start();
    }

    static void run(String topic) {
        KucoinReader pugep = KucoinReader.create(topic);
        pugep.start();
    }
}

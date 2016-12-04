package rudi;

import rudi.error.CannotProcessLineException;
import rudi.error.CannotReadSourceException;
import rudi.process.DefaultLineProcessor;
import rudi.process.pre.SourcePreProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiSource;
import rudi.support.RudiSourceRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Main class for RUDI interpreter
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide first argument as the absolute path to the RUDI source file.");
            System.exit(-1);
        } else if (!args[0].endsWith(".rud")) {
            System.err.println("Please provide path to a '.rud' file.");
            System.exit(-1);
        }

        try {
            RudiSource source = new RudiSource(Files.lines(Paths.get(args[0])).collect(Collectors.toList()));
            SourcePreProcessor.process(source);
            RudiSource main = RudiSourceRegistry.getInstance().get(RudiConstant.MAIN_PROGRAM_KEY);
            for (int i = 1; i <= main.totalLines(); i++) {
                DefaultLineProcessor.getInstance().doProcess(i, main.getLine(i));
            }
        } catch (IOException e0) {
            System.err.println("Failed to read RUDI source file provided by first argument.");
            System.exit(-1);
        } catch (CannotProcessLineException | CannotReadSourceException e1) {
            System.err.println(e1.getMessage());
            System.exit(-1);
        }
    }
}

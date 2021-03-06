package org.hildan.tools.csvmerger;

import java.io.IOException;
import java.util.Arrays;

import org.hildan.utils.csv.CsvMerger;

/**
 * This program merges two or more CSV files into one. The different sources must have the same
 * column headers.
 *
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey BION</a>
 */
public class CsvFileMerger {

    private static final String DEST_SWITCH = "-o";

    private static final String DEST_DEFAULT = "merged.csv";

    /**
     * Choose between GUI or console version according to the number of arguments.
     *
     * @param args
     *            No arguments will start the GUI, otherwise filenames have to be specified:
     *            destination first, then all the sources.
     */
    public static void main(String[] args) {
        if (args.length >= 2) {
            String[] sources;
            String dest;
            if (args[0].equals(DEST_SWITCH)) {
                dest = args[1];
                sources = Arrays.copyOfRange(args, 2, args.length);
            } else {
                dest = DEST_DEFAULT;
                sources = args;
            }
            mergeFiles(sources, dest);
        } else {
            System.out.println("Usage: CsvFileMerger [-o <dest>] <source1> [sources]*");
        }
    }

    /**
     * Merges the given source files into the given destination file.
     *
     * @param sources
     *            An array of filenames referring to the sources.
     * @param destination
     *            The destination filename.
     */
    private static void mergeFiles(String[] sources, String destination) {
        if (sources.length == 0) {
            System.err.println("No source file specified.");
            return;
        } else if (sources.length == 1) {
            System.out.println("warning: only one source has been provided, copying content to destination.");
        }
        try {
            System.out.println("Sources to merge:");
            for (int i = 0; i < sources.length; i++) {
                System.out.println("> " + sources[i] + "");
            }
            String destFilename;
            if (destination == null || "".equals(destination)) {
                System.out.println("No output file selected.");
                destFilename = generateDestFilename(sources[0]);
                System.out.println("Auto output filename: " + destFilename);
            } else {
                destFilename = destination;
            }
            System.out.println("Merging into " + destFilename + "...");
            CsvMerger merger = new CsvMerger(destFilename);
            merger.merge(sources);
            System.out.println("Success.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    /**
     * Returns a destination filename based on the name of the specified source, removing its
     * number.
     *
     * @param source
     *            The source numbered filename
     * @return The generated destination filename.
     */
    private static String generateDestFilename(String source) {
        String base = getBaseNameWithoutNumber(source);
        char lastChar = base.charAt(base.length() - 1);
        if (lastChar == '-' || lastChar == '_') {
            return base + "merged.csv";
        } else {
            return base + "-merged.csv";
        }
    }

    /**
     * Removes the 3-digit number and the extension of a CSV filename.
     *
     * @param fullName
     *            The initial filename with the number and the extension.
     * @return The truncated filename without number and extension.
     */
    private static String getBaseNameWithoutNumber(String fullName) {
        int extPosition = fullName.lastIndexOf(".");
        if (extPosition == -1) {
            extPosition = fullName.length();
        }
        return fullName.substring(0, extPosition - 3);
    }
}

package com.joffrey_bion.csv.csv_file_merger;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import com.joffrey_bion.csv.CsvMerger;
import com.joffrey_bion.file_processor_window.JFileProcessorWindow;
import com.joffrey_bion.file_processor_window.file_picker.JFilePickersPanel;

/**
 * This program merges two or more CSV files into one. The different sources must
 * have the same column headers.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey BION</a>
 */
public class CsvFileMerger {

    private static final int ARG_SOURCES_START = 1;
    private static final int ARG_DEST = 0;

    private static MergerArgsPanel argsPanel;

    /**
     * Choose between GUI or console version according to the number of arguments.
     * 
     * @param args
     *            No arguments will start the GUI, otherwise filenames have to be
     *            specified: destination first, then all the sources.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    openWindow();
                }
            });
        } else if (args.length >= 2) {
            mergeFiles(Arrays.copyOfRange(args, ARG_SOURCES_START, args.length), args[ARG_DEST]);
        } else {
            System.out.println("Usage: CsvFileMerger <dest> <source1> [sources]*");
        }
    }

    /**
     * Starts the GUI.
     */
    private static void openWindow() {
        JFileProcessorWindow.setSystemLookAndFeel();
        // file pickers source and destination
        JFilePickersPanel filePickers = new JFilePickersPanel("1st input file", "Output file");
        argsPanel = new MergerArgsPanel();
        @SuppressWarnings("serial")
        JFileProcessorWindow frame = new JFileProcessorWindow("CSV Merger", "Merge", filePickers,
                argsPanel) {
            @Override
            public void process(String[] inPaths, String[] outPaths) {
                clearLog();
                processNumberedFiles(inPaths[0], outPaths[0]);
            }
        };
        frame.setVisible(true);
    }

    /**
     * Calls {@link #mergeFiles(String[], String, Logger)} with a list of numbered
     * filenames as the sources.
     * 
     * @param source
     *            The filename of the first source containing the first number.
     * @param dest
     *            The destination filename.
     */
    private static void processNumberedFiles(String source, String dest) {
        if (source == null || "".equals(source)) {
            System.err.println("No input file pattern selected.");
            return;
        }
        try {
            String[] fileNumbers = argsPanel.getFileNumbers();
            String[] sources = new String[fileNumbers.length];
            String base = getBaseNameWithoutNumber(source);
            for (int i = 0; i < sources.length; i++) {
                sources[i] = base + fileNumbers[i] + ".csv";
            }
            mergeFiles(sources, dest);
        } catch (Exception e) {
            System.err.println(e.toString());
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
     * Returns a destination filename based on the name of the specified source,
     * removing its number.
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

package com.joffrey_bion.csv.csv_file_merger;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.joffrey_bion.csv.CsvMerger;
import com.joffrey_bion.file_processor_window.ConsoleLogger;
import com.joffrey_bion.file_processor_window.JFilePickersPanel;
import com.joffrey_bion.file_processor_window.JFileProcessorWindow;
import com.joffrey_bion.file_processor_window.Logger;

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
        } else if (args.length >= 3) {
            mergeFiles(Arrays.copyOfRange(args, ARG_SOURCES_START, args.length), args[ARG_DEST],
                    new ConsoleLogger());
        } else {
            System.out.println("Usage: CsvFileMerger <dest> <source1> <source2> [source3]*");
        }
    }

    /**
     * Starts the GUI.
     */
    private static void openWindow() {
        // windows system look and feel for the window
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // file pickers source and destination
        JFilePickersPanel filePickers = new JFilePickersPanel("1st input file", "Output file");
        argsPanel = new MergerArgsPanel();
        @SuppressWarnings("serial")
        JFileProcessorWindow frame = new JFileProcessorWindow("CSV Merger", "Merge", filePickers,
                argsPanel) {
            @Override
            public void process(String[] inPaths, String[] outPaths) {
                processNumberedFiles(inPaths[0], outPaths[0], this);
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
     * @param log
     *            A {@link Logger} to display log messages.
     */
    private static void processNumberedFiles(String source, String dest, Logger log) {
        log.clearLog();
        if (source == null || "".equals(source)) {
            log.printErr("No input file pattern selected.");
            return;
        }
        try {
            String[] fileNumbers = argsPanel.getFileNumbers();
            String[] sources = new String[fileNumbers.length];
            String base = getBaseNameWithoutNumber(source);
            for (int i = 0; i < sources.length; i++) {
                sources[i] = base + fileNumbers[i] + ".csv";
            }
            mergeFiles(sources, dest, log);
        } catch (Exception e) {
            log.printErr(e.toString());
        }
    }

    /**
     * Merges the given source files into the given destination file.
     * 
     * @param sources
     *            An array of filenames referring to the sources.
     * @param destination
     *            The destination filename.
     * @param log
     *            A {@link Logger} to display log messages.
     */
    private static void mergeFiles(String[] sources, String destination, Logger log) {
        if (sources.length < 2) {
            log.printErr("Cannot merge less than 2 files!");
            return;
        }
        try {
            log.println("Sources to merge:");
            for (int i = 0; i < sources.length; i++) {
                log.println("> " + sources[i] + "");
            }
            String destFilename;
            if (destination == null || "".equals(destination)) {
                log.println("No output file selected.");
                destFilename = generateDestFilename(sources[0]);
                log.println("Auto output filename: " + destFilename);
            } else {
                destFilename = destination;
            }
            log.println("Processing...");

            CsvMerger merger = new CsvMerger(destFilename);
            merger.merge(sources);
            log.println("Success.");
        } catch (IOException e) {
            e.printStackTrace();
            log.printErr(e.getMessage());
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

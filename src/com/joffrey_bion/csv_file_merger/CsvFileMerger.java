package com.joffrey_bion.csv_file_merger;

import java.io.IOException;

import javax.swing.UIManager;

import com.joffrey_bion.file_processor_window.JFilePickersPanel;
import com.joffrey_bion.file_processor_window.JFileProcessorWindow;
import com.joffrey_bion.file_processor_window.Logger;
import com.joffrey_bion.file_processor_window.MergerArgsPanel;

public class CsvFileMerger {

    private static MergerArgsPanel argsPanel;

    public static void main(String[] args) {
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
        JFileProcessorWindow frame = new JFileProcessorWindow("Pseq File Processor", "Process",
                filePickers, argsPanel) {
            @Override
            public void process(String[] inPaths, String[] outPaths) {
                processFile(inPaths[0], outPaths[0], this);
            }
        };
        frame.setVisible(true);
        return;
    }

    private static void processFile(String source, String dest, Logger log) {
        log.clearLog();
        if (source == null || "".equals(source)) {
            log.printErr("No input file pattern selected.");
            return;
        }
        try {
            String[] fileNumbers = argsPanel.getFileNumbers();
            String[] sources = new String[fileNumbers.length];
            String base = getBaseNameWithoutNumber(source);
            log.println("Sources to merge:");
            for (int i = 0; i < sources.length; i++) {
                sources[i] = base + fileNumbers[i] + ".csv";
                log.println("> " + sources[i] + "");
            }
            String destFilename;
            if (dest == null || "".equals(dest)) {
                log.println("No output file selected.");
                destFilename = generateDestFilename(source);
                log.println("Auto output filename: " + destFilename);
            } else {
                destFilename = dest;
            }
            log.println("Processing...");

            CsvMerger merger = new CsvMerger(destFilename);
            merger.merge(sources);

            log.println("Success.");
        } catch (IOException e) {
            log.printErr(e.getMessage());
        }catch (Exception e) {
            log.printErr(e.toString());
        }
    }

    private static String generateDestFilename(String source) {
        return getBaseNameWithoutNumber(source) + "-merged.csv";
    }

    private static String getBaseNameWithoutNumber(String fullName) {
        int extPosition = fullName.lastIndexOf(".");
        if (extPosition == -1) {
            extPosition = fullName.length();
        }
        return fullName.substring(0, extPosition - 3);
    }
}

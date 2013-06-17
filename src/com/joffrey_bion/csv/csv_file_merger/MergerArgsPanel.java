package com.joffrey_bion.csv.csv_file_merger;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class MergerArgsPanel extends JPanel {
    private JTextField tfFrom;
    private JTextField tfTo;

    /**
     * Create the panel.
     */
    public MergerArgsPanel() {
        JLabel lblFrom = new JLabel("From #");
        add(lblFrom);
        tfFrom = new JTextField();
        tfFrom.setText("000");
        add(tfFrom);
        tfFrom.setColumns(3);
        JLabel lblTo = new JLabel("to #");
        add(lblTo);
        tfTo = new JTextField();
        tfTo.setText("003");
        add(tfTo);
        tfTo.setColumns(3);
    }

    public String[] getFileNumbers() {
        String[] numbers = new String[getNumberOfFiles()];
        int n = 0;
        for (int i = Integer.parseInt(tfFrom.getText()); i <= Integer.parseInt(tfTo.getText()); i++) {
            numbers[n++] = String.format("%03d", i);
        }
        return numbers;
    }

    private int getNumberOfFiles() {
        return Integer.parseInt(tfTo.getText()) - Integer.parseInt(tfFrom.getText()) + 1;
    }
}

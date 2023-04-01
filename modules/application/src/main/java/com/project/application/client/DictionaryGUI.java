package com.project.application.client;

import javax.swing.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.project.application.util.Codecs;
import com.project.repository.dictionary.DictionaryEntry;
import com.project.repository.dictionary.DictionaryOperation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class DictionaryGUI extends JFrame {
    private JComboBox action;
    private JTextField word;
    private JButton submitButton;
    private JTextArea meanings;
    private JPanel dictionaryPanel;
    private JLabel dictionaryGUILabel;
    private JLabel meaningLabel;
    private JTextField responseStatus;
    private JTextField wordResponse;
    private JTextArea meaningResponse;
    private JLabel wordResponseLabel;
    private JLabel meaningResponseLabel;
    private static final String ip = "localhost";
    private static final int port = 4887;

    public static void main(String[] args) {
        JFrame frame = new JFrame("DictionaryGUI");
        frame.setTitle("Dictionary GUI");
        frame.setContentPane(new DictionaryGUI().dictionaryPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    public DictionaryGUI() {
        wordResponseLabel.setVisible(false);
        wordResponse.setVisible(false);
        wordResponseLabel.setVisible(false);
        meaningResponse.setVisible(false);
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wordResponseLabel.setVisible(false);
                wordResponse.setVisible(false);
                meaningResponseLabel.setVisible(false);
                meaningResponse.setVisible(false);
                responseStatus.setText("");
                String selectedAction = Objects.requireNonNull(action.getSelectedItem()).toString();
                if (selectedAction.equalsIgnoreCase("Query") || selectedAction.equalsIgnoreCase("Remove")) {
                    meanings.setVisible(false);
                    meaningLabel.setVisible(false);
                } else {
                    meanings.setVisible(true);
                    meaningLabel.setVisible(true);
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wordResponse.setVisible(false);
                meaningResponse.setVisible(false);
                responseStatus.setVisible(true);
                String selectedAction = Objects.requireNonNull(action.getSelectedItem()).toString();
                String[] noMeanings = {};
                DictionaryOperation request;
                String response;

                switch (selectedAction.toLowerCase()) {
                    case "add":
                        if (meanings.getText().length() > 0) {
                            String[] meaningsList = meanings.getText().split(System.lineSeparator());
                            request = new DictionaryOperation(word.getText(), meaningsList, DictionaryOperation.Operation.ADD);
                            response = sendRequest(request);
                            if (Objects.equals(response, Boolean.TRUE.toString())) {
                                responseStatus.setText("Successfully added word " + word.getText() + " to dictionary");
                            } else if (Objects.equals(response, Boolean.FALSE.toString())) {
                                responseStatus.setText("Word " + word.getText() + " already exists in dictionary");
                            } else {
                                responseStatus.setText("Unable to connect to dictionary server");
                            }
                        } else {
                            responseStatus.setText("No meaning provided for word " + word.getText());
                        }
                        break;
                    case "update":
                        if (meanings.getText().length() > 0) {
                            String[] meaningsList = meanings.getText().split(System.lineSeparator());
                            request = new DictionaryOperation(word.getText(), meaningsList, DictionaryOperation.Operation.UPDATE);
                            response = sendRequest(request);
                            if (Objects.equals(response, Boolean.TRUE.toString())) {
                                responseStatus.setText("Successfully updated word " + word.getText() + " in dictionary");
                            } else if (Objects.equals(response, Boolean.FALSE.toString())) {
                                responseStatus.setText("Word " + word.getText() + " does not exist in dictionary");
                            } else {
                                responseStatus.setText("Unable to connect to dictionary server");
                            }
                        } else {
                            responseStatus.setText("No meaning provided for word " + word.getText());
                        }
                        break;
                    case "query":
                        request = new DictionaryOperation(word.getText(), noMeanings, DictionaryOperation.Operation.QUERY);
                        response = sendRequest(request);
                        if (!Objects.equals(response, Boolean.FALSE.toString())) {
                            try {
                                DictionaryEntry entry = Codecs.objectMapper.reader().readValue(response, DictionaryEntry.class);
                                responseStatus.setText("Successfully queried word " + word.getText());
                                wordResponseLabel.setVisible(true);
                                wordResponse.setVisible(true);
                                wordResponse.setText(entry.word);
                                meaningResponseLabel.setVisible(true);
                                meaningResponse.setVisible(true);
                                meaningResponse.setText(String.join("\n", entry.meanings));
                            } catch (IOException ex) {
                                responseStatus.setText("Unable to connect to dictionary server");
                            }
                        } else {
                            responseStatus.setText("Word " + word.getText() + " does not exist in dictionary");
                        }
                        break;
                    case "remove":
                        request = new DictionaryOperation(word.getText(), noMeanings, DictionaryOperation.Operation.REMOVE);
                        response = sendRequest(request);
                        if (Objects.equals(response, Boolean.TRUE.toString())) {
                            responseStatus.setText("Successfully removed word " + word.getText() + " from dictionary");
                        } else if (Objects.equals(response, Boolean.FALSE.toString())) {
                            responseStatus.setText("Word " + word.getText() + " does not exist in dictionary");
                        } else {
                            responseStatus.setText("Unable to connect to dictionary server");
                        }
                        break;
                }
            }
        });
    }

    public static String sendRequest(DictionaryOperation request) {

        try (Socket socket = new Socket(ip, port);) {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            output.writeUTF(Codecs.objectMapper.writeValueAsString(request));
            System.out.println("Data sent to Server--> " + request);
            output.flush();

            // Wait for server response
            while (true) {
                if (input.available() > 0) {
                    return input.readUTF();
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Error, IP Address of the host is invalid");
        } catch (IOException e) {
            System.out.println("Error completing client request");
        }
        return null;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        dictionaryPanel = new JPanel();
        dictionaryPanel.setLayout(new GridLayoutManager(8, 3, new Insets(15, 15, 15, 15), -1, -1));
        dictionaryPanel.setEnabled(true);
        dictionaryGUILabel = new JLabel();
        dictionaryGUILabel.setText("Dictionary GUI");
        dictionaryPanel.add(dictionaryGUILabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        action = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Add");
        defaultComboBoxModel1.addElement("Query");
        defaultComboBoxModel1.addElement("Update");
        defaultComboBoxModel1.addElement("Remove");
        action.setModel(defaultComboBoxModel1);
        action.setToolTipText("");
        dictionaryPanel.add(action, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("Word");
        dictionaryPanel.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        meaningLabel = new JLabel();
        meaningLabel.setHorizontalAlignment(0);
        meaningLabel.setHorizontalTextPosition(0);
        meaningLabel.setText("Meanings");
        meaningLabel.setToolTipText("Add one meaning per line");
        dictionaryPanel.add(meaningLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        word = new JTextField();
        dictionaryPanel.add(word, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        submitButton = new JButton();
        submitButton.setText("Submit");
        dictionaryPanel.add(submitButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        meanings = new JTextArea();
        meanings.setEditable(true);
        dictionaryPanel.add(meanings, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(150, 50), new Dimension(150, 50), null, 0, false));
        final JSeparator separator1 = new JSeparator();
        dictionaryPanel.add(separator1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        dictionaryPanel.add(separator2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator3 = new JSeparator();
        dictionaryPanel.add(separator3, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Response");
        dictionaryPanel.add(label2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        responseStatus = new JTextField();
        responseStatus.setText("");
        dictionaryPanel.add(responseStatus, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        meaningResponse = new JTextArea();
        dictionaryPanel.add(meaningResponse, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        wordResponse = new JTextField();
        dictionaryPanel.add(wordResponse, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        wordResponseLabel = new JLabel();
        wordResponseLabel.setText("Word");
        dictionaryPanel.add(wordResponseLabel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        meaningResponseLabel = new JLabel();
        meaningResponseLabel.setText("Meaning");
        dictionaryPanel.add(meaningResponseLabel, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return dictionaryPanel;
    }

}

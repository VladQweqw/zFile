package com.codebind;

import com.codebind.exceptions.NotDirectory;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static com.codebind.options.Option.option_btns;

import com.codebind.options.*;

public class App {
    private final String version = "1.1.0";
    private final JFrame frame = new JFrame("FileEdit");
    private JList<String> list;
    private String current_path;
    private final HashSet<String> selected_files = new HashSet<>();
    private boolean select_all_flag = false;
    private final HashSet<String> selected_options = new HashSet<String>();
    private ArrayList<String> fileNames = new ArrayList<>();
    private final String files_title = "Files";

    private static Style style = new Style();

    // class instances
    Increment Option_Increment = new Increment();
    LowerCase Option_Lowercase = new LowerCase();
    UpperCase Option_Uppercase = new UpperCase();
    AddPhase Option_AddPhase = new AddPhase();
    RemovePhase Option_RemovePhase = new RemovePhase();

    // create components
    private JButton createFileExplorer() {
        JButton fileChooserBtn = new JButton("Select from PC");
        fileChooserBtn = style.buttonStyles(fileChooserBtn);

        fileChooserBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(frame);

            if(result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                readDirFiles(selectedFile);
                this.current_path = selectedFile.getAbsolutePath();
            }
        });

        return fileChooserBtn;
    }
    private JButton createManualPathButton(JTextField inp) {
        JButton manualPathBtn = new JButton("Scan");
        manualPathBtn.setPreferredSize(new Dimension(100, 30));
        manualPathBtn = style.buttonStyles(manualPathBtn);

        manualPathBtn.addActionListener(e -> {
            try {
                String path = inp.getText();

                if(!new File(path).isDirectory()) {
                    throw new NotDirectory();
                }

                this.current_path = path;
                readDirFiles(new File(path));
            }
            catch(NotDirectory exc) {
                exc.printStackTrace();

                JOptionPane.showMessageDialog(
                        frame,
                        "Path doesn't lead to any folder",
                        "File selected is not a folder",
                        JOptionPane.WARNING_MESSAGE
                );
            }
            catch(Exception err) {
                System.out.println("Nu ai pus text");
            }
        });

        return manualPathBtn;
    }
    private JScrollPane createOptionsMenu() {
        JPanel btn_wrappers = new JPanel();
        btn_wrappers = style.setBackground(btn_wrappers);

        for(Map.Entry<String, String> entry : option_btns.entrySet()) {
            final JButton[] btn = {new JButton(entry.getKey())};
            btn[0] = style.buttonStyles(btn[0]);

            btn[0].addActionListener(e -> {
                if(this.selected_options.contains(entry.getValue())) {
                    this.selected_options.remove(entry.getValue());
                    btn[0] = style.buttonStyles(btn[0]);

                }else {
                    btn[0] = style.buttonStylesActive(btn[0]);

                    this.selected_options.add(entry.getValue());
                    openInputPopup(entry.getValue());
                }
            });

            btn_wrappers.add(btn[0]);
        }

        JScrollPane scrollPane = new JScrollPane(
                btn_wrappers,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(480, 55));
        scrollPane = style.setScrollPane(scrollPane);

        return scrollPane;
    }
    private JSeparator getSeparator() {
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(480, 1));

        return separator;
    }
    private JScrollPane createFilesList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        updateFileList(listModel);

        list.setCellRenderer((list1, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value);
            label.setOpaque(true);
            label = style.listItemStyle(label);

            if(select_all_flag) {
                label = style.listItemStyleSelected(label);
                selected_files.add(value);
            }else {
                if(isSelected) {
                    label = style.listItemStyleSelected(label);
                    selected_files.add(value);
                } else {
                    selected_files.remove(value);
                }
            }

            return label;
        });
        list = style.listStyles(list);

        JScrollPane jScrollPane = new JScrollPane(list);
        jScrollPane.setPreferredSize(new Dimension(480, 225));

        jScrollPane = style.setScrollPane(jScrollPane);

        return jScrollPane;
    }

    // popups
    private void openInputPopup(String type) {
        JDialog popup = new JDialog();

        if(type.equals(Option_Increment.name_id)) {
            popup = IncrementPopup();
        }else if(type.equals(Option_AddPhase.name_id)) {
            popup = AddPhasePopup();
        }else if(type.equals(Option_RemovePhase.name_id)) {
            popup = RemovePhasePopup();
        }else if(type.equals(Option_Lowercase.name_id)) {
            previewOptions();
            return;

        }else if(type.equals(Option_Uppercase.name_id)) {
            previewOptions();
            return;
        }

        popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        popup.setResizable(false);
        popup.setVisible(true);

        popup.setLocationRelativeTo(frame);
    }
    private JDialog IncrementPopup() {
        JDialog popup = new JDialog(frame, "Enter details", true);

        popup.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        popup.setSize(350, 200);
        popup = style.popupStyle(popup);

        // desc
        JTextArea desc = new JTextArea();
        desc.setText("Add a text and a step increase for the text increment, for example text 1, " +
                "text 2," +
                " text 3");
        desc.setPreferredSize(new Dimension(330, 30));
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc = style.setBackground(desc);

        JPanel desc_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        desc_panel.add(desc);
        desc_panel = style.setBackground(desc_panel);



        // text panel
        JPanel text_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField increment_text = new JTextField("text");
        increment_text.setPreferredSize(new Dimension(200, 30));
        increment_text = style.textFieldStyles(increment_text);

        text_panel.add(increment_text);
        text_panel = style.setBackground(text_panel);

        // step panel
        JPanel step_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField increment_step = new JTextField("1");
        increment_step.setPreferredSize(new Dimension(50, 30));
        increment_step = style.textFieldStyles(increment_step);

        step_panel.add(increment_step);
        step_panel = style.setBackground(step_panel);

        // btn
        JButton save_btn = new JButton("Save");
        save_btn.setPreferredSize(new Dimension(100, 30));
        save_btn = style.buttonStyles(save_btn);

        JDialog finalPopup = popup;
        JTextField finalIncrement_text = increment_text;
        JTextField finalIncrement_step = increment_step;

        save_btn.addActionListener(e -> {
            Option_Increment.save_settings(
                    finalIncrement_text.getText(),
                    Integer.parseInt(finalIncrement_step.getText())
            );
            finalPopup.dispose();
            previewOptions();
        });

        // popup props
        popup.add(desc_panel);
        popup.add(text_panel);
        popup.add(step_panel);
        popup.add(save_btn);

        return popup;
    }
    private JDialog AddPhasePopup() {
        JDialog popup = new JDialog(frame, "Enter details", true);

        popup.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        popup.setSize(350, 200);
        popup = style.popupStyle(popup);

        // title
        JTextArea desc = new JTextArea("Add a text after an index, " +
                "0 -> first letter");
        desc.setPreferredSize(new Dimension(330, 30));
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc = style.setBackground(desc);

        // text panel
        JPanel text_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField phase_text = new JTextField("Phase");
        phase_text = style.textFieldStyles(phase_text);

        phase_text.setPreferredSize(new Dimension(200, 30));
        text_panel.add(phase_text);
        text_panel = style.setBackground(text_panel);

        // step panel
        JPanel index_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField phase_index = new JTextField("0");
        phase_index = style.textFieldStyles(phase_index);

        phase_index.setPreferredSize(new Dimension(50, 30));
        index_panel.add(phase_index);
        index_panel = style.setBackground(index_panel);

        // btn
        JButton save_btn = new JButton("Save");
        save_btn.setPreferredSize(new Dimension(100, 30));
        save_btn = style.buttonStyles(save_btn);

        JTextField finalPhase_text = phase_text;
        JTextField finalPhase_index = phase_index;
        JDialog finalPopup = popup;

        save_btn.addActionListener(e -> {
            Option_AddPhase.save_settings(
                    finalPhase_text.getText(),
                    Integer.parseInt(finalPhase_index.getText())

            );

            finalPopup.dispose();
            previewOptions();
        });

        // popup props
        popup.add(desc);
        popup.add(text_panel);
        popup.add(index_panel);
        popup.add(save_btn);

        return popup;
    }
    private JDialog RemovePhasePopup() {
        JDialog popup = new JDialog(frame, "Enter details", true);

        popup.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        popup.setSize(350, 200);
        popup = style.popupStyle(popup);

        // title
        JTextArea desc = new JTextArea("Remove first matching, or all matching");
        desc.setPreferredSize(new Dimension(330, 30));
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc = style.setBackground(desc);

        // text panel
        JPanel text_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5,0));
        JTextField phase_text = new JTextField("Phase");
        phase_text = style.textFieldStyles(phase_text);

        phase_text.setPreferredSize(new Dimension(200, 30));
        text_panel.add(phase_text);
        text_panel = style.setBackground(text_panel);

        // replace all btn
        JPanel ck_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2,0));
        JCheckBox checkbox = new JCheckBox("Replace all?");
        checkbox = style.checkboxStyle(checkbox);

        checkbox.setPreferredSize(new Dimension(150, 30));
        ck_panel.add(checkbox);
        ck_panel = style.setBackground(ck_panel);


        // case sensitive
        JPanel ck_panel_2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2,0));
        JCheckBox caseSensitive = new JCheckBox("Case sensitive?");
        caseSensitive = style.checkboxStyle(caseSensitive);

        caseSensitive.setPreferredSize(new Dimension(150, 30));
        ck_panel_2.add(caseSensitive);
        ck_panel_2 = style.setBackground(ck_panel_2);

        // btn
        JButton save_btn = new JButton("Save");
        save_btn.setPreferredSize(new Dimension(100, 30));
        save_btn = style.buttonStyles(save_btn);

        JTextField finalPhase_text = phase_text;
        JCheckBox finalCheckbox = checkbox;
        JCheckBox finalCaseSensitive = caseSensitive;
        JDialog finalPopup = popup;

        save_btn.addActionListener(e -> {
            Option_RemovePhase.save_settings(
                    finalPhase_text.getText(),
                    finalCheckbox.isSelected(),
                    finalCaseSensitive.isSelected()

            );
            finalPopup.dispose();
            previewOptions();
        });

        // popup props
        popup.add(desc);
        popup.add(text_panel);
        popup.add(ck_panel);
        popup.add(ck_panel_2);
        popup.add(save_btn);

        return popup;
    }

    // file list
    private void updateFileList(DefaultListModel<String> listModel) {
        listModel.clear();
        selected_files.clear();

        for(String item: fileNames) {
            listModel.addElement(item);
        }

        filesTitle.setText(
                "Files (" + this.fileNames.size() + " files)"
        );
    }
    private JCheckBox select_all() {
        JCheckBox select_all = new JCheckBox("Select all");
        select_all.setPreferredSize(new Dimension(250, 30));
        select_all = style.checkboxStyle(select_all);
        JCheckBox finalSelect_all = select_all;

        select_all.addActionListener(e -> {
            this.select_all_flag = finalSelect_all.isSelected();
            updateFileList((DefaultListModel<String>) list.getModel());
        });

        return select_all;
    }
    private void previewOptions() {
        ArrayList<String> arr = new ArrayList<>();
            if(this.selected_options.contains(Option_Increment.name_id)) {
                arr = Option_Increment.implement(this.selected_files, fileNames);
            }
            if(this.selected_options.contains(Option_AddPhase.name_id)) {
                arr = Option_AddPhase.implement(this.selected_files, fileNames);
            }
            if(this.selected_options.contains(Option_RemovePhase.name_id)) {
                arr = Option_RemovePhase.implement(this.selected_files, fileNames);
            }
            if(this.selected_options.contains(Option_Lowercase.name_id)) {
                arr = Option_Lowercase.implement(this.selected_files, fileNames);
            }
            if(this.selected_options.contains(Option_Uppercase.name_id)) {
                arr = Option_Uppercase.implement(this.selected_files, fileNames);
            }



        this.fileNames = arr;
        updateFileList((DefaultListModel<String>) list.getModel());
    }
    private JButton renameFiles() {
        JButton renameBtn = new JButton("Rename selected");
        renameBtn = style.buttonStyles(renameBtn);

        renameBtn.addActionListener(e -> {

            for(Map.Entry<String, String> entry : FilesMap.toRenameMatrix.entrySet()) {
                File current = new File(entry.getKey());
                String extension = FilesMap.getExtension(current);

                File new_name = new File(current.getParent() + "\\" + entry.getValue() + extension);

                if(current.renameTo(new_name)) {
                    System.out.println("Renamed " + entry.getValue());
                }else {
                    System.out.println("!!! Errror" + entry.getValue());
                }
            }

            readDirFiles(new File(this.current_path));

            JOptionPane.showMessageDialog(
                    frame,
                    "Succesfully renamed selected files!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        return renameBtn;
    }
    private void readDirFiles(File folder) {
        FilesMap.toRenameMatrix = new HashMap<>();
        this.fileNames = new ArrayList<>();
        ArrayList<File> fileList = new ArrayList<>();

        File[] files = folder.listFiles();

        if(files != null) {
            for(File file : files) {
                this.fileNames.add(FilesMap.removeExtension(file));
                FilesMap.toRenameMatrix.put(file.getAbsolutePath(), FilesMap.removeExtension(file));
                fileList.add(file);
            }
        }

        updateFileList((DefaultListModel<String>) list.getModel());
    }
    private JLabel filesTitle = new JLabel("Files");
    private JLabel websiteSupport() {
        JLabel website = new JLabel("Website support");
        website.setPreferredSize(new Dimension(150, 30));
        website.setHorizontalAlignment(SwingConstants.CENTER);
        website = style.titleStyle(website);
        JLabel finalWebsite = website;

        website.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(
                            new URI("https://fileedit.netlify.app/")
                    );
                }catch(Exception ex) {
                    System.out.println("Website doesnt open");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                finalWebsite.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // Change cursor to hand
            }

            @Override
            public void mouseExited(MouseEvent e) {
                finalWebsite.setCursor(Cursor.getDefaultCursor());  // Change cursor back to default
            }


        });

        return website;
    }

    // app
    public App() {
        ImageIcon icon = new ImageIcon("src/resources/images/logo.png");
        frame.setIconImage(icon.getImage());


        // panel
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // title of the app
        JLabel title = new JLabel("FileEdit v." + version);
        title.setPreferredSize(new Dimension(500, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title = style.titleStyle(title);
        panel.add(title);

        // path search si filde explorer
        JTextField manual_path_inp = new JTextField("folder location path");
        manual_path_inp.setPreferredSize(new Dimension(325, 30));
        manual_path_inp = style.textFieldStyles(manual_path_inp);

        panel.add(manual_path_inp);
        panel.add(createManualPathButton(manual_path_inp));

        // File explorer
        panel.add(createFileExplorer());

        // options menu
        JLabel optionsTitle = new JLabel("Options");
        optionsTitle.setPreferredSize(new Dimension(500, 30));
        optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        optionsTitle = style.titleStyle(optionsTitle);
        panel.add(optionsTitle);

        filesTitle = style.titleStyle(filesTitle);

        // options menu
        panel.add(createOptionsMenu());

        // separator
        panel.add(getSeparator());

        // files list
        filesTitle.setPreferredSize(new Dimension(500, 30));
        filesTitle.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(filesTitle);

        // checkbox and rename button
        panel.add(createFilesList());

        // options and button
        panel.add(select_all());
        panel.add(renameFiles());

        // suport
        panel.add(websiteSupport());
        panel = style.setBackground(panel);
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        // frame options
        panel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

        frame.setContentPane(panel);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 630);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        JOptionPane notice = new JOptionPane(
                "I am not responsible for any file loss due to bad programming, thank you !",
                JOptionPane.INFORMATION_MESSAGE);

        JDialog dialog = notice.createDialog("Notice");
        dialog = style.setBackground(dialog);

        ImageIcon icon = new ImageIcon("src/resources/images/logo.png");
        dialog.setIconImage(icon.getImage());

        dialog.setVisible(true);
        SwingUtilities.invokeLater(App::new);
    }
}

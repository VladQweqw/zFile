package app.pages.Rename;

import app.exceptions.NotDirectory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import app.pages.Rename.options.*;
import app.pages.Page;

import static app.pages.Rename.options.Option.option_btns;

public class Rename extends Page {
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

    private JList<String> list;
    private String current_path;
    private final HashSet<String> selected_files = new HashSet<>();
    private boolean select_all_flag = false;
    private final HashSet<String> selected_options = new HashSet<String>();
    private ArrayList<String> fileNames = new ArrayList<>();

    // class instances
    Increment Option_Increment = new Increment();
    LowerCase Option_Lowercase = new LowerCase();
    UpperCase Option_Uppercase = new UpperCase();
    AddPhase Option_AddPhase = new AddPhase();
    RemovePhase Option_RemovePhase = new RemovePhase();

    // create components
    private JButton createFileExplorer() {
        JButton fileChooserBtn = new JButton("Select from PC");

        fileChooserBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(panel);

            if(result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                search_bar.setText(selectedFile.getAbsolutePath());

                readDirFiles(selectedFile);
                this.current_path = selectedFile.getAbsolutePath();
            }
        });

        return fileChooserBtn;
    }
    private JButton createManualPathButton(JTextField inp) {
        JButton manualPathBtn = new JButton("Scan");
        manualPathBtn.setPreferredSize(new Dimension(100, 30));

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
                        panel,
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

        for(Map.Entry<String, String> entry : option_btns.entrySet()) {
            final JButton btn = new JButton(entry.getKey());

            btn.addActionListener(e -> {
                this.selected_options.add(entry.getValue());
                openInputPopup(entry.getValue());
            });

            btn_wrappers.add(btn);
        }

        JScrollPane scrollPane = new JScrollPane(
                btn_wrappers,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(480, 65));

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

            if(select_all_flag) {
                label.setBackground(new Color(0x8E24AA));

                selected_files.clear();
                selected_files.addAll(fileNames);


            }else {
                if(isSelected) {
                    label.setBackground(new Color(0x8E24AA));

                    selected_files.add(value);
                } else {
                    selected_files.remove(value);
                }
            }

            return label;
        });

        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Adding key listener to capture the Ctrl+A event
        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_A) {
                    selected_files.clear();
                    selected_files.addAll(fileNames);
                }
            }
        });

        JScrollPane jScrollPane = new JScrollPane(list);
        jScrollPane.setPreferredSize(new Dimension(480, 225));

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

        popup.setLocationRelativeTo(panel);
    }
    private JDialog IncrementPopup() {
        JDialog popup = new JDialog(frame, "Enter details", true);

        popup.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        popup.setSize(350, 230);

        // desc
        JTextArea desc = new JTextArea();
        desc.setText("Add a text and a step increase for the text increment, for example text 1, " +
                "text 2," +
                " text 3");
        desc.setPreferredSize(new Dimension(330, 40));
        desc.setEditable(false);
        desc.setLineWrap(true);

        JPanel desc_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        desc_panel.add(desc);



        // text panel
        JPanel text_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField increment_text = new JTextField("text");
        increment_text.setPreferredSize(new Dimension(200, 30));

        text_panel.add(increment_text);

        // step panel
        JPanel step_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField increment_step = new JTextField("1");
        increment_step.setPreferredSize(new Dimension(50, 30));

        step_panel.add(increment_step);

        // btn
        JButton save_btn = new JButton("Save");
        save_btn.setPreferredSize(new Dimension(100, 30));

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
        popup.setLocationRelativeTo(null);

        return popup;
    }
    private JDialog AddPhasePopup() {
        JDialog popup = new JDialog(frame, "Enter details", true);

        popup.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        popup.setSize(350, 200);

        // title
        JTextArea desc = new JTextArea("Add a text after an index, " +
                "0 -> first letter");
        desc.setPreferredSize(new Dimension(330, 30));
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);

        // text panel
        JPanel text_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField phase_text = new JTextField("Phase");

        phase_text.setPreferredSize(new Dimension(200, 30));
        text_panel.add(phase_text);

        // step panel
        JPanel index_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField phase_index = new JTextField("0");

        phase_index.setPreferredSize(new Dimension(50, 30));
        index_panel.add(phase_index);

        // btn
        JButton save_btn = new JButton("Save");
        save_btn.setPreferredSize(new Dimension(100, 30));

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
        popup.setLocationRelativeTo(null);

        return popup;
    }

    private JDialog RemovePhasePopup() {
        JDialog popup = new JDialog(frame, "Enter details", true);

        popup.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        popup.setSize(350, 230);

        // title
        JTextArea desc = new JTextArea("Remove first matching, or all matching");
        desc.setPreferredSize(new Dimension(330, 30));
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);

        // text panel
        JPanel text_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5,0));
        JTextField phase_text = new JTextField("Phase");

        phase_text.setPreferredSize(new Dimension(200, 30));
        text_panel.add(phase_text);

        // replace all btn
        JPanel ck_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2,0));
        JCheckBox checkbox = new JCheckBox("Replace all?");

        checkbox.setPreferredSize(new Dimension(150, 30));
        ck_panel.add(checkbox);


        // case sensitive
        JPanel ck_panel_2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2,0));
        JCheckBox caseSensitive = new JCheckBox("Case sensitive?");

        caseSensitive.setPreferredSize(new Dimension(150, 30));
        ck_panel_2.add(caseSensitive);

        // btn
        JButton save_btn = new JButton("Save");
        save_btn.setPreferredSize(new Dimension(100, 30));

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
        popup.setLocationRelativeTo(null);

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
                this.selected_options.remove("increment");

            }
            if(this.selected_options.contains(Option_AddPhase.name_id)) {
                arr = Option_AddPhase.implement(this.selected_files, fileNames);
                this.selected_options.remove("add_phase");

            }
            if(this.selected_options.contains(Option_RemovePhase.name_id)) {
                arr = Option_RemovePhase.implement(this.selected_files, fileNames);
                this.selected_options.remove("remove_phase");

            }
            if(this.selected_options.contains(Option_Lowercase.name_id)) {
                arr = Option_Lowercase.implement(this.selected_files, fileNames);
                this.selected_options.remove("lowercase");

            }
            if(this.selected_options.contains(Option_Uppercase.name_id)) {
                arr = Option_Uppercase.implement(this.selected_files, fileNames);
                this.selected_options.remove("uppercase");
            }



        this.fileNames = arr;
        updateFileList((DefaultListModel<String>) list.getModel());
    }
    private JButton renameFiles() {
        JButton renameBtn = new JButton("Rename selected");
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
                    panel,
                    "Succesfully renamed selected files!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        return renameBtn;
    }
    private void readDirFiles(File folder) {
        System.out.println("READ");
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

    private JTextField search_bar = new JTextField(this.current_path);

    private JFrame frame;
    // app
    public Rename(JFrame frame) {
        this.frame = frame;

        // path search si filde explorer
        search_bar.setPreferredSize(new Dimension(325, 30));

        panel.add(search_bar);
        panel.add(createManualPathButton(search_bar));

        // File explorer
        panel.add(createFileExplorer());

        // options menu
        JLabel optionsTitle = new JLabel("Options");
        optionsTitle.setPreferredSize(new Dimension(500, 30));
        optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(optionsTitle);


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


        // panel options
        panel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
    }

    public JPanel getPanel() {
        return this.panel;
    }

}

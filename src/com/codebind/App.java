package com.codebind;

import com.codebind.exceptions.NotDirectory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.codebind.options.Option.option_btns;

import com.codebind.options.*;

public class App {
    private final String version = "1.0.0";
    private final JFrame frame = new JFrame("FileEdit");
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    private JList<String> list;
    private String current_path = "";
    private final HashSet<String> selected_files = new HashSet<>();
    private boolean select_all_flag = false;
    private HashSet<String> selected_options = new HashSet<String>();
    private ArrayList<File> fileList = new ArrayList<>();
    private ArrayList<String> fileNames = new ArrayList<>();

    // class instances
    Increment Option_Increment = new Increment();
    LowerCase Option_Lowercase = new LowerCase();
    UpperCase Option_Uppercase = new UpperCase();
    AddPhase Option_AddPhase = new AddPhase();
    RemovePhase Option_RemovePhase = new RemovePhase();

    private ArrayList<Font> import_font() {
        ArrayList<Font> fonts = new ArrayList<>();

        // regular
        try {
            Font poppins_reg = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Bold.ttf"));

            fonts.add(poppins_reg);
        }
        catch(FontFormatException | IOException e) {
            System.out.println("Error loading Poppins Regular font");

            Font def = new Font("Arial", Font.PLAIN, 16);
            fonts.add(def);
        }

        // medium
        try {
            Font poppins_med = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Regular.ttf"));

            fonts.add(poppins_med);
        }
        catch(FontFormatException | IOException e) {
            System.out.println("Error loading Poppins Medium font");


            Font def = new Font("Arial", Font.PLAIN, 16);
            fonts.add(def);
        }


        // bold
        try {
            Font poppins_bold = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Bold.ttf"));

            fonts.add(poppins_bold);
        }
        catch(FontFormatException | IOException e) {
            System.out.println("Error loading Poppins Bold font");


            Font def = new Font("Arial", Font.PLAIN, 16);
            fonts.add(def);
        }


        return fonts;
    }

    // create components
    private JButton createFileExplorer() {
        JButton fileChooserBtn = new JButton("Select from PC");
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

        for(Map.Entry<String, String> entry : option_btns.entrySet()) {
            JButton btn = new JButton(entry.getValue());

            btn.addActionListener(e -> {
                if(this.selected_options.contains(entry.getValue())) {
                    this.selected_options.remove(entry.getValue());
                    btn.setBackground(new Color(255, 255,255));

                }else {
                    this.selected_options.add(entry.getValue());
                    btn.setBackground(new Color(255, 0,0));

                    openInputPopup(entry.getValue());
                }

            });

            btn_wrappers.add(btn);
        }

        JScrollPane scrollPane = new JScrollPane(
                btn_wrappers,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(480, 55));

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
                label.setForeground(Color.WHITE);
                label.setBackground(Color.BLUE);

                selected_files.add(value);


            }else {
                if(isSelected) {
                    label.setForeground(Color.WHITE);
                    label.setBackground(Color.BLUE);
                    selected_files.add(value);

                } else {
                    selected_files.remove(value);

                    label.setForeground(Color.BLACK);
                    label.setBackground(Color.WHITE);


                }
            }

            return label;
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
        }else if(type.equals(Option_Uppercase.name_id)) {
            previewOptions();
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

        // title
        JTextArea desc = new JTextArea("Add a text and a step increase for the text increment, for example text 1, " +
                "text 2," +
                " text 3");
        desc.setPreferredSize(new Dimension(330, 30));
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);

        // text panel
        JPanel text_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField increment_text = new JTextField("text");
        increment_text.setPreferredSize(new Dimension(200, 30));
        text_panel.add(increment_text);

        // step panel
        JPanel step_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        JTextField increment_step = new JTextField("step");
        increment_step.setPreferredSize(new Dimension(50, 30));
        step_panel.add(increment_step);

        // btn
        JButton save_btn = new JButton("Save");
        save_btn.setPreferredSize(new Dimension(100, 30));

        save_btn.addActionListener(e -> {
            Option_Increment.save_settings(
                    increment_text.getText(),
                    Integer.parseInt(increment_step.getText())
            );
            popup.dispose();
            previewOptions();
        });

        // popup props
        popup.add(desc);
        popup.add(text_panel);
        popup.add(step_panel);
        popup.add(save_btn);

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

        save_btn.addActionListener(e -> {
            Option_AddPhase.save_settings(
                    phase_text.getText(),
                    Integer.parseInt(phase_index.getText())

            );
            popup.dispose();
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

        save_btn.addActionListener(e -> {
            Option_RemovePhase.save_settings(
                    phase_text.getText(),
                    checkbox.isSelected(),
                    caseSensitive.isSelected()

            );
            popup.dispose();
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
    }
    private JCheckBox select_all() {
        JCheckBox select_all = new JCheckBox("Select all");
        select_all.setPreferredSize(new Dimension(250, 30));

        select_all.addActionListener(e -> {
            this.select_all_flag = select_all.isSelected();
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
        renameBtn.setPreferredSize(new Dimension(150, 30));

        renameBtn.addActionListener(e -> {

            for(Map.Entry<String, String> entry : FilesMap.toRenameMatrix.entrySet()) {
                File current = new File(entry.getKey());
                String extension = FilesMap.getExtension(current);

                File new_name = new File(current.getParent() + "\\" + entry.getValue() + extension);

                if(current.renameTo(new_name)) {
                    FilesMap.removeOldAddNew(current.getAbsolutePath(), new_name, entry.getValue());
                    System.out.println("Renamed " + entry.getValue());
                }else {
                    System.out.println("!!! Errror" + entry.getValue());
                }
            }

            JOptionPane.showMessageDialog(
                    frame,
                    "Succesfully renamed selected files!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        FilesMap.replaceArrays();
        return renameBtn;
    }
    private void readDirFiles(File folder) {
        FilesMap.toRenameMatrix = new HashMap<>();
        this.fileNames = new ArrayList<>();
        this.fileList = new ArrayList<>();

        File[] files = folder.listFiles();

        if(files != null) {
            for(File file : files) {
                this.fileNames.add(FilesMap.removeExtension(file));
                FilesMap.toRenameMatrix.put(file.getAbsolutePath(), FilesMap.removeExtension(file));
                this.fileList.add(file);
            }
        }

        updateFileList((DefaultListModel<String>) list.getModel());
    }

    // app
    public App() {
        // font import
        ArrayList<Font> fonts = import_font();

        // order regular - medium - bold
        Font poppins_regular = fonts.get(0);
        Font poppins_medium = fonts.get(1);
        Font poppins_bold = fonts.get(2);

        // ge
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(poppins_regular);
        ge.registerFont(poppins_medium);
        ge.registerFont(poppins_bold);

        poppins_regular.deriveFont(Font.PLAIN, 16);
        poppins_medium.deriveFont(Font.PLAIN, 16);
        poppins_bold.deriveFont(Font.BOLD, 16);

        //set the font
        panel.setFont(poppins_bold);

        // title of the app
        JLabel title = new JLabel("FileEdit v." + version);
        title.setPreferredSize(new Dimension(500, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title);

        // path search si filde explorer
        JTextField manual_path_inp = new JTextField("folder location path");
        manual_path_inp.setPreferredSize(new Dimension(325, 30));
        manual_path_inp.setMargin(new Insets(0,0,0, 20));

        panel.add(manual_path_inp);
        panel.add(createManualPathButton(manual_path_inp));
        panel.add(createFileExplorer());

        // options menu
        JLabel optionsTitle = new JLabel("Options");

        optionsTitle.setPreferredSize(new Dimension(500, 30));
        optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(optionsTitle);
        panel.add(createOptionsMenu());


        // files list
        JLabel filesTitle = new JLabel("Files");
        panel.add(getSeparator());
        filesTitle.setPreferredSize(new Dimension(500, 30));
        filesTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(filesTitle);


        panel.add(createFilesList());

        // checkbox and rename button
        panel.add(select_all());
        panel.add(renameFiles());


        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // frame options
        panel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

        frame.setContentPane(panel);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(
                null,
                "I am not responsible for any file loss due to bad programming, thank you !",
                "Notice",
                JOptionPane.INFORMATION_MESSAGE);

        SwingUtilities.invokeLater(App::new);


    }
}

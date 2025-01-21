package app.pages.Group;

import app.exceptions.NotDirectory;
import app.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class GroupBy extends Page {
    // variables
    private String current_path;
    private ArrayList<FileObject> filesList = new ArrayList<>();
    private Integer groupOption = -1;

    // components
    private static JPanel panel = new JPanel();
    private static JFrame frame;
    private JButton fileExplorer = new JButton("Select from PC");
    private JButton manual_scan = new JButton("Scan");
    private JTextField search_bar = new JTextField(this.current_path);
    private JList<String> list = new JList<>();
    private JScrollPane jScrollPane = new JScrollPane();
    private JLabel optionsTitle = new JLabel("Options");
    private JLabel filesTitle = new JLabel("Files");
    private Options options = new Options();
    private JButton groupBtn = new JButton("Group files");
    private ButtonGroup btn_group = new ButtonGroup();

    // listeners
    private void createFileExplorer() {
        fileExplorer.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(panel);

            if(result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                search_bar.setText(selectedFile.getAbsolutePath());
                this.current_path = selectedFile.getAbsolutePath();

                readDirFiles(selectedFile);
                updateFileList((DefaultListModel<String>) list.getModel());
            }
        });
    }
    private void createManualPathButton() {
        manual_scan.setPreferredSize(new Dimension(100, 30));

        manual_scan.addActionListener(e -> {
            try {
                String path = search_bar.getText();

                if(!new File(path).isDirectory()) {
                    throw new NotDirectory();
                }

                this.current_path = path;
                readDirFiles(new File(path));
                updateFileList((DefaultListModel<String>) list.getModel());
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
    }
    private void createFilesList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        this.list = new JList<>(listModel);
        updateFileList(listModel);

        this.list.setCellRenderer((list1, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value);
            label.setOpaque(true);

            return label;
        });

        this.jScrollPane = new JScrollPane(list);
        this.jScrollPane.setPreferredSize(new Dimension(480, 225));
    }

    // methods
    private void readDirFiles(File folder) {
        File[] files = folder.listFiles();

        if(files != null) {
            for(File file : files) {
                if(file.isDirectory()) continue;

                filesList.add(
                        new FileObject(file)
                );

            }
        }

    }
    private void updateFileList(DefaultListModel<String> list) {
        list.clear();

        for(FileObject file : this.filesList) {
            list.addElement(file.edited_name);
        }

        filesTitle.setText(
                "Files (" + this.filesList.size() + " files)"
        );
    }


    // constructor
    public GroupBy(JFrame frame) {
        this.frame = frame;

        search_bar.setPreferredSize(new Dimension(325, 30));
        panel.add(search_bar);

        panel.add(manual_scan);
        createManualPathButton();

        // File explorer
        panel.add(fileExplorer);
        createFileExplorer();

        // options menu
        optionsTitle.setPreferredSize(new Dimension(500, 30));
        optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(optionsTitle);

        for(Option option : options.option_names) {
            JRadioButton radio = new JRadioButton(option.toString());

            radio.addActionListener(e -> {
                if(radio.isSelected()) {
                    this.groupOption = option.option_id;
                }
            });
            btn_group.add(radio);
            panel.add(radio);
        }

        // files title
        filesTitle.setPreferredSize(new Dimension(500, 30));
        filesTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(filesTitle);

        // files list
        createFilesList();
        panel.add(jScrollPane);

        // group btn
        groupBtn.addActionListener(e -> {
            Boolean status = options.groupFiles(this.filesList, this.groupOption);

            if(status) {
                JOptionPane.showMessageDialog(
                        panel,
                        "Succesfully grouped the files",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(
                        panel,
                        "Some files failed to be moved",
                        "Partial success",
                        JOptionPane.WARNING_MESSAGE);
            }

        });
        panel.add(groupBtn);

        // panel options
        panel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}

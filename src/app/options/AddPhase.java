package app.options;


import app.FilesMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class AddPhase extends Option {
    public String phase = "";
    public int index = 0;
    public String name_id = "";

    public AddPhase() {
        super(
                "Add phase",
                "add_phase"
        );

        this.name_id = "add_phase";
    }

    public void save_settings(String phase, int index) {
        this.phase = phase;
        this.index = index;
    }

    @Override
    public ArrayList<String> implement(HashSet<String> files, ArrayList<String> FileNames) {
        ArrayList<String> new_arr = new ArrayList<>();

        for(int i = 0; i < FileNames.size(); i++) {
            String file_name = FileNames.get(i);

            if (files.contains(file_name)) {
                StringBuilder sb = new StringBuilder(file_name);
                File current_file = FilesMap.getByName(file_name);

                String new_file =   sb.insert(index, phase).toString();

                new_arr.add(new_file);
                FilesMap.updateItem(current_file, new_file);
            } else {
                new_arr.add(file_name);
            }
        }

        return new_arr;
    }
}

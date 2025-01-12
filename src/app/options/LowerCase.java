package app.options;


import app.FilesMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class LowerCase extends Option {
    public String name_id = "";
    public LowerCase() {
        super(
                "LowerCase",
                "lowercase"
        );

        this.name_id = "lowercase";
    }
    @Override
    public ArrayList<String> implement(HashSet<String> files, ArrayList<String> FileNames) {
        ArrayList<String> new_arr = new ArrayList<>();

        for(int i = 0; i < FileNames.size(); i++) {
            String file_name = FileNames.get(i);
            File current_file = FilesMap.getByName(file_name);

            if (files.contains(file_name)) {
                new_arr.add(file_name.toLowerCase());
                FilesMap.updateItem(current_file,  file_name.toLowerCase());

            } else {
                new_arr.add(file_name);
            }
        }

        return new_arr;
    }
}

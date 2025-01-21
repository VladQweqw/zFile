package app.pages.Rename.options;


import app.pages.Rename.FilesMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class Increment extends Option {
    public String text;
    public int step;

    public String name_id;
    public Increment() {
        super(
                "Increment",
                "increment"
        );

        name_id = "increment";
    }
    public void save_settings(String text, int step) {
        this.text = text;
        this.step = step;
    }

    @Override
    public ArrayList<String> implement(HashSet<String> selected_files, ArrayList<String> FileNames) {
        ArrayList<String> new_arr = new ArrayList<>();
        int y = 1;

        for(int i = 0; i < FileNames.size(); i++) {
            String file_name = FileNames.get(i);
            File current_file = FilesMap.getByName(file_name);

            if(selected_files.contains(file_name)) {
                String new_name = this.text + " " + (step * y++);

                new_arr.add(new_name);
                FilesMap.updateItem(current_file, new_name);
            }else {
                new_arr.add(file_name);
            }
        }
        return new_arr;
    }

}

package app.pages.Rename.options;


import app.pages.FileObject;

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
    public ArrayList<String> implement(HashSet<String> files, ArrayList<FileObject> FileNames) {
        ArrayList<String> new_arr = new ArrayList<>();

        for(int i = 0; i < FileNames.size(); i++) {
            FileObject file = FileNames.get(i);
            String name = file.edited_name;

            if (files.contains(name)) {
                StringBuilder sb = new StringBuilder(name);

                String new_name = sb.insert(index, phase).toString();

                file.setName(new_name);
                new_arr.add(new_name);
            } else {
                new_arr.add(name);
            }
        }

        return new_arr;
    }
}

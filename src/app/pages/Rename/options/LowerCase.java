package app.pages.Rename.options;


import app.pages.FileObject;

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
    public ArrayList<String> implement(HashSet<String> files, ArrayList<FileObject> FileNames) {
        ArrayList<String> new_arr = new ArrayList<>();

        for(int i = 0; i < FileNames.size(); i++) {
            FileObject file = FileNames.get(i);
            String name = file.edited_name;

            if (files.contains(name)) {
                String new_name = name.toLowerCase();

                new_arr.add(new_name);
                file.setName(new_name);
            } else {
                new_arr.add(name);
            }
        }

        return new_arr;
    }
}

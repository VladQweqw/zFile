package app.pages.Rename.options;


import app.pages.FileObject;

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
    public ArrayList<String> implement(HashSet<String> selected_files, ArrayList<FileObject> FileNames) {
        ArrayList<String> new_arr = new ArrayList<>();
        int y = 1;

        System.out.println(selected_files);
        for(int i = 0; i < FileNames.size(); i++) {
            FileObject file = FileNames.get(i);

            if(selected_files.contains(file.edited_name)) {
                String new_name = this.text + " " + (step * y++);

                new_arr.add(new_name);
                file.setName(new_name);
            }else {
                new_arr.add(file.edited_name);
            }
        }

        return new_arr;
    }

}

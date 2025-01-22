package app.pages.Rename.options;


import app.pages.FileObject;

import java.util.ArrayList;
import java.util.HashSet;

public class RemovePhase extends Option {
    public String phase = "";
    public String name_id = "";
    public boolean caseSensitive = false;
    public boolean replace_all = false;

    public RemovePhase() {
        super(
                "Remove Phase",
                "remove_phase"
        );

        this.name_id = "remove_phase";
    }

    public void save_settings(String phase, boolean replace_all, boolean caseSensitive) {
        this.phase = phase;
        this.replace_all = replace_all;
        this.caseSensitive = caseSensitive;
    }

    @Override
    public ArrayList<String> implement(HashSet<String> files, ArrayList<FileObject> FileNames) {
        ArrayList<String> new_arr = new ArrayList<>();

        for(int i = 0; i < FileNames.size(); i++) {
            FileObject file = FileNames.get(i);
            String name = file.edited_name;

            if (files.contains(name)) {
                String case_insensitive = "";
                String cp = phase;

                if(!this.caseSensitive) {
                    case_insensitive = "(?i)";
                    cp = cp.toLowerCase();
                }

                if(this.replace_all) {
                    String new_name = name.replaceAll(case_insensitive + cp, "");

                    new_arr.add(new_name);
                    file.setName(new_name);
                }else {

                    if(!caseSensitive) {
                        name.replaceFirst(phase.toUpperCase(), "");
                        name.replaceFirst(phase.toLowerCase(), "");

                        file.setName(name);
                    }else {
                        name.replaceFirst(phase, "");
                        file.setName(name);
                    }

                    new_arr.add(name);
                }
            } else {
                new_arr.add(name);
            }
        }

        return new_arr;
    }
}

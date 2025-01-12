package app.options;


import app.FilesMap;

import java.io.File;
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
    public ArrayList<String> implement(HashSet<String> files, ArrayList<String> FileNames) {
        ArrayList<String> new_arr = new ArrayList<>();

        for(int i = 0; i < FileNames.size(); i++) {
            String file_name = FileNames.get(i);
            File current_file = FilesMap.getByName(file_name);

            if (files.contains(file_name)) {
                String case_insensitive = "";
                String cp = phase;

                if(!this.caseSensitive) {
                    case_insensitive = "(?i)";
                    cp = cp.toLowerCase();
                }

                if(this.replace_all) {
                    new_arr.add(file_name.replaceAll(case_insensitive + cp, ""));
                    FilesMap.updateItem(current_file, file_name.replaceAll(case_insensitive + cp, ""));

                }else {
                    if(!caseSensitive) {
                        file_name = file_name.replaceFirst(phase.toUpperCase(), "");
                        file_name = file_name.replaceFirst(phase.toLowerCase(), "");

                        FilesMap.updateItem(current_file, file_name);
                    }else {
                        file_name = file_name.replaceFirst(phase, "");
                        FilesMap.updateItem(current_file, file_name);
                    }

                    new_arr.add(file_name);
                }
            } else {
                new_arr.add(file_name);
            }
        }

        return new_arr;
    }
}

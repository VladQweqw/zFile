package app.pages.Group;

import app.exceptions.FailToMove;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Options {

    public ArrayList<Option> option_names = new ArrayList<>();

    public Options() {
        option_names.add(new Option("year"));
        option_names.add(new Option("month"));
        option_names.add(new Option("day"));

    }

    private Boolean moveFile(Path src, Path dest) {
        try {
            Files.move(
                    src,
                    dest,
                    StandardCopyOption.REPLACE_EXISTING);
            return true;
        }catch(FailToMove | IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Boolean groupFiles(ArrayList<FileObject> files, Integer option) {
        Boolean flag = true;

        for(FileObject file : files) {
            String[] date = file.date_formated.split("-");
            String year = date[0];
            String month = date[1];
            String day = date[2];

            String parent = new File(file.path).getParent();
            Path dest_folder;

            if(option >= 0) {
                File year_dir = new File(parent + File.separator + year);

                if(!year_dir.isDirectory()) {
                    year_dir.mkdir();
                }

                if(option >= 1) {
                    File month_dir = new File(year_dir + File.separator + month);

                    if(!month_dir.isDirectory()) {
                        month_dir.mkdir();
                    }

                    if(option >= 2) {
                        File day_dir = new File(month_dir + File.separator + day);

                        if(!day_dir.isDirectory()) {
                            day_dir.mkdir();
                        }

                        // if all 3
                        dest_folder = new File(day_dir.toPath() + "\\" + file.edited_name).toPath();
                    }else {
                        // if only the year and month
                        dest_folder = new File(month_dir.toPath() + "\\" + file.edited_name).toPath();
                    }

                }else {
                    // if only the year
                    dest_folder = new File(year_dir.toPath() + "\\" + file.edited_name).toPath();
                }

                if(!moveFile(file.getPath(), dest_folder)) {
                    flag = false;
                }
            }else {
                return false;
            }
        }

        return flag;
    }

}

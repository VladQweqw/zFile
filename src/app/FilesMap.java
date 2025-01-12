package app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FilesMap {
    public static HashMap<String, String> toRenameMatrix = new HashMap<>();
    public static void updateItem(File file, String new_name) {
        if(file != null) {
            toRenameMatrix.replace(file.getAbsolutePath(), new_name);
        }
    }


    public static File getByName(String name) {
        for (Map.Entry<String, String> entry : toRenameMatrix.entrySet()) {
            if (entry.getValue().equals(name)) {
                return new File(entry.getKey());
            }
        }

        return null;
    }

    public static String getExtension(File path) {
        for(int i = path.getName().length() - 1; i > 0; i--) {
            if(path.getName().charAt(i) == '.') {
                return path.getName().substring(i);
            };
        };

        return null;
    }

    public static String removeExtension(File path) {


        for(int i = 0; i < path.getName().length(); i++) {
            if(path.getName().charAt(i) == '.') {
                return path.getName().substring(0, i);
            };
        };

        return path.getName();
    }

}

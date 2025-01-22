package app.pages.Rename.options;

import app.pages.FileObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Option {
    public static HashMap<String, String> option_btns = new HashMap<>();

    public Option(String name, String id_name) {
       option_btns.put(name, id_name);
    }

    public abstract ArrayList<String> implement(HashSet<String> y, ArrayList<FileObject> z);
}

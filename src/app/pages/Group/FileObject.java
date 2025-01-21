package app.pages.Group;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;

public class FileObject {
    public String name;
    public String path;
    public String edited_name;
    public String extension;
    public String date_formated;

    public static String getExtension(String name) {
        for(int i = name.length() - 1; i > 0; i--) {
            if(name.charAt(i) == '.') {
                return name.substring(i);
            };
        };

        return null;
    }

    public String convertMonth(int month) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        String conv = month + 1 > 9 ? "" + (month + 1) : "0" + (month + 1);

        return months[month] + " (" +  conv + ")";
    }

    public String formatDate(File file) {
        String formated;

        try {
            BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime creationTime = attributes.creationTime();

            Date creationDate = new Date(creationTime.toMillis());

            formated = (1900 + creationDate.getYear()) + "-" + convertMonth(creationDate.getMonth()) + "-" + creationDate.getDate();
            return formated;
        }catch (Exception e) {
            System.out.println("Error");
        }

        return "";
    }

    public FileObject(File file) {
        this.name = file.getName();
        this.path = file.getAbsolutePath();
        this.edited_name = file.getName();
        this.extension = getExtension(file.getName());
        this.date_formated = formatDate(file);

        System.out.println(this.toString());
    }

    public Path getPath() {
        return new File(this.path).toPath();
    }

    @Override
    public String toString() {
        String output = "";

        output += "Name: " + this.name + "\n";
        output += "Path: " + this.path + "\n";
        output += "EditedName: " + this.edited_name + "\n";
        output += "Extension: " + this.extension + "\n";
        output += "Date: " + this.date_formated + "\n";

        return output;
    }
}

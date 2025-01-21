package app.pages.Group;

public class Option {
    public int option_id;
    public String option_name;

    private static Integer id = 0;

    public Option(String name) {
        this.option_id = id;
        this.option_name = name;

        id++;
    }



    @Override
    public String toString() {
        return "By " + this.option_name;
    }
}

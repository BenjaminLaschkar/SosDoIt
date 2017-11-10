package ca.uqac.sosdoit.data;

/**
 * The list of all the Task possible for an advert
 */

public enum Task {

    // List of tasks :
    TEST            ("test"),
    BABYSITTING     ("Babysitting"),
    MATH_COURSE     ("Math course");
    // TODO

    private String text = "";

    Task(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }




}

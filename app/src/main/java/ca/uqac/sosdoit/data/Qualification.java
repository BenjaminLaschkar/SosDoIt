package ca.uqac.sosdoit.data;

/**
 * The list of all the Qualification possible for an advert
 */

public enum Qualification {

    TEST                ("Test"),
    MATH_ACKNOWLEDGE    ("Math Acknowledge");
    // TODO

    private String text = "";

    Qualification(String text) {
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

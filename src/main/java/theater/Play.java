package theater;

/**
 * Class representing a play with a name and type.
 */
public class Play {

    // Encapsulated fields
    private final String name;
    private final String type;

    /**
     * Creates a new Play with the given name and type.
     *
     * @param name the name of the play
     * @param type the type of the play (e.g., tragedy, comedy)
     */
    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the name of the play.
     *
     * @return the play's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the play.
     *
     * @return the play's type
     */
    public String getType() {
        return type;
    }
}


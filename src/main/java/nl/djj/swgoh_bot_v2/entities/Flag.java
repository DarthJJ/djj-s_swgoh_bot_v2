package nl.djj.swgoh_bot_v2.entities;

/**
 * @author djj
 */
public class Flag {
    private final transient String name;
    private final transient String description;
    private final transient String helpText;

    /**
     * Constructor.
     *
     * @param name        name of the flag.
     * @param description description of the flag.
     * @param helpText    text to be shown when asked for help.
     */

    public Flag(final String name, final String description, final String... helpText) {
        this.name = name;
        this.description = description;
        this.helpText = String.join(" ", helpText);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHelpText() {
        return helpText;
    }
}

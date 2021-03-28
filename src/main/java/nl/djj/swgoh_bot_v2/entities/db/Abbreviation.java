package nl.djj.swgoh_bot_v2.entities.db;

/**
 * @author DJJ
 */
public class Abbreviation {
    private final transient String baseId;
    private final transient String abbreviation;

    /**
     * Constructor.
     * @param baseId the unit Id.
     * @param abbreviation the abbreviation.
     */
    public Abbreviation(final String baseId, final String abbreviation) {
        super();
        this.baseId = baseId;
        this.abbreviation = abbreviation;
    }

    public String getBaseId() {
        return baseId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}

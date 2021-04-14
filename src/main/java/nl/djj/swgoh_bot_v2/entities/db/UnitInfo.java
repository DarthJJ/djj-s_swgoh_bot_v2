package nl.djj.swgoh_bot_v2.entities.db;

/**
 * @author DJJ
 */
public class UnitInfo {
    private final transient String baseId;
    private final transient String name;
    private final transient Alignment alignment;
    private final transient boolean character;


    /**
     * Constructor.
     * @param baseId the base id of the toon.
     * @param name the name of the toon.
     * @param alignment the alignment of the unit.
     * @param character if the char is a toon.
     */
    public UnitInfo(final String baseId, final String name, final String alignment, final boolean character) {
        super();
        this.baseId = baseId;
        this.name = name;
        this.alignment = Alignment.getByString(alignment);
        this.character = character;
    }

    public String getBaseId() {
        return baseId;
    }

    public String getName() {
        return name;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public boolean isCharacter() {
        return character;
    }
}

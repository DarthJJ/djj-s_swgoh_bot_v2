package nl.djj.swgoh_bot_v2.config;

import java.util.Map;

import static java.util.Map.entry;

/**
 * @author DJJ
 */
public final class SwgohConstants {
    /**
     * The Max Gear Level a toon can have.
     */
    public static final int MAX_GEAR_LEVEL = 13;

    /**
     * The Max Rarity level a toon can have.
     */
    public static final int MAX_RARITY_LEVEL = 7;

    /**
     * Gear level 13.
     */
    public static final int GEAR_LEVEL_13 = 13;

    /**
     * Gear level 12.
     */
    public static final int GEAR_LEVEL_12 = 12;

    /**
     * The Max Relic tier a toon can have.
     */
    public static final int MAX_RELIC_TIER = 8;

    /**
     * Allycode length.
     */
    public static final int ALLYCODE_LENGTH = 9;

    /**
     * The relic levels in the game.
     */
    public static final int[] RELIC_LEVELS = {0, 1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * The list of compare toons.
     */
    public static final Map<String, String> COMPARE_TOONS = Map.ofEntries(
            entry("JEDIKNIGHTREVAN", "Jedi Knight Revan"),
            entry("DARTHREVAN", "Darth Revan"),
            entry("DARTHMALAK", "Darth Malak"),
            entry("DARTHTRAYA", "Darth Traya"),
            entry("PADMEAMIDALA", "Padme Amidala"),
            entry("ENFYSNEST", "Enfys Nest"),
            entry("GENERALSKYWALKER", "General Anakin Skywalker"),
            entry("COMMANDERLUKESKYWALKER", "Commander Luke Skywalker"),
            entry("GRIEVOUS", "General Grievous"),
            entry("SUPREMELEADERKYLOREN", "Supreme Leader Kylo Ren"),
            entry("GLREY", "Rey"),
            entry("JEDIKNIGHTLUKE", "Jedi Knight Luke Skywalker"),
            entry("SITHPALPATINE", "Sith Eternal Emperor"),
            entry("GRANDMASTERLUKE", "Grand Master Luke Skywalker"),
            entry("CAPITALMALEVOLENCE", "Malevolence"),
            entry("CAPITALNEGOTIATOR", "Negotiator")
    );

    public static final Map<Integer, Double> relicScale = Map.ofEntries(
            entry(0, 0.0),
            entry(1, 0.05),
            entry(2, 0.1),
            entry(3, 0.1),
            entry(4, 0.15),
            entry(5, 0.15),
            entry(6, 0.2),
            entry(7, 0.25)
    );

    public static final Map<Integer, Double> gearScale = Map.ofEntries(
            entry(0, 0.0),
            entry(1, 0.01),
            entry(2, 0.01),
            entry(3, 0.01),
            entry(4, 0.01),
            entry(5, 0.03),
            entry(6, 0.05),
            entry(7, 0.07),
            entry(8, 0.08),
            entry(9, 0.1),
            entry(10, 0.11),
            entry(11, 0.13),
            entry(12, 0.17),
            entry(13, 0.22)
    );

    public static final Map<Integer, Double> rarityScale = Map.ofEntries(
            entry(0, 0.0),
            entry(1, 0.03),
            entry(2, 0.05),
            entry(3, 0.08),
            entry(4, 0.09),
            entry(5, 0.2),
            entry(6, 0.25),
            entry(7, 0.3)
    );

    /**
     * Constructor.
     */
    private SwgohConstants() {
        super();
    }
}

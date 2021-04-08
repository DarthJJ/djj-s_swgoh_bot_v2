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

    /**
     * Constructor.
     */
    private SwgohConstants() {
        super();
    }
}

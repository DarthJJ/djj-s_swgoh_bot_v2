package nl.djj.swgoh_bot_v2.entities.compare;

import nl.djj.swgoh_bot_v2.entities.db.Guild;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DJJ
 */
public class GuildCompare {
    private final transient Guild guild;
    private final transient int g13;
    private final transient int g12;
    private final transient int zetas;
    private final transient Map<Integer, Integer> relics;
    private final transient Map<String, UnitProfile> units;

    /**
     * Constructor.
     * @param guild the guild object.
     * @param g13 amount of G13.
     * @param g12 amount of G12.
     * @param zetas amount of zetas.
     */
    public GuildCompare(final Guild guild, final int g13, final int g12, final int zetas) {
        this.guild = guild;
        this.g13 = g13;
        this.g12 = g12;
        this.zetas = zetas;
        this.relics = new ConcurrentHashMap<>();
        this.units = new ConcurrentHashMap<>();
    }

    public Guild getGuild() {
        return guild;
    }

    public int getG13() {
        return g13;
    }

    public int getG12() {
        return g12;
    }

    public int getZetas() {
        return zetas;
    }

    public Map<Integer, Integer> getRelics() {
        return relics;
    }

    public Map<String, UnitProfile> getUnits() {
        return units;
    }

    /**
     * Add a relic count.
     * @param level the relic level.
     * @param amount the amount of relics.
     */
    public void addRelic(final int level, final int amount) {
        relics.put(level, amount);
    }

    /**
     * Add a unit profile.
     * @param identifier the id of the unit.
     * @param profile the unit profile.
     */
    public void addUnitProfile(final String identifier, final UnitProfile profile) {
        units.put(identifier, profile);
    }

    /**
     * @author DJJ
     */
    public static class UnitProfile {
        private final transient String name;
        private final transient int sevenStars;
        private final transient int sixStars;
        private final transient int g13;
        private final transient int g12;
        private final transient int zetas;
        private final transient int relic5plus;

        /**
         * Constructor.
         * @param name unit name.
         * @param sevenStars amount of seven stars.
         * @param sixStars amount of six stars.
         * @param g13 amount of G13.
         * @param g12 amount of G12.
         * @param zetas amount of zetas.
         * @param relic5plus amount of relic5plus.
         */
        public UnitProfile(final String name, final int sevenStars, final int sixStars, final int g13, final int g12, final int zetas, final int relic5plus) {
            this.name = name;
            this.sevenStars = sevenStars;
            this.sixStars = sixStars;
            this.g13 = g13;
            this.g12 = g12;
            this.zetas = zetas;
            this.relic5plus = relic5plus;
        }

        public String getName() {
            return name;
        }

        public int getSevenStars() {
            return sevenStars;
        }

        public int getSixStars() {
            return sixStars;
        }

        public int getG13() {
            return g13;
        }

        public int getG12() {
            return g12;
        }

        public int getZetas() {
            return zetas;
        }

        public int getRelic5plus() {
            return relic5plus;
        }
    }
}

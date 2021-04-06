package nl.djj.swgoh_bot_v2.config;

/**
 * @author DJJ
 */
public final class BotConstants {

    /**
     * The Owner ID of the bot.
     */
    public static final String OWNER_ID = "405842805441822721";
    /**
     * The path to store generated files.
     */
    public static final String FILE_STORAGE_PATH = "output/";
    /**
     * The bot colour for embeds.
     */
    public static final int BOT_COLOUR = (int) Long.parseLong("00FF00", 16);
    /**
     * Name of the bot.
     */
    public static final String BOT_NAME = "DJJ SWGOH bot";
    /**
     * Abbreviations download link.
     */
    public static final String ABBREVIATIONS_LINK = "https://www.dropbox.com/s/u405p8yrxudiji7/Abbreviations.csv?dl=1";
    /**
     * Farming locations download link.
     */
    public static final String FARMING_LOCATIONS_LINK = "https://www.dropbox.com/s/aarwxxjrt8tv4yj/farming_locations.csv?dl=1";
    /**
     * Zeta Advice download link.
     */
    public static final String ZETA_ADVICE_LINK = "https://www.dropbox.com/s/21vg2bu1qopk65q/zeta_info.csv?dl=1";
    /**
     * Mod Advice download link.
     */
    public static final String MOD_ADVICE_LINK = "http://apps.crouchingrancor.com/mods/advisor.json";
    /**
     * TB GGF download link.
     */
    public static final String TB_GGF_LINK = "https://www.dropbox.com/s/80281ffzvjub7i4/ggf_geo.csv?dl=1";
    /**
     * Crancor download link.
     */
    public static final String CRANCOR_TOONS_LINK = "https://www.dropbox.com/s/qx7vuy0fw57l1ii/CRancor_requirements.csv?dl=1";
    /**
     * Default bot prefix.
     */
    public static final String DEFAULT_PREFIX = "!#";

    /**
     * Constructor.
     */
    private BotConstants() {
        super();
    }
}

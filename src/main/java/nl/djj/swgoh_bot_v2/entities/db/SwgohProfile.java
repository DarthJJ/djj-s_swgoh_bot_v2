package nl.djj.swgoh_bot_v2.entities.db;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * @author DJJ
 */
public class SwgohProfile {
    private final transient int level;
    private final transient String guild;
    private final transient int toonRank;
    private final transient int shipRank;
    private final transient int gpTotal;
    private final transient int gpToons;
    private final transient int gpShips;
    private final transient URL profileUrl;
    private final transient Date lastUpdated;

    public SwgohProfile(final int level, final String guild, final int toonRank, final int shipRank, final int gpTotal, final int gpToons, final int gpShips, final String allycode, final Date lastUpdated){
        this.level = level;
        this.guild = guild;
        this.toonRank = toonRank;
        this.shipRank = shipRank;
        this.gpTotal = gpTotal;
        this.gpToons = gpToons;
        this.gpShips = gpShips;
        this.profileUrl = generateProfileUrl(allycode);
        this.lastUpdated = lastUpdated;
    }

    private URL generateProfileUrl(final String allycode){
        try {
            return new URL("https://swgoh.gg/p/" + allycode);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}

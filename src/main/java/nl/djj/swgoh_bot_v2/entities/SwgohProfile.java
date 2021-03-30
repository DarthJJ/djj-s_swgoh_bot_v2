package nl.djj.swgoh_bot_v2.entities;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author DJJ
 */
public class SwgohProfile {
    private transient String name;
    private transient int level;
    private transient String guild;
    private transient int toonRank;
    private transient int shipRank;
    private transient int gpTotal;
    private transient int gpToons;
    private transient int gpShips;
    private transient String profileUrl;
    private transient Date lastUpdated;

    /**
     * Constructor.
     */
    public SwgohProfile() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public String getGuild() {
        return guild;
    }

    public void setGuild(final String guild) {
        this.guild = guild;
    }

    public int getToonRank() {
        return toonRank;
    }

    public void setToonRank(final int toonRank) {
        this.toonRank = toonRank;
    }

    public int getShipRank() {
        return shipRank;
    }

    public void setShipRank(final int shipRank) {
        this.shipRank = shipRank;
    }

    public int getGpTotal() {
        return gpTotal;
    }

    public void setGpTotal(final int gpTotal) {
        this.gpTotal = gpTotal;
    }

    public int getGpToons() {
        return gpToons;
    }

    public void setGpToons(final int gpToons) {
        this.gpToons = gpToons;
    }

    public int getGpShips() {
        return gpShips;
    }

    public void setGpShips(final int gpShips) {
        this.gpShips = gpShips;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(final String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Creates an user object based on JSON.
     * @param userData the JSON data.
     * @return the user object.
     */
    public static SwgohProfile initFromJson(final JSONObject userData) {
        final SwgohProfile profile = new SwgohProfile();
        profile.setName(userData.getString("name"));
        profile.setLevel(userData.getInt("level"));
        profile.setGuild(userData.getString("guild_name"));
        profile.setToonRank(userData.getJSONObject("arena").getInt("rank"));
        profile.setShipRank(userData.getJSONObject("fleet_arena").getInt("rank"));
        profile.setGpTotal(userData.getInt("galactic_power"));
        profile.setGpToons(userData.getInt("character_galactic_power"));
        profile.setGpShips(userData.getInt("ship_galactic_power"));
        profile.setProfileUrl("https://swgoh.gg/p/" + userData.getInt("ally_code"));
        try {
            profile.setLastUpdated(new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss", Locale.ENGLISH).parse(userData.getString("last_updated")));
        } catch (final ParseException e) {
            profile.setLastUpdated(new Date());
        }
        return profile;
    }
}

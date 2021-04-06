package nl.djj.swgoh_bot_v2.entities;

import org.json.JSONObject;

/**
 * @author DJJ
 */
public final class SwgohGuild {
    private final transient int identifier;
    private transient String name;
    private transient int galacticPower;
    private transient int profiles;
    private transient int members;

    private SwgohGuild(final int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGalacticPower() {
        return galacticPower;
    }

    public void setGalacticPower(int galacticPower) {
        this.galacticPower = galacticPower;
    }

    public int getProfiles() {
        return profiles;
    }

    public void setProfiles(int profiles) {
        this.profiles = profiles;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    /**
     * Init from JSON data.
     * @param guildJson the JSON data.
     * @return the SWGOH Guild object.
     */
    public static SwgohGuild initFromJson(final JSONObject guildJson) {
        final JSONObject guildData = guildJson.getJSONObject("data");
        final SwgohGuild guild = new SwgohGuild(guildData.getInt("id"));
        guild.setName(guildData.getString("name"));
        guild.setGalacticPower(guildData.getInt("galactic_power"));
        guild.setProfiles(guildData.getInt("profile_count"));
        guild.setMembers(guildData.getInt("member_count"));
        return guild;
    }
}

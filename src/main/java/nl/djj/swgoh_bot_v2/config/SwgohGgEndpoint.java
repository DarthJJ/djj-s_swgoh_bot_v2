package nl.djj.swgoh_bot_v2.config;

/**
 * @author DJJ
 */
public enum SwgohGgEndpoint {
    ENDPOINT("https://swgoh.gg"),
    API(ENDPOINT.url + "/api/"),
    CHARACTER_ENDPOINT(API.url + "characters/"),
    SHIP_ENDPOINT(API.url + "ships/"),
    PLAYER_ENDPOINT(API.url + "player/"),
    GUILD_ENDPOINT(API.url + "guild/"),
    ABILITY_ENDPOINT(API.url + "abilities/"),
    GL_CHECKLIST_ENDPOINT(API.url + "gl-checklist/"),
    MOD_ENDPOINT(API.url + "players/%s/mods/");

    private final String url;

    SwgohGgEndpoint(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}

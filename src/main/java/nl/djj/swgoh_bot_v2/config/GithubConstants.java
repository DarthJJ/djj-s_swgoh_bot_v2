package nl.djj.swgoh_bot_v2.config;

/**
 * @author DJJ
 **/
public class GithubConstants {

    public static String GITHUB_ISSUES_API_URL = "https://api.github.com/repos/%s/%s/issues";
    public static String OAUTH_TOKEN;
    public static String REPO;
    public static String OWNER;
    public static String GITHUB_ISSUES_URL = "https://github.com/%s/%s/issues";

    /**
     * Constructor.
     **/
    private GithubConstants() {
        super();
    }

    public static void init(final String owner, final String repo, final String oauthToken) {
        GITHUB_ISSUES_API_URL = String.format(GITHUB_ISSUES_API_URL, owner, repo);
        GITHUB_ISSUES_URL = String.format(GITHUB_ISSUES_URL, owner, repo);
        OAUTH_TOKEN = oauthToken;
        REPO = repo;
        OWNER = owner;
    }
}

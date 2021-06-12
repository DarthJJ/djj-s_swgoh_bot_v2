package nl.djj.swgoh_bot_v2.config;

/**
 * @author DJJ
 **/
public final class GithubConstants {

    private static String OAUTH_TOKEN;
    private static String REPO;
    private static String OWNER;
    private static String GITHUB_URL = "https://github.com/%s/%s";
    private static String GITHUB_ISSUES_URL = "https://github.com/%s/%s/issues";

    /**
     * Constructor.
     **/
    private GithubConstants() {
        super();
    }

    /**
     * Init's the github constants met de variables in .env.
     *
     * @param owner      the repo owner.
     * @param repo       the repo name.
     * @param oauthToken the authentication token.
     */
    public static void init(final String owner, final String repo, final String oauthToken) {
        GITHUB_ISSUES_URL = String.format(GITHUB_ISSUES_URL, owner, repo);
        GITHUB_URL = String.format(GITHUB_URL, owner, repo);
        OAUTH_TOKEN = oauthToken;
        REPO = repo;
        OWNER = owner;
    }

    /**
     * The authentication token.
     *
     * @return the token.
     */
    public static String getOauthToken() {
        return OAUTH_TOKEN;
    }

    /**
     * Gets the repo name.
     *
     * @return the repo name.
     */
    public static String getREPO() {
        return REPO;
    }

    /**
     * Gets the owner name.
     *
     * @return the owner name.
     */
    public static String getOWNER() {
        return OWNER;
    }

    /**
     * Gets the url of the issues of the repo.
     *
     * @return the url.
     */
    public static String getGithubIssuesUrl() {
        return GITHUB_ISSUES_URL;
    }


    public static String getGithubUrl() {
        return GITHUB_URL;
    }
}

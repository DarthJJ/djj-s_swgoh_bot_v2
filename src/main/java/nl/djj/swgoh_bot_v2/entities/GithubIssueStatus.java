package nl.djj.swgoh_bot_v2.entities;

/**
 * @author DJJ
 **/
public class GithubIssueStatus {
    private final transient int issueId;
    private final transient String state;
    private final transient String assignee;
    private final transient String title;
    private final transient String body;
    private final transient String labels;
    private final transient String lastComment;
    private final transient String url;

    /**
     * The Constructor.
     * @param issueId the id.
     * @param state the state.
     * @param assignee the assigned person.
     * @param title the title.
     * @param body the body.
     * @param labels the labels.
     * @param lastComment the last comment.
     * @param url the url.
     */
    public GithubIssueStatus(final int issueId, final String state, final String assignee, final String title, final String body, final String labels, final String lastComment, final String url) {
        this.issueId = issueId;
        this.state = state;
        this.assignee = assignee;
        this.title = title;
        this.body = body;
        this.labels = labels;
        this.lastComment = lastComment;
        this.url = url;
    }

    public int getIssueId() {
        return issueId;
    }

    public String getState() {
        return state;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getLabels() {
        return labels;
    }

    public String getLastComment() {
        return lastComment;
    }

    public String getUrl() {
        return url;
    }
}

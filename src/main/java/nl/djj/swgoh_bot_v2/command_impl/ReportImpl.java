package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.GithubConstants;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.database.Database;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.GithubIssueStatus;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.exceptions.SQLRetrieveError;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;
import nl.djj.swgoh_bot_v2.helpers.StringHelper;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author DJJ
 **/
public class ReportImpl {
    private final transient String className = this.getClass().getName();
    private final transient Logger logger;
    private final transient DatabaseHandler dbHandler;
    private transient GitHub github;

    /**
     * Constructor.
     *
     * @param logger the logger.
     **/
    public ReportImpl(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.logger = logger;
        this.dbHandler = dbHandler;
        init();
    }

    private void init() {
        try {
            github = new GitHubBuilder().withOAuthToken(GithubConstants.getOauthToken()).build();
        } catch (final IOException exception) {
            logger.error(className, "init", exception.getMessage());
        }
    }

    /**
     * gets the status of an issue on github.
     *
     * @param message the message.
     */
    //CHECKSTYLE.OFF: NPathComplexity
    public void issueStatus(final Message message) {
        if (message.getArgs().isEmpty() || !message.getArgs().get(0).matches(".*\\d.*")) {
            message.error("Please provide an issue number");
            return;
        }
        final int issueNumber = Integer.parseInt(message.getArgs().get(0));
        try {
            final GHIssue issue = github.getRepository(GithubConstants.getOWNER() + "/" + GithubConstants.getREPO()).getIssue(issueNumber);
            if (issue.getHtmlUrl().toString().contains("/pull/")) {
                throw new GHFileNotFoundException();
            }
            String assignee = "not assigned";
            String labels = "no labels";
            String lastComment = "no comment";
            if (issue.getAssignee() != null) {
                assignee = issue.getAssignee().getName();
            }
            if (issue.getLabels().size() > 0) {
                labels = issue.getLabels().stream().map(GHLabel::getName).collect(Collectors.joining("\n"));
            }
            if (!issue.getComments().isEmpty()) {
                lastComment = issue.getComments().get(issue.getCommentsCount() - 1).getBody();
            }
            final GithubIssueStatus githubIssueStatus = new GithubIssueStatus(issue.getNumber(), issue.getState().name(), assignee, issue.getTitle(), issue.getBody(), labels, lastComment, issue.getUrl().toString());
            message.done(MessageHelper.formatIssueOverview(githubIssueStatus));
        } catch (final GHFileNotFoundException exception) {
            message.error("This issue does not exist, please go to the following link for all issues: \n" + GithubConstants.getGithubIssuesUrl());
        } catch (final IOException | IllegalArgumentException error) {
            logger.error(className, "getIssue", error.getMessage());
            message.error(error.getMessage());
        }
    }
    //CHECKSTYLE.ON: NPathComplexity

    /**
     * Creates an issue on github.
     *
     * @param message the message.
     */
    public void createIssue(final Message message) {
        try {
            if (!dbHandler.isUserAllowedToCreateTicket(message.getAuthorId())) {
                message.error("You are not allowed to create an ticket. Please contact the developer if you think this is wrong");
                return;
            }
            if (message.getArgs().isEmpty()) {
                message.error("Please provide a description of the issue");
                return;
            }
            final long issueId = github.getRepository(GithubConstants.getOWNER() + "/" + GithubConstants.getREPO()).createIssue("Bot created issue by: " + message.getAuthor())
                    .body(message.getArgs().stream().map(String::toString).collect(Collectors.joining(" ")))
                    .label("BotInbox").create().getNumber();
            message.done("Issue is created, for future reference about this issue, use ID: **" + issueId + "**\nThanks for your support and contribution");
        } catch (final IOException | SQLRetrieveError exception) {
            message.error("Something went wrong creating the issue, try again later");
        }
    }

    public void disallowUser(final Message message) {
        try {
            if (dbHandler.getPermissionForUser(message.getAuthorId()) == Permission.USER) {
                message.error("You are not allowed to run this command");
                return;
            }
            if (message.getArgs().isEmpty()){
                message.error("Please tag a user to disallow");
                return;
            }
            final String discordId = StringHelper.getDiscordIdFromTag(message.getAltArgs().get(0));
            dbHandler.setUserDisallowed(discordId);
            message.done(String.format("User: **%s** disallowed from creating tickets", String.join(" ", message.getArgs()).replace("@", "")));
        } catch (final SQLRetrieveError | SQLInsertionError error){
            message.error(error.getMessage());
        }
    }
}

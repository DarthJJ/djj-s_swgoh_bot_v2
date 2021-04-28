package nl.djj.swgoh_bot_v2.helpers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.config.SwgohConstants;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.GithubIssueStatus;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.compare.*;
import nl.djj.swgoh_bot_v2.entities.db.Config;
import nl.djj.swgoh_bot_v2.entities.db.Guild;
import nl.djj.swgoh_bot_v2.entities.swgoh.SwgohProfile;
import org.json.JSONArray;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author DJJ
 */
//CHECKSTYLE.OFF: MultipleStringLiteralsCheck
public final class MessageHelper {
    private static final String REACTION_POSITIVE = "U+1F44D";
    private static final String REACTION_ERROR = "U+274C";
    private static final String REACTION_DONE = "U+2705";
    private static final String GEAR_ICON = "\u2699";
    private static final String RELIC_ICON = "\uD83D\uDCDC";
    private static final String ZETA_ICON = "\u2742";
    //    private static final String RARITY_ICON = "\u2605";
    private static final String TABLE_FORMAT = "%-15s%-3s%-15s%n";
    private static final String UNIT_TABLE_FORMAT = "%-30s%-3s%-15s%n";
    private static final String HELP_TABLE_FORMAT = "%-10s%-3s%-15s%n";
    private static final String PROFILE_TABLE_FORMAT = "%-8s%-15s%-3s%-15s%n";
    private static final String GUILD_TABLE_FORMAT = "%-11s%-15s%-3s%-15s%n";
    private static final String GL_OVERVIEW_FORMAT = "%-28s%-5s%-5s%-5s%-6s%n";

    /**
     * Constructor.
     */
    private MessageHelper() {
        super();
    }

    private static MessageEmbed baseEmbed() {
        final EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(BotConstants.BOT_NAME, "https://www.youtube.com/watch?v=YddwkMJG1Jo", "https://cdn.discordapp.com/avatars/405842805441822721/83e13c25144df2db31aa963083e6dc40.png");
        builder.setColor(new Color(BotConstants.BOT_COLOUR));
        builder.setTimestamp(ZonedDateTime.now());
        builder.setFooter("Generated by: " + BotConstants.BOT_NAME);
        return builder.build();
    }

    /**
     * Sends a working reaction to the message.
     *
     * @param message the message.
     */
    public static void addWorkingReaction(final Message message) {
        message.getChannel().addReactionById(message.getMessageId(), REACTION_POSITIVE).queue();
    }

    /**
     * Sends an error reaction to the message.
     *
     * @param message the message.
     */
    public static void addErrorReaction(final Message message) {
        message.getChannel().addReactionById(message.getMessageId(), REACTION_ERROR).queue();
    }

    /**
     * Sends a positive reaction to the message.
     *
     * @param message the message.
     */
    public static void addDoneReaction(final Message message) {
        message.getChannel().addReactionById(message.getMessageId(), REACTION_DONE).queue();
    }

    /**
     * Creates the generic help text.
     *
     * @param helpText the help text.
     * @return the messageEmbed.
     */
    public static List<MessageEmbed> formatGenericHelpText(final Map<String, List<BaseCommand>> helpText) {
        final List<MessageEmbed> returnValue = new ArrayList<>();
        EmbedBuilder builder = new EmbedBuilder(baseEmbed());
        builder.appendDescription("Commands available for you");
        for (final Map.Entry<String, List<BaseCommand>> entry : helpText.entrySet()) {
            final StringBuilder helpString = new StringBuilder();
            for (final BaseCommand command : entry.getValue()) {
                helpString.append(String.format(HELP_TABLE_FORMAT, command.getName(), " = ", command.getDescription()));
            }
            helpString.append('\n');
            builder.addField(new MessageEmbed.Field(entry.getKey(), "```" + helpString + "```", false));
            if (builder.length() >= MessageEmbed.EMBED_MAX_LENGTH_BOT - 500) {
                returnValue.add(builder.build());
                builder = new EmbedBuilder(baseEmbed());
            }
        }
        returnValue.add(builder.build());
        return returnValue;
    }

    /**
     * Formats the generic info for an embed.
     *
     * @param guild the guild.
     * @return a message embed.
     */
    public static MessageEmbed formatGuildSwgohProfile(final Guild guild) {
        final EmbedBuilder builder = new EmbedBuilder(baseEmbed());
        builder.appendDescription("Guild information for: " + guild.getName());
        builder.addField(new MessageEmbed.Field("Members:", Integer.toString(guild.getMembers()), false));
        builder.addField(new MessageEmbed.Field("GP:", StringHelper.formatNumber(guild.getGalacticPower()), false));
        return builder.build();
    }

    /**
     * Formats the info to an embed.
     *
     * @param profile the profile.
     * @return a message embed.
     */
    public static MessageEmbed formatSwgohProfile(final SwgohProfile profile) {
        final EmbedBuilder builder = new EmbedBuilder(baseEmbed());
        builder.appendDescription("Generic Profile info for: " + profile.getName());
        final String generic = String.format(TABLE_FORMAT, "Level", "=", profile.getLevel()) +
                String.format(TABLE_FORMAT, "Guild", "=", profile.getGuild());
        builder.addField(new MessageEmbed.Field("Generic", "```" + generic + "```", false));
        final String galacticPower = String.format(TABLE_FORMAT, "GP_total", "=", profile.getGpTotal()) +
                String.format(TABLE_FORMAT, "GP toons", "=", profile.getGpToons()) +
                String.format(TABLE_FORMAT, "GP ships", "=", profile.getGpShips());
        builder.addField(new MessageEmbed.Field("GP", "```" + galacticPower + "```", false));
        final String arena = String.format(TABLE_FORMAT, "Toon Arena", "=", profile.getToonRank()) +
                String.format(TABLE_FORMAT, "Ship Arena", "=", profile.getShipRank());
        builder.addField(new MessageEmbed.Field("Arena", "```" + arena + "```", false));
        final String profileLink = String.format(TABLE_FORMAT, "SWGOH profile", "", "[Link](" + profile.getProfileUrl() + ")");
        builder.addField(new MessageEmbed.Field("SWGOH", profileLink, false));
        builder.addField(new MessageEmbed.Field("Updated:", profile.getLastUpdated().toString(), false));
        return builder.build();
    }

    /**
     * Generates a specific help text.
     *
     * @param name        the command.
     * @param description the description.
     * @param flags       the flags.
     * @return an embed.
     */
    public static MessageEmbed formatSpecificHelpText(final String name, final String description, final Map<String, Flag> flags, final String prefix) {
        final EmbedBuilder builder = new EmbedBuilder(baseEmbed());
        builder.appendDescription("Info for command: " + name);
        builder.addField("Description", description, false);
        if (!flags.isEmpty()) {
            for (final Map.Entry<String, Flag> entry : flags.entrySet()) {
                final String flagText = String.format(TABLE_FORMAT, "Description", "=", entry.getValue().getDescription()) +
                        String.format(TABLE_FORMAT, "Usage", "=", prefix + entry.getValue().getHelpText());
                builder.addField(entry.getValue().getName(), "```" + flagText + "```", false);
            }
        }
        return builder.build();
    }

    /**
     * Generates a config embed.
     *
     * @param config the config.
     * @return the embed.
     */
    public static MessageEmbed formatConfig(final Config config) {
        final EmbedBuilder builder = new EmbedBuilder(baseEmbed());
        builder.appendDescription("Bot configuration");
        builder.addField(new MessageEmbed.Field("SWGOH ID", config.getSwgohId(), false));
        builder.addField(new MessageEmbed.Field("Prefix", config.getPrefix(), false));
        builder.addField(new MessageEmbed.Field("ModRole", config.getModerationRole(), false));
        builder.addField(new MessageEmbed.Field("Presence Ignore role", config.getIgnoreRole(), false));
        builder.addField(new MessageEmbed.Field("BotNotifyChannel", config.getNotifyChannel(), false));
        builder.addField(new MessageEmbed.Field("BotLoggingChannel", config.getBotLoggingChannel(), false));
        return builder.build();
    }

    /**
     * Formats the relic information.
     *
     * @param units      the unit list.
     * @param relicLevel the relic level checked.
     * @param username   the username of the user.
     * @return a list with embeds.
     */
    public static List<MessageEmbed> formatProfileRelic(final Map<String, Integer> units, final int relicLevel, final String username) {
        final int maxCounter = 5;
        final List<MessageEmbed> returnValue = new ArrayList<>();
        EmbedBuilder builder = new EmbedBuilder(baseEmbed());
        builder.appendDescription("Relic'd toons below: **" + relicLevel + "** for: **" + username + "**");
        final StringBuilder helpString = new StringBuilder();
        int counter = 0;
        for (final Map.Entry<String, Integer> entry : units.entrySet()) {
            helpString.append(String.format(UNIT_TABLE_FORMAT, entry.getKey(), ":", entry.getValue()));
            counter++;
            if (counter == maxCounter) {
                if (helpString.isEmpty()) {
                    continue;
                }
                builder.addField(new MessageEmbed.Field(Integer.toString(builder.getFields().size() + 1), "```" + helpString + "```", false));
                helpString.setLength(0);
                counter = 0;
                if (builder.length() >= MessageEmbed.EMBED_MAX_LENGTH_BOT - 500) {
                    returnValue.add(builder.build());
                    builder = new EmbedBuilder(baseEmbed());
                }
            }
        }
        if (!helpString.isEmpty()) {
            builder.addField(new MessageEmbed.Field(Integer.toString(builder.getFields().size()), "```" + helpString + "```", false));
        }
        returnValue.add(builder.build());
        return returnValue;
    }

    /**
     * Formats the guildGPMessage.
     *
     * @param players the player GP info.
     * @return a message embed.
     */
    public static MessageEmbed formatGuildGPOverview(final Map<String, Integer> players) {
        final EmbedBuilder embed = new EmbedBuilder(baseEmbed());
        embed.setDescription("GP Overview, sorted high -> low");
        final StringBuilder builder = new StringBuilder();
        for (final Map.Entry<String, Integer> entry : players.entrySet()) {
            builder.append(String.format(TABLE_FORMAT, entry.getKey(), ":", StringHelper.formatNumber(entry.getValue())));
            if (builder.length() > MessageEmbed.VALUE_MAX_LENGTH - 100) {
                embed.addField(Integer.toString(embed.getFields().size() + 1), "```" + builder + "```", false);
                builder.setLength(0);
            }
        }
        embed.addField(Integer.toString(embed.getFields().size() + 1), "```" + builder + "```", false);
        return embed.build();
    }

    /**
     * Formats the guildRelicMessage.
     *
     * @param players the player GP info.
     * @return a message embed.
     */
    public static MessageEmbed formatGuildRelicOverview(final Map<String, Integer> players, final int relicLevel) {
        final EmbedBuilder embed = new EmbedBuilder(baseEmbed());
        embed.setDescription("Relic Overview higher or at: '" + relicLevel + "', sorted high -> low");
        final StringBuilder builder = new StringBuilder();
        for (final Map.Entry<String, Integer> entry : players.entrySet()) {
            builder.append(String.format(TABLE_FORMAT, entry.getKey(), ":", entry.getValue()));
            if (builder.length() > MessageEmbed.VALUE_MAX_LENGTH - 100) {
                embed.addField(Integer.toString(embed.getFields().size() + 1), "```" + builder + "```", false);
                builder.setLength(0);
            }
        }
        embed.addField(Integer.toString(embed.getFields().size() + 1), "```" + builder + "```", false);
        return embed.build();
    }

    /**
     * Formats the guild profiles in an embed.
     *
     * @param playerGuild the guild of the player.
     * @param rivalGuild  the guild of the rival.
     * @return an list with embeds.
     */
    public static List<MessageEmbed> formatGuildCompare(final GuildCompare playerGuild, final GuildCompare rivalGuild) {
        final List<MessageEmbed> returnValue = new ArrayList<>();
        EmbedBuilder embed = new EmbedBuilder(baseEmbed());
        embed.setDescription("Guild Compare");
        final String profileString = String.format(GUILD_TABLE_FORMAT, "name:", playerGuild.getGuild().getName(), ":", rivalGuild.getGuild().getName()) +
                String.format(GUILD_TABLE_FORMAT, "GP", StringHelper.formatNumber(playerGuild.getGuild().getGalacticPower()), ":", StringHelper.formatNumber(rivalGuild.getGuild().getGalacticPower())) +
                String.format(GUILD_TABLE_FORMAT, "Members", playerGuild.getGuild().getMembers(), ":", rivalGuild.getGuild().getMembers()) +
                String.format(GUILD_TABLE_FORMAT, "Zetas", playerGuild.getZetas(), ":", rivalGuild.getZetas());
        embed.addField("Profile", "```" + profileString + "```", false);
        final String gearString = String.format(GUILD_TABLE_FORMAT, "G13", playerGuild.getG13(), ":", rivalGuild.getG13()) +
                String.format(GUILD_TABLE_FORMAT, "G12", playerGuild.getG12(), ":", rivalGuild.getG12());
        embed.addField("Gear", "```" + gearString + "```", false);
        returnValue.add(embed.build());
        embed = new EmbedBuilder(baseEmbed());
        for (final Map.Entry<String, String> entry : SwgohConstants.COMPARE_TOONS.entrySet()) {
            final GuildCompare.UnitProfile playerUnitProfile = playerGuild.getUnits().get(entry.getKey());
            final GuildCompare.UnitProfile rivalUnitProfile = rivalGuild.getUnits().get(entry.getKey());
            final String unitString = String.format(GUILD_TABLE_FORMAT, "7 Stars", playerUnitProfile.getSevenStars(), ":", rivalUnitProfile.getSevenStars()) +
                    String.format(GUILD_TABLE_FORMAT, "6 Stars", playerUnitProfile.getSixStars(), ":", rivalUnitProfile.getSixStars()) +
                    String.format(GUILD_TABLE_FORMAT, "G13", playerUnitProfile.getG13(), ":", rivalUnitProfile.getG13()) +
                    String.format(GUILD_TABLE_FORMAT, "G12", playerUnitProfile.getG12(), ":", rivalUnitProfile.getG12()) +
                    String.format(GUILD_TABLE_FORMAT, "Zetas", playerUnitProfile.getZetas(), ":", rivalUnitProfile.getZetas()) +
                    String.format(GUILD_TABLE_FORMAT, "Relic 5+", playerUnitProfile.getRelic5plus(), ":", rivalUnitProfile.getRelic5plus());
            embed.addField(entry.getValue(), "```" + unitString + "```", false);
            if (embed.length() >= MessageEmbed.EMBED_MAX_LENGTH_BOT - 500) {
                returnValue.add(embed.build());
                embed = new EmbedBuilder(baseEmbed());
            }
        }
        returnValue.add(embed.build());
        return returnValue;
    }

    /**
     * Formats the profileCompare message.
     *
     * @param compareProfiles the compareProfiles.
     * @return a list of embeds.
     */
    public static List<MessageEmbed> formatProfileCompare(final ProfileCompare... compareProfiles) {
        EmbedBuilder embed = new EmbedBuilder(baseEmbed());
        final List<MessageEmbed> returnValue = new ArrayList<>();
        embed.setDescription("Profile comparison");
        final String profileString = String.format(PROFILE_TABLE_FORMAT, "name:", compareProfiles[0].getName(), ":", compareProfiles[1].getName()) +
                String.format(PROFILE_TABLE_FORMAT, "guild:", compareProfiles[0].getGuild(), ":", compareProfiles[1].getGuild()) +
                String.format(PROFILE_TABLE_FORMAT, "GP:", StringHelper.formatNumber(compareProfiles[0].getGalacticPower()), ":", StringHelper.formatNumber(compareProfiles[1].getGalacticPower())) +
                String.format(PROFILE_TABLE_FORMAT, "ToonGP:", StringHelper.formatNumber(compareProfiles[0].getToonGp()), ":", StringHelper.formatNumber(compareProfiles[1].getToonGp())) +
                String.format(PROFILE_TABLE_FORMAT, "ShipGP:", StringHelper.formatNumber(compareProfiles[0].getShipGp()), ":", StringHelper.formatNumber(compareProfiles[1].getShipGp()));
        embed.addField("Profile", "```" + profileString + "```", false);
        final String rosterString = String.format(PROFILE_TABLE_FORMAT, "Zetas:", compareProfiles[0].getZetas(), ":", compareProfiles[1].getZetas()) +
                String.format(PROFILE_TABLE_FORMAT, "G13:", compareProfiles[0].getG13(), ":", compareProfiles[1].getG13()) +
                String.format(PROFILE_TABLE_FORMAT, "G13:", compareProfiles[0].getG12(), ":", compareProfiles[1].getG12());
        embed.addField("Roster", "```" + rosterString + "```", false);
        final String relicString = String.format(PROFILE_TABLE_FORMAT, "tier0", compareProfiles[0].getRelics().get(0), ":", compareProfiles[1].getRelics().get(0)) +
                String.format(PROFILE_TABLE_FORMAT, "tier1", compareProfiles[0].getRelics().get(1), ":", compareProfiles[1].getRelics().get(1)) +
                String.format(PROFILE_TABLE_FORMAT, "tier2", compareProfiles[0].getRelics().get(2), ":", compareProfiles[1].getRelics().get(2)) +
                String.format(PROFILE_TABLE_FORMAT, "tier3", compareProfiles[0].getRelics().get(3), ":", compareProfiles[1].getRelics().get(3)) +
                String.format(PROFILE_TABLE_FORMAT, "tier4", compareProfiles[0].getRelics().get(4), ":", compareProfiles[1].getRelics().get(4)) +
                String.format(PROFILE_TABLE_FORMAT, "tier5", compareProfiles[0].getRelics().get(5), ":", compareProfiles[1].getRelics().get(5)) +
                String.format(PROFILE_TABLE_FORMAT, "tier6", compareProfiles[0].getRelics().get(6), ":", compareProfiles[1].getRelics().get(6)) +
                String.format(PROFILE_TABLE_FORMAT, "tier7", compareProfiles[0].getRelics().get(7), ":", compareProfiles[1].getRelics().get(7)) +
                String.format(PROFILE_TABLE_FORMAT, "tier8", compareProfiles[0].getRelics().get(8), ":", compareProfiles[1].getRelics().get(8));
        embed.addField("Relics", "```" + relicString + "```", false);
        returnValue.add(embed.build());
        embed = new EmbedBuilder(baseEmbed());
        for (final Map.Entry<String, String> entry : SwgohConstants.COMPARE_TOONS.entrySet()) {
            UnitCompare player = compareProfiles[0].getUnits().get(entry.getKey());
            if (player == null) {
                player = new UnitCompare();
            }
            UnitCompare rival = compareProfiles[1].getUnits().get(entry.getKey());
            if (rival == null) {
                rival = new UnitCompare();
            }
            final String unitString = String.format(PROFILE_TABLE_FORMAT, "GP", player.getGalacticPower(), ":", rival.getGalacticPower()) +
                    String.format(PROFILE_TABLE_FORMAT, "Stars", player.getRarity(), ":", rival.getRarity()) +
                    String.format(PROFILE_TABLE_FORMAT, "Gear", player.getGear(), ":", rival.getGear()) +
                    String.format(PROFILE_TABLE_FORMAT, "Relic", player.getRelic(), ":", rival.getRelic()) +
                    String.format(PROFILE_TABLE_FORMAT, "Zetas", player.getZetas(), ":", rival.getZetas()) +
                    String.format(PROFILE_TABLE_FORMAT, "Speed", player.getSpeed(), ":", rival.getSpeed());
            embed.addField(entry.getValue(), "```" + unitString + "```", false);
        }
        returnValue.add(embed.build());
        return returnValue;
    }

    /**
     * Creates the changelog message.
     *
     * @param changelog the changelog.
     * @param version   the version.
     * @return an embed.
     */
    public static MessageEmbed formatChangelog(final double version, final Map<String, JSONArray> changelog) {
        final EmbedBuilder embed = new EmbedBuilder(baseEmbed());
        embed.setDescription("Changelog for version: **__" + version + "__**");
        for (final Map.Entry<String, JSONArray> entry : changelog.entrySet()) {
            final StringBuilder changeString = new StringBuilder();
            for (int i = 0; i < entry.getValue().length(); i++) {
                changeString.append(entry.getValue().get(i)).append('\n');
            }
            embed.addField(entry.getKey(), "```" + changeString + "```", false);
        }
        return embed.build();
    }

    /**
     * Formats a message for the playerGLStatus.
     *
     * @param playerGlStatus the playerGLStatus.
     * @return a message embed.
     */
    public static MessageEmbed formatPlayerGLStatus(final PlayerGLStatus playerGlStatus) {
        final EmbedBuilder embed = new EmbedBuilder(baseEmbed());
        embed.setDescription("GL Status for: " + playerGlStatus.getGlEvent() + "\nTotal Completion: **" + new DecimalFormat("##.##%").format(playerGlStatus.getTotalCompleteness()) + "**");
        final StringBuilder status = new StringBuilder(String.format(GL_OVERVIEW_FORMAT, "name", GEAR_ICON, RELIC_ICON, ZETA_ICON, "status"));
        for (final GLUnit compare : playerGlStatus.getUnits()) {
            status.append(String.format(GL_OVERVIEW_FORMAT, compare.getUnitName(), compare.getGearLevel(), compare.getRelicLevel(), compare.getZetas(), new DecimalFormat("##.##%").format(compare.getCompleteness())));
        }
        embed.addField("status", "```" + status + "```", false);
        return embed.build();
    }

    /**
     * Formats a message for the Guild GL Status.
     *
     * @param event        the event name.
     * @param playerStatus the player statussen.
     * @return a message embed.
     */
    public static MessageEmbed formatGuildGLStatus(final String event, final Map<String, PlayerGLStatus> playerStatus) {
        final EmbedBuilder embed = new EmbedBuilder(baseEmbed());
        final int maxCounter = 5;
        embed.setDescription("Guild GL Status for: " + event);
        int counter = 0;
        StringBuilder status = new StringBuilder(String.format(TABLE_FORMAT, "Name", "", "Status"));
        for (final Map.Entry<String, PlayerGLStatus> entry : playerStatus.entrySet()) {
            counter++;
            status.append(String.format(TABLE_FORMAT, entry.getKey(), ":", new DecimalFormat("##.##%").format(entry.getValue().getTotalCompleteness())));
            if (counter == maxCounter) {
                embed.addField(Integer.toString(embed.getFields().size()), "```" + status + "```", false);
                counter = 0;
                status = new StringBuilder(String.format(TABLE_FORMAT, "Name", "", "Status"));
            }
        }
        return embed.build();
    }

    /**
     * Creates an embed for a github issue status.
     * @param githubIssueStatus the gitHub Issue status.
     * @return a messageEmbed.
     */
    public static MessageEmbed formatIssueOverview(final GithubIssueStatus githubIssueStatus) {
        final EmbedBuilder embed = new EmbedBuilder(baseEmbed());
        embed.setDescription("Issue overview **" + githubIssueStatus.getIssueId() + "**");
        embed.addField("Issue Status", "```" + githubIssueStatus.getState() + "```", false);
        embed.addField("Assigned to", "```" + githubIssueStatus.getAssignee() + "```", false);
        embed.addField("Issue Title", "```" + githubIssueStatus.getTitle() + "```", false);
        embed.addField("Issue Description", "```" + githubIssueStatus.getBody() + "```", false);
        embed.addField("Issue Labels", "```" + githubIssueStatus.getLabels() + "```", false);
        embed.addField("Last comment", "```" + githubIssueStatus.getLastComment() + "```", false);
        embed.addField("Issue URL", "[Link to Github](" + githubIssueStatus.getUrl() + ")", false);
        return embed.build();
    }

//CHECKSTYLE.ON: MultipleStringLiteralsCheck
}

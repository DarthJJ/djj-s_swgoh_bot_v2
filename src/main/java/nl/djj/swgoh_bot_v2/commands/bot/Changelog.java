package nl.djj.swgoh_bot_v2.commands.bot;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import nl.djj.swgoh_bot_v2.helpers.MessageHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DJJ
 */
public class Changelog extends BaseCommand {

    /**
     * Constructor.
     * @param logger the logger.
     * @param implHelper the implHelper.
     */
    public Changelog(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "changelog";
        requiredLevel = Permission.USER;
        description = "Gets the latest changelog for the bot.";
        aliases = new String[]{
                "cl"
        };
        category = CommandCategory.BOT;
        flagRequired = false;
    }

    @Override
    public void createFlags() {
        //Not needed since this command has no flag.
    }

    @Override
    public void handleMessage(final Message message) {
        final JSONObject changelogJson;
        try {
            changelogJson = new JSONObject(new JSONTokener(Files.newInputStream(Paths.get("changelog.json"))));
            final Map<String, JSONArray> changelog = new ConcurrentHashMap<>();
            changelog.put("Added", changelogJson.getJSONArray("add"));
            changelog.put("Changed", changelogJson.getJSONArray("change"));
            changelog.put("Fixed", changelogJson.getJSONArray("fix"));
            changelog.put("Removed", changelogJson.getJSONArray("remove"));
            message.done(MessageHelper.formatChangelog(changelogJson.getDouble("version"), changelog));
        } catch (final IOException error) {
            message.error(error.getMessage());
        }
    }
}

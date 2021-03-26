package nl.djj.swgoh_bot_v2.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author DJJ
 */
public class HttpHelper {
    private final transient Logger logger;
    private final transient String className = this.getClass().getSimpleName();

    /**
     * Constructor.
     *
     * @param logger the logger to use.
     */
    public HttpHelper(final Logger logger) {
        super();
        this.logger = logger;
    }

    private String readAll(final Reader reader) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * Download a JSON array from the given URL.
     * @param url the url.
     * @return JSON data.
     */
    public JSONArray getJsonArray(final String url) {
        try (InputStream is = new URL(url).openStream()) {
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            final String jsonText = readAll(rd);
            return new JSONArray(jsonText);
        } catch (final IOException exception) {
            logger.error(className, exception.getMessage());
            return null;
        }
    }

    /**
     * Download a JSON Object from the given URL.
     * @param url the url.
     * @return JSON data.
     */
    public JSONObject getJsonObject(final String url) {
        try (InputStream is = new URL(url).openStream()) {
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            final String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } catch (final IOException exception) {
            logger.error(className, exception.getMessage());
            return null;
        }
    }
}

package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
        final StringBuilder builder = new StringBuilder();
        int line;
        while ((line = reader.read()) != -1) {
            builder.append((char) line);
        }
        return builder.toString();
    }

    /**
     * Download a JSON array from the given URL.
     *
     * @param url the url.
     * @return JSON data.
     */
    public JSONArray getJsonArray(final String url) throws HttpRetrieveError {
        try (InputStream stream = new URL(url).openStream()) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            final String jsonText = readAll(reader);
            return new JSONArray(jsonText);
        } catch (final IOException exception) {
            throw new HttpRetrieveError(className, exception.getMessage(), logger);
        }
    }

    /**
     * Download a JSON Object from the given URL.
     *
     * @param url the url.
     * @return JSON data.
     */
    public JSONObject getJsonObject(final String url) throws HttpRetrieveError {
        try (InputStream stream = new URL(url).openStream()) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            final String jsonText = readAll(reader);
            return new JSONObject(jsonText);
        } catch (final IOException exception) {
            throw new HttpRetrieveError(className, exception.getMessage(), logger);
        }
    }

    /**
     * Get's CSV data.
     * @param url the url to download from.
     * @return CSV data.
     * @throws HttpRetrieveError when something goes wrong.
     */
    public List<String> getCsv(final String url) throws HttpRetrieveError {
        try {
            final URL urlCSV = new URL(url);
            final List<String> returnValue = new ArrayList<>();
            final URLConnection urlConn = urlCSV.openConnection();
            final InputStreamReader inputCSV = new InputStreamReader(
                    urlConn.getInputStream());
            final BufferedReader reader = new BufferedReader(inputCSV);
            String line;
            while ((line = reader.readLine()) != null) {
                returnValue.add(line);
            }
            return returnValue;
        } catch (final IOException exception) {
            throw new HttpRetrieveError(className, exception.getMessage(), logger);
        }
    }
}

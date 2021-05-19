package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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

    private JSONObject stringToJsonObject(final Reader reader) throws IOException {
        logger.debug(className, "Reading JSON data into an object");
        return new JSONObject(new JSONTokener(reader));
    }

    private JSONArray stringToJSonArray(final Reader reader) throws IOException {
        logger.debug(className, "Reading JSON data into an array");
        return new JSONArray(new JSONTokener(reader));
    }

    /**
     * Download a JSON array from the given URL.
     *
     * @param url the url.
     * @return JSON data.
     */
    public JSONArray getJsonArray(final String url) throws HttpRetrieveError {
        logger.debug(className, "Retrieving from: " + url);
        try {
            final URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", "swgohBot");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            final int timeout = 100_000;
            connection.setReadTimeout(timeout);
            connection.setConnectTimeout(timeout);
            connection.connect();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            return stringToJSonArray(reader);
        } catch (final IOException exception) {
            throw new HttpRetrieveError(className, "getJsonArray", exception);
        }
    }

    /**
     * Download a JSON Object from the given URL.
     *
     * @param url the url.
     * @return JSON data.
     */
    public JSONObject getJsonObject(final String url) throws HttpRetrieveError {
        logger.debug(className, "Retrieving from: " + url);
        try {
            final URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", "swgohBot");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            final int timeout = 100_000;
            connection.setReadTimeout(timeout);
            connection.setConnectTimeout(timeout);
            connection.connect();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            return stringToJsonObject(reader);
        } catch (final IOException exception) {
            throw new HttpRetrieveError(className, "getJsonObject", exception);
        }
    }

    /**
     * Get's CSV data.
     *
     * @param url the url to download from.
     * @return CSV data.
     * @throws HttpRetrieveError when something goes wrong.
     */
    public List<String> getCsv(final String url) throws HttpRetrieveError {
        logger.debug(className, "Retrieving from: " + url);
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
            throw new HttpRetrieveError(className, "getCsv", exception);
        }
    }
}

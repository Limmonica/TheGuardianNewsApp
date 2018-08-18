package com.example.limmonica.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class with helper methods related to performing the HTTP request and parsing the article data from The Guardian.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns a new URL Object from a given string URL
     */
    private static URL createUrl(String stringUrl) {

        // Initialize the url to null
        URL url = null;

        // Try to update the variable and if not successful catch the exception
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }

        // Return the URL Object
        return url;
    }

    /**
     * Make a HTTP request to the given URL and return a String as the response
     */
    private static String makeHttpRequest(URL url) throws IOException {

        // Initialize the response
        String jsonResponse = "";

        // If the Url is null, then return early
        if (url == null) {
            return jsonResponse;
        }

        // Initialize the url connection
        HttpURLConnection urlConnection = null;

        // Initialize the input stream
        InputStream inputStream = null;

        // Try to establish a connection
        try {
            // Open the connection
            urlConnection = (HttpURLConnection) url.openConnection();

            // Set a read timeout to 10 seconds
            urlConnection.setReadTimeout(10000);

            // Set a timeout for the connection to 15 seconds
            urlConnection.setConnectTimeout(15000);

            // Set the request method, for when connected to the url, to get/read the data
            urlConnection.setRequestMethod("GET");

            // Connect tot the url
            urlConnection.connect();

            // If the connection was successful then read the input stream and parse the response
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Read the input stream
                inputStream = urlConnection.getInputStream();

                // Parse the response from input stream and store it into a String
                jsonResponse = readFromStream(inputStream);

            } else {

                // Otherwise log the error message and the connection response code
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            // Catch the IOException error if there are problems with connecting
            Log.e(LOG_TAG, "Problem retrieving the articles JSON results.", e);

        } finally {

            // If the connection is not null
            if (urlConnection != null) {

                // Disconnect
                urlConnection.disconnect();
            }

            // If there was an input stream, closing it could throw a IOException which is why it
            // is specified in the method signature
            if (inputStream != null) {

                // Close it
                inputStream.close();

            }
        }

        // Return the response from reading the JSON data in a String
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from
     * the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        // Create a new String builder object
        StringBuilder output = new StringBuilder();

        // If the input stream is not null
        if (inputStream != null) {

            // Create a new input stream reader object based on the input stream
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            // Create a new buffered reader object from the input stream reader
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Read and store into the buffer the first line
            String line = reader.readLine();

            // While the line is not null
            while (line != null) {
                // Append it to the output string builder
                output.append(line);
                // Read and store into the buffer the next line
                line = reader.readLine();
            }
        }

        // Return the output
        return output.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Article> extractResponseFromJson(String articleJSON) {

        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles to
        List<Article> articles = new ArrayList<>();

        // Try to parse the data read from The Guardian. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        try {

            // Parse the response and build up a list of Article objects with the corresponding data:
            // Create a JSONObject from the data received
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            // Extract "response" JSONObject
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results", which represents a
            // list of results (or articles).
            JSONArray articleArray = responseObject.getJSONArray("results");

            // For each article in the articleArray, create an {@link Article} object
            for (int i = 0; i < articleArray.length(); i++) {

                // Get a single article JSONObject at position i within the list of articles
                JSONObject currentArticle = articleArray.getJSONObject(i);

                // Extract the value for the key called "webPublicationDate"
                String date = currentArticle.getString("webPublicationDate");

                // Extract the value for the key called "sectionName"
                String section = currentArticle.getString("sectionName");

                // Extract the value for the key called "webUrl"
                String url = currentArticle.getString("webUrl");

                // For a given article, extract the JSONObject associated with the key called
                // "fields", which represents a list of additional fields for that article
                JSONObject fields = currentArticle.getJSONObject("fields");

                // Extract the value for the key called "headline"
                String title = fields.getString("headline");

                // Extract the value for the key called "byline"
                String author = fields.getString("byline");

                // Extract the value for the key called "trailText"
                String trail = fields.getString("trailText");

                String thumbnail = null;
                if (fields.has("thumbnail")) {
                    // Extract the value for the key called "thumbnail"
                    thumbnail = fields.getString("thumbnail");
                }

                // Create Article java object from: date, section, title, author, trail, and url
                Article article = new Article(date, section, thumbnail, title, author, trail, url);

                // Add article to list of articles
                articles.add(article);
            }

        } catch (JSONException e) {

            // If an error is thrown when executing any of the above statements in the "try" block
            // print a log message with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }

        // Return the list of articles
        return articles;
    }

    /**
     * Query the USGS dataset and return a list of {@link Article} objects.
     */
    public static List<Article> fetchArticleData(String requestUrl) {

        // Create a URL Object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {

            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Article}s.
        List<Article> articles = extractResponseFromJson(jsonResponse);

        // Return the list of {@link Article}s
        return articles;
    }
}

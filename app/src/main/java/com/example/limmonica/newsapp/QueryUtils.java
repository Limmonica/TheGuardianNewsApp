package com.example.limmonica.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving articles data from The Guardian.
 */
public class QueryUtils {

    /**
     * Sample JSON response for a The Guardian query
     */
    private static final String SAMPLE_JSON_RESPONSE = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":133,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":14,\"orderBy\":\"relevance\",\"results\":[{\"id\":\"technology/2018/aug/02/macs-ipods-apps-how-apple-revolutionised-technology\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-08-02T16:03:49Z\",\"webTitle\":\"From Macs to iPods and apps: how Apple revolutionised technology\",\"webUrl\":\"https://www.theguardian.com/technology/2018/aug/02/macs-ipods-apps-how-apple-revolutionised-technology\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/aug/02/macs-ipods-apps-how-apple-revolutionised-technology\",\"fields\":{\"trailText\":\"Over 42 years, the company has created an ‘app economy’ and placed itself at the centre of it\",\"byline\":\"Alex Hern\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"artanddesign/2018/aug/10/natsiaa-2018-young-guns-breathe-new-life-into-indigenous-art-traditions\",\"type\":\"article\",\"sectionId\":\"artanddesign\",\"sectionName\":\"Art and design\",\"webPublicationDate\":\"2018-08-10T11:00:37Z\",\"webTitle\":\"Natsiaa 2018: young guns breathe new life into Indigenous art traditions\",\"webUrl\":\"https://www.theguardian.com/artanddesign/2018/aug/10/natsiaa-2018-young-guns-breathe-new-life-into-indigenous-art-traditions\",\"apiUrl\":\"https://content.guardianapis.com/artanddesign/2018/aug/10/natsiaa-2018-young-guns-breathe-new-life-into-indigenous-art-traditions\",\"fields\":{\"trailText\":\"This year’s awards in Darwin merge the old and the new with modern materials and technology \",\"byline\":\"Helen Davidson\"},\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"technology/2018/aug/07/indian-ride-hailing-firm-ola-to-take-on-uber-with-launch-in-uk\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-08-07T11:11:44Z\",\"webTitle\":\"Indian ride-hailing firm Ola to take on Uber with launch in UK\",\"webUrl\":\"https://www.theguardian.com/technology/2018/aug/07/indian-ride-hailing-firm-ola-to-take-on-uber-with-launch-in-uk\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/aug/07/indian-ride-hailing-firm-ola-to-take-on-uber-with-launch-in-uk\",\"fields\":{\"trailText\":\"Company first in Britain to offer choice of private hire car or black cab through same app\",\"byline\":\"Julia Kollewe\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2018/aug/02/amazon-halved-uk-corporation-tax-bill-to-45m-last-year\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-08-03T09:05:46Z\",\"webTitle\":\"Amazon halved corporation tax bill despite UK profits tripling\",\"webUrl\":\"https://www.theguardian.com/technology/2018/aug/02/amazon-halved-uk-corporation-tax-bill-to-45m-last-year\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/aug/02/amazon-halved-uk-corporation-tax-bill-to-45m-last-year\",\"fields\":{\"trailText\":\"Revelation comes after US company posts record $2.5bn profit in most recent quarter\",\"byline\":\"Mark Sweney\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"australia-news/2018/aug/09/crown-resorts-reveals-profits-gained-from-pokies-for-first-time\",\"type\":\"article\",\"sectionId\":\"australia-news\",\"sectionName\":\"Australia news\",\"webPublicationDate\":\"2018-08-09T04:50:51Z\",\"webTitle\":\"Crown windfall driven by $450m in losses from Victorian pokies players\",\"webUrl\":\"https://www.theguardian.com/australia-news/2018/aug/09/crown-resorts-reveals-profits-gained-from-pokies-for-first-time\",\"apiUrl\":\"https://content.guardianapis.com/australia-news/2018/aug/09/crown-resorts-reveals-profits-gained-from-pokies-for-first-time\",\"fields\":{\"trailText\":\"Anti-gambling advocates say profits demonstrate company’s need to expand technology so that problem gamblers can be turned away \",\"byline\":\"Melissa Davey and agencies\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"business/2018/aug/06/more-than-6m-workers-fear-being-replaced-by-machines-report\",\"type\":\"article\",\"sectionId\":\"business\",\"sectionName\":\"Business\",\"webPublicationDate\":\"2018-08-05T23:01:22Z\",\"webTitle\":\"More than 6m workers fear being replaced by machines – report\",\"webUrl\":\"https://www.theguardian.com/business/2018/aug/06/more-than-6m-workers-fear-being-replaced-by-machines-report\",\"apiUrl\":\"https://content.guardianapis.com/business/2018/aug/06/more-than-6m-workers-fear-being-replaced-by-machines-report\",\"fields\":{\"trailText\":\"Government and trade unions urged to do more for those at risk from new technologies \",\"byline\":\"Richard Partington Economics correspondent\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"commentisfree/2018/aug/05/magical-thinking-about-machine-learning-will-not-bring-artificial-intelligence-any-closer\",\"type\":\"article\",\"sectionId\":\"commentisfree\",\"sectionName\":\"Opinion\",\"webPublicationDate\":\"2018-08-05T06:00:03Z\",\"webTitle\":\"Magical thinking about machine learning won’t bring the reality of AI any closer | John Naughton\",\"webUrl\":\"https://www.theguardian.com/commentisfree/2018/aug/05/magical-thinking-about-machine-learning-will-not-bring-artificial-intelligence-any-closer\",\"apiUrl\":\"https://content.guardianapis.com/commentisfree/2018/aug/05/magical-thinking-about-machine-learning-will-not-bring-artificial-intelligence-any-closer\",\"fields\":{\"trailText\":\"Unchecked flaws in algorithms, and even the technology itself, should put a brake on the escalating use of big data\",\"byline\":\"John Naughton\"},\"isHosted\":false,\"pillarId\":\"pillar/opinion\",\"pillarName\":\"Opinion\"},{\"id\":\"media/2018/aug/02/fifth-of-britons-feel-stressed-if-they-cant-access-internet-ofcom-report\",\"type\":\"article\",\"sectionId\":\"media\",\"sectionName\":\"Media\",\"webPublicationDate\":\"2018-08-02T06:14:21Z\",\"webTitle\":\"Britons spend average of 24 hours a week online, Ofcom says\",\"webUrl\":\"https://www.theguardian.com/media/2018/aug/02/fifth-of-britons-feel-stressed-if-they-cant-access-internet-ofcom-report\",\"apiUrl\":\"https://content.guardianapis.com/media/2018/aug/02/fifth-of-britons-feel-stressed-if-they-cant-access-internet-ofcom-report\",\"fields\":{\"trailText\":\"Study reveals dramatic rise in addiction to technology, as average Briton checks a mobile phone every 12 minutes \",\"byline\":\"Jim Waterson Media editor\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"politics/2018/jul/31/uk-will-act-alone-against-tech-giant-tax-avoidance-if-global-solution-falters\",\"type\":\"article\",\"sectionId\":\"politics\",\"sectionName\":\"Politics\",\"webPublicationDate\":\"2018-08-01T10:47:03Z\",\"webTitle\":\"UK will act alone against tech firm tax avoidance if global solution falters\",\"webUrl\":\"https://www.theguardian.com/politics/2018/jul/31/uk-will-act-alone-against-tech-giant-tax-avoidance-if-global-solution-falters\",\"apiUrl\":\"https://content.guardianapis.com/politics/2018/jul/31/uk-will-act-alone-against-tech-giant-tax-avoidance-if-global-solution-falters\",\"fields\":{\"trailText\":\"Exclusive: Treasury minister Mel Stride suggests digital revenue levy on Google and Facebook\",\"byline\":\"Pippa Crerar\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"education/2018/aug/05/john-prest-obituary\",\"type\":\"article\",\"sectionId\":\"education\",\"sectionName\":\"Education\",\"webPublicationDate\":\"2018-08-05T14:20:26Z\",\"webTitle\":\"John Prest obituary\",\"webUrl\":\"https://www.theguardian.com/education/2018/aug/05/john-prest-obituary\",\"apiUrl\":\"https://content.guardianapis.com/education/2018/aug/05/john-prest-obituary\",\"fields\":{\"trailText\":\"Other lives:\",\"byline\":\"Jeremy Burchardt\"},\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"}]}}";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Article> extractArticles() {

        // Create an empty ArrayList that we can start adding articles to
        ArrayList<Article> articles = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the SAMPLE_JSON_RESPONSE string and build up a list of Article objects with the corresponding data.
            //        Convert SAMPLE_JSON_RESPONSE String into a JSONObject / Create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(SAMPLE_JSON_RESPONSE);

            //        Extract "response" JSONObject
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");

            //        Extract “results” JSONArray / Extract the JSONArray associated with the key called "results", which represents a list of results (or articles).
            JSONArray articleArray = responseObject.getJSONArray("results");

            //        Loop through each result in the array / For each article in the articleArray, create an {@link Article} object
            for (int i = 0; i < articleArray.length(); i++) {

                //        Get article JSONObject at position i
                JSONObject currentArticle = articleArray.getJSONObject(i);

                //        Extract "webPublicationDate" for date
                String date = currentArticle.getString("webPublicationDate");

                //        Extract "sectionName" for section
                String section = currentArticle.getString("sectionName");

                //        Get "fields" JSONObject
                JSONObject fields = currentArticle.getJSONObject("fields");

                //        Extract "webTitle" for title
                String title = currentArticle.getString("webTitle");

                //        Extract "byline" for author
                String author = fields.getString("byline");

                //        Extract "trailText" for trail
                String trail = fields.getString("trailText");

                //        Extract "webUrl" for url
                String url = currentArticle.getString("webUrl");

                //        14) Create Article java object from: date, section, title, author, trail, and url
                Article article = new Article(date, section, title, author, trail, url);

                //        15) Add article to list of articles
                articles.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }

        // Return the list of articles
        return articles;
    }
}

package com.example.limmonica.newsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * An {@link ArticleAdapter} knows how to create a list item layout for each article in the
 * data source (a list of {@link Article} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView to be displayed to the
 * user.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {

    private static final String AUTHOR_LABEL = "by ";
    private static final String SECTION_SEP = " / ";

    /**
     * Constructs a new {@link ArticleAdapter}.
     *
     * @param context  of the app
     * @param articles is the list of articles, which is the data source of the adapter
     */
    ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.article_list_item, parent, false);
        }

        // Find the article at the given position in the list of articles
        final Article currentArticle = getItem(position);

        // Get the section of the current article and store it into a String
        assert currentArticle != null;
        String sectionTitle = currentArticle.getArticleSection();

        // Find the View with view ID article_separator
        View articleSeparator = listItemView.findViewById(R.id.article_separator);
        // Set the color of the background to the specific color of the section
        articleSeparator.setBackgroundColor(getSectionColor(sectionTitle));

        // Get the date string of the current article
        String dateString = currentArticle.getArticleDate();
        // Assign to it a date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault());
        try {
            // Parse the date in the given String format
            Date mDate = dateFormat.parse(dateString);
            // Get the time in milliseconds from the date
            long timeInMilliseconds = mDate.getTime();
            // Create a new Date object from the time in milliseconds of the article
            Date dateObject = new Date(timeInMilliseconds);
            // Format the date string (i.e. "Apr 25, 2016")
            String formattedDate = formatDate(dateObject);
            // Find the TextView with view ID date_view
            TextView dateView = listItemView.findViewById(R.id.date_view);
            // Display the date of the current article in that TextView
            dateView.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Find the TextView with view ID section_view
        TextView sectionView = listItemView.findViewById(R.id.section_view);
        // Add a separator " / " to the section text
        String sectionLine = sectionTitle + SECTION_SEP;
        // Display the section of the current article in that TextView
        sectionView.setText(sectionLine);
        // Set the color of the section text to the specific color of the section
        sectionView.setTextColor(getSectionColor(sectionTitle));

        // Find the ImageView with view ID thumbnail_view
        ImageView thumbnailView = listItemView.findViewById(R.id.thumbnail_view);
        // If the current article has thumbnail image
        if (currentArticle.hasThumbnail()) {
            // Download and set the Thumbnail image on the Image View
            new DownloadImageTask(thumbnailView).execute(currentArticle.getThumbnailUrl());
            // Make sure the view is visible
            thumbnailView.setVisibility(View.VISIBLE);
        } else {
            // Make sure the view is not visible
            thumbnailView.setVisibility(View.GONE);
        }

        // Find the TextView with view ID title_view
        TextView titleView = listItemView.findViewById(R.id.title_view);
        // Display the title of the current article in that TextView
        titleView.setText(currentArticle.getArticleTitle());

        // Find the TextView with view ID author_view
        TextView authorView = listItemView.findViewById(R.id.author_view);
        // Get the String with the author name of the article
        String authorName = currentArticle.getArticleAuthor();
        // Check if an author is provided
        if (!TextUtils.isEmpty(authorName)) {
            // String variable to store the author line
            String authorLine;
            // Create the author line by adding "by " before the author name
            authorLine = AUTHOR_LABEL + authorName;
            // Display the author of the current article in that TextView
            authorView.setText(authorLine);
            // Set the color of the text to the specific color of the section
            authorView.setTextColor(getSectionColor(sectionTitle));
            // Make sure the view is visible
            authorView.setVisibility(View.VISIBLE);
        } else {
            // Otherwise hide the TextView
            authorView.setVisibility(View.GONE);
        }

        // Find the TextView with view ID trail_view
        TextView trailView = listItemView.findViewById(R.id.trail_view);
        // Strip the HTML from text and display the trail - a short preview - of the current
        // article in that TextView
        trailView.setText(Html.fromHtml(currentArticle.getArticleTrail()).toString().replaceAll("\n", "").trim());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        return dateFormat.format(dateObject);
    }

    /**
     * Returns the color of the background of the layout containing the section of the article
     *
     * @param sectionTitle is the title of the section
     * @return the color
     */
    private int getSectionColor(String sectionTitle) {

        int sectionColor;

        switch (sectionTitle) {
            case "News":
            case "World news":
            case "UK news":
            case "US news":
            case "Australia news":
            case "Technology":
            case "Science":
            case "Cities":
            case "Global development":
            case "Business":
            case "Society":
            case "Education":
            case "Environment":
            case "Politics":
                sectionColor = R.color.news;
                break;
            case "Opinion":
                sectionColor = R.color.opinion;
                break;
            case "Sport":
                sectionColor = R.color.sport;
                break;
            case "Culture":
            case "Film":
            case "Music":
            case "TV and radio":
            case "Books":
            case "Art and design":
            case "Stage":
            case "Games":
            case "Classical":
                sectionColor = R.color.culture;
                break;
            case "Lifestyle":
            case "Fashion":
            case "Food":
            case "Travel":
            case "Health and fitness":
            case "Women":
            case "Love and sex":
            case "Beauty":
            case "Home and garden":
            case "Money":
            case "Cars":
                sectionColor = R.color.lifestyle;
                break;
            default:
                sectionColor = R.color.other;
                break;
        }
        return ContextCompat.getColor(getContext(), sectionColor);
    }

    /**
     * {@link DownloadImageTask} is an {@link AsyncTask} used to access the thumbnail url, download
     * the data and decode the stream into a bitmap on a background thread and then update the
     * UI with the Bitmap image of the article
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        // Initialize the ImageView of the article
        ImageView articleThumbnail;

        /**
         * Constructs a new {@link DownloadImageTask} object.
         *
         * @param articleThumbnail is the ImageView of the article thumbnail which will be returned
         *                         onPostExecute() based on the result
         */
        DownloadImageTask(ImageView articleThumbnail) {
            this.articleThumbnail = articleThumbnail;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mImage = null;
            try {
                // Open the stream for the url of the thumbnail
                InputStream incoming = new URL(urlDisplay).openStream();
                // Decode the incoming stream and store it into a Bitmap
                mImage = BitmapFactory.decodeStream(incoming);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Return the Bitmap image
            return mImage;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the result(ed) Bitmap image in the article ImageView
            articleThumbnail.setImageBitmap(result);
        }
    }
}

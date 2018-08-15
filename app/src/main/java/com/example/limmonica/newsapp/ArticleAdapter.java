package com.example.limmonica.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private static final String AUTHOR_LABEL = "By ";

    /**
     * Constructs a new {@link ArticleAdapter}.
     *
     * @param context  of the app
     * @param articles is the list of articles, which is the data source of the adapter
     */
    public ArticleAdapter(Context context, List<Article> articles) {
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
        // Display the section of the current article in that TextView
        sectionView.setText(currentArticle.getArticleSection());

        // Find the Layout which contains the section of the article
        LinearLayout sectionViewLayout = listItemView.findViewById(R.id.section_layout);
        // Store the section of the article in a string
        String sectionTitle = currentArticle.getArticleSection();
        // Set the appropriate background color of the layout based on the section title
        sectionViewLayout.setBackgroundColor(getSectionColor(sectionTitle));

        // Find the TextView with view ID title_view
        TextView titleView = listItemView.findViewById(R.id.title_view);
        // Display the title of the current article in that TextView
        titleView.setText(currentArticle.getArticleTitle());

        // Get the String with the author name of the article
        String authorName = currentArticle.getArticleAuthor();
        // String variable to store the author line
        String authorLine;
        // Create the author line by adding "By " before the author name
        authorLine = AUTHOR_LABEL + authorName;
        // Find the TextView with view ID author_view
        TextView authorView = listItemView.findViewById(R.id.author_view);
        // Display the author of the current article in that TextView
        authorView.setText(authorLine);

        // Find the TextView with view ID trail_view
        TextView trailView = listItemView.findViewById(R.id.trail_view);
        // Display the trail of the current article in that TextView
        trailView.setText(currentArticle.getArticleTrail());

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
            case "Technology":
                sectionColor = R.color.technology;
                break;
            case "Business":
                sectionColor = R.color.business;
                break;
            case "Politics":
                sectionColor = R.color.politics;
                break;
            case "Australia news":
                sectionColor = R.color.australia_news;
                break;
            case "Media":
                sectionColor = R.color.media;
                break;
            case "Art and design":
                sectionColor = R.color.art_and_design;
                break;
            case "Education":
                sectionColor = R.color.education;
                break;
            case "Opinion":
                sectionColor = R.color.opinion;
                break;
            default:
                sectionColor = R.color.other;
                break;
        }
        return ContextCompat.getColor(getContext(), sectionColor);
    }
}

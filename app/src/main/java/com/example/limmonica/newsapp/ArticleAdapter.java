package com.example.limmonica.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * An {@link ArticleAdapter} knows how to create a list item layout for each article in the
 * data source (a list of {@link Article} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView to be displayed to the
 * user.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {

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

        // Find the TextView with view ID date_view
        TextView dateView = listItemView.findViewById(R.id.date_view);
        // Display the date of the current article in that TextView
        dateView.setText(currentArticle.getArticleDate());

        // Find the TextView with view ID section_view
        TextView sectionView = listItemView.findViewById(R.id.section_view);
        // Display the section of the current article in that TextView
        sectionView.setText(currentArticle.getArticleSection());

        // Find the ImageView with view ID thumbnail_view
        ImageView thumbnailView = listItemView.findViewById(R.id.thumbnail_view);
        // Display the thumbnail of the current article in that ImageView
        thumbnailView.setImageResource(currentArticle.getArticleThumbnail());

        // Find the TextView with view ID title_view
        TextView titleView = listItemView.findViewById(R.id.title_view);
        // Display the title of the current article in that TextView
        titleView.setText(currentArticle.getArticleTitle());

        // Find the TextView with view ID author_view
        TextView authorView = listItemView.findViewById(R.id.author_view);
        // Display the author of the current article in that TextView
        authorView.setText(currentArticle.getArticleAuthor());

        // Find the TextView with view ID trail_view
        TextView trailView = listItemView.findViewById(R.id.trail_view);
        // Display the trail of the current article in that TextView
        trailView.setText(currentArticle.getArticleTrail());

        // Find the Button with view ID read_more_view
        Button readView = listItemView.findViewById(R.id.read_more_view);
        // Set up a click listener
        readView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getArticleUrl());
                // Create a new intent to view the earthquake URI
                Intent readMoreIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                // Start activity
                getContext().startActivity(readMoreIntent);
            }
        });

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}

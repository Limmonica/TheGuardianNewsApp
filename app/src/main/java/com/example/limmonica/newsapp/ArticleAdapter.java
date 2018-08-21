package com.example.limmonica.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An {@link ArticleAdapter} knows how to create a list item layout for each article in the
 * data source (a list of {@link Article} objects).
 * <p>
 * These list item layouts will be provided to a {@link RecyclerView} to be displayed to the
 * user.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    // Tag for the log messages
    private static final String LOG_TAG = ArticleAdapter.class.getSimpleName();
    // Constant value String for the Author name
    private static final String AUTHOR_LABEL = "by ";
    // Constant value String for the Section title
    private static final String SECTION_SEP = " / ";
    // Initialization of the list of articles
    private List<Article> mArticles;
    // Initialization of the layout inflater
    private LayoutInflater mInflater;
    // Initialization of the context
    private Context mContext;

    /**
     * Creates a new {@link ArticleAdapter}
     *
     * @param context  is the context
     * @param articles is the data passed into the constructor
     */
    ArticleAdapter(Context context, List<Article> articles) {
        this.mInflater = LayoutInflater.from(context);
        this.mArticles = articles;
        this.mContext = context;
    }

    /**
     * Called when {@link RecyclerView} needs a new {@link ArticleViewHolder} of the given
     * type to represent an item. Constructed with a new View inflated from an XML layout file
     * that can represent the items of the given type.
     *
     * @param parent   is the ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType is the view type of the new View
     * @return a new ViewHolder that holds a View of the given view type
     */
    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the article_list_item layout file
        View itemView = mInflater.inflate(R.layout.article_list_item, parent, false);
        // Returns a new ViewHolder of the inflated xml layout file
        return new ArticleViewHolder(itemView);
    }

    /**
     * Called by {@link RecyclerView} to display the data at the specified position. This method
     * updates the contents of the itemView to reflect the item at the given position.
     *
     * @param holder   is the {@link ArticleViewHolder} which should be updated to represent
     *                 the contents of the item at the given position in the data set
     * @param position is he position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        // Find the article at the given position in the list of articles
        Article currentArticle = mArticles.get(position);

        // Get the section of the current article and store it into a String
        String sectionTitle = currentArticle.getArticleSection();

        // Set the color of the View background to the specific color of the section
        holder.separatorView.setBackgroundColor(getSectionColor(sectionTitle));

        // Add a separator " / " to the section text
        String sectionLine = sectionTitle + SECTION_SEP;
        // Display the section of the current article in that TextView
        holder.sectionView.setText(sectionLine);
        // Set the color of the section text to the specific color of the section
        holder.sectionView.setTextColor(getSectionColor(sectionTitle));

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
            // Display the date of the current article in that TextView
            holder.dateView.setText(formattedDate);
        } catch (ParseException e) {
            Log.v(LOG_TAG, "Problems with parsing the date", e);
        }

        // If the current article has thumbnail image
        if (currentArticle.hasThumbnail()) {
            // Load and set the Thumbnail image on the Image View
            Picasso.get().load(currentArticle.getThumbnailUrl()).into(holder.thumbnailView);
            // Make sure the view is visible
            holder.thumbnailView.setVisibility(View.VISIBLE);
        } else {
            // Make sure the view is not visible
            holder.thumbnailView.setVisibility(View.GONE);
        }

        // Display the title of the current article in that TextView
        holder.titleView.setText(currentArticle.getArticleTitle());

        // Get the String with the author name of the article
        String authorName = currentArticle.getArticleAuthor();
        // Check if an author is provided
        if (!TextUtils.isEmpty(authorName)) {
            // String variable to store the author line
            String authorLine;
            // Create the author line by adding "by " before the author name
            authorLine = AUTHOR_LABEL + authorName;
            // Display the author of the current article in that TextView
            holder.authorView.setText(authorLine);
            // Set the color of the text to the specific color of the section
            holder.authorView.setTextColor(getSectionColor((sectionTitle)));
            // Make sure the view is visible
            holder.authorView.setVisibility(View.VISIBLE);
        } else {
            // Otherwise hide the TextView
            holder.authorView.setVisibility(View.GONE);
        }

        // Strip the HTML from text and display the trail - a short preview - of the current
        // article in that TextView
        holder.trailView.setText(Html.fromHtml(currentArticle
                .getArticleTrail()
                .replaceAll("\n", "")
                .trim()));
    }

    /**
     * @return the total number of items in the data set held by the adapter
     */
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    /**
     * Updates the {@link List<Article>}
     *
     * @param articles is the list of articles to be added to the data set held by the adapter
     */
    public void addAll(List<Article> articles) {
        mArticles = articles;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        // Create a new pattern of date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        // Return the dateObject formatted following the pattern
        return dateFormat.format(dateObject);
    }

    /**
     * Returns the color of the background of the layout containing the section of the article
     *
     * @param sectionTitle is the title of the section
     * @return the color
     */
    private int getSectionColor(String sectionTitle) {

        // Initialization of the variable which will hold the color
        int sectionColor;

        // Assign the colors based on the title of the section
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

        // Return the color based on the section title
        return ContextCompat.getColor(mContext, sectionColor);
    }

    /**
     * Base class for {@link ArticleAdapter} which stores and recycles views as they are scrolled
     * off screen.
     */
    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.separator_view)
        View separatorView;
        @BindView(R.id.section_view)
        TextView sectionView;
        @BindView(R.id.date_view)
        TextView dateView;
        @BindView(R.id.thumbnail_view)
        ImageView thumbnailView;
        @BindView(R.id.title_view)
        TextView titleView;
        @BindView(R.id.author_view)
        TextView authorView;
        @BindView(R.id.trail_view)
        TextView trailView;

        /**
         * Describes an item view and metadata about its place within the RecyclerView.
         * It subclasses the {@link ArticleViewHolder} and adds fields for caching potentially
         * expensive findViewById(int) results.
         *
         * @param itemView is the {@link View} referenced and represented by
         *                 this {@link ArticleViewHolder}
         */
        ArticleViewHolder(View itemView) {
            // Calls the parent constructor that accepts 1 parameter of the type View
            super(itemView);
            // Binding view
            ButterKnife.bind(this, itemView);
            // Set a click listener on the view
            itemView.setOnClickListener(this);
        }

        // Sets the Intent behavior when the item is clicked
        @Override
        public void onClick(View view) {
            // Find the current layout that was clicked on
            Article currentArticle = mArticles.get(getLayoutPosition());
            // Convert the String Url into a Uri object (to pass into the Intent constructor)
            Uri articleUri = Uri.parse(currentArticle.getArticleUrl());
            // Create a new intent to view the article Uri
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
            // Set the intent to launch a new activity
            mContext.startActivity(websiteIntent);
        }
    }
}

package com.example.limmonica.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    /**
     * Constant value for the article loader ID
     */
    private static final int ARTICLE_LOADER_ID = 1;

    /**
     * JSON response for a The Guardian query
     */
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?from-date=2018-08-01&show-fields=byline%2CtrailText%2Cthumbnail%2Cheadline&order-by=newest&page-size=30&q=technology&api-key=a42dcdcf-932d-4091-862e-e4328fa79e1d";

    /**
     * Adapter for the list of articles
     */
    private ArticleAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        // Find a reference to the {@link ListView in the layout
        ListView articleListView = findViewById(R.id.list);

        // Find a reference to the {@link TextView} for the empty state
        mEmptyStateTextView = findViewById(R.id.empty_view);
        // Set the empty view
        articleListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes the list of articles as input
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView} so the list can be populated
        // in the user interface
        articleListView.setAdapter(mAdapter);

        // Set an item click listener of the ListView, which sends an intent to a web browser to
        // open a website with more info about the selected article
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current article that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert the String Url into a Uri object (to pass into the Intent constructor)
                assert currentArticle != null;
                Uri articleUri = Uri.parse(currentArticle.getArticleUrl());

                // Create a new intent to view the article Uri
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Set the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        // Set empty state text to display "No articles found."
        mEmptyStateTextView.setText(R.string.no_articles);

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous article data
        mAdapter.clear();

        // If there is a valid list of {@link Article}, then add them to the adapter's data set.
        // This will trigger the ListView to update
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

        // Loader reset so we can clear out our existing data
        mAdapter.clear();
    }
}

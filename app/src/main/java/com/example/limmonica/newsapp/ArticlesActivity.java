package com.example.limmonica.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";

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

        // Find a reference to the {@link ListView} in the layout
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

    /**
     * This method initialize the contents of the Activity's options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This method is called whenever an item in the options menu is selected.
     * It passes the MenuItem that is selected
     *
     * @param item is the item that is selected
     * @return returns boolean true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Returns a unique ID for the menu item defined by the android:id attribute in the
        // menu resource as action_settings
        int id = item.getItemId();
        // Match the ID against known menu items to perform the appropriate action
        // If the ID matches the action_settings menu item
        if (id == R.id.action_settings) {
            // Create an intent to open the Settings Activity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            // Start Settings Activity
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Instantiates and returns a mew loader for the given ID
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieves a String value from the preferences. The second param is the default value for
        // this preference
        String numberArticles = sharedPrefs.getString(
                getString(R.string.settings_number_articles_key),
                getString(R.string.settings_number_articles_default)
        );

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // Breaks apart the URI string that's passed into its param
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // Prepares the baseUri that we just parsed so we can add query params to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query params and its value
        uriBuilder.appendQueryParameter("from-date", "2018-08-01");
        uriBuilder.appendQueryParameter("show-fields", "byline,trailText,thumbnail,headline");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", numberArticles);
        uriBuilder.appendQueryParameter("q", "technology");
        uriBuilder.appendQueryParameter("api-key", "a42dcdcf-932d-4091-862e-e4328fa79e1d");
        // Create a new loader for the completed uri
        return new ArticleLoader(this, uriBuilder.toString());
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

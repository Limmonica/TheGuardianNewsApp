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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>, SwipeRefreshLayout.OnRefreshListener {

    //Constant value for the article loader ID
    private static final int ARTICLE_LOADER_ID = 1;

    //JSON response for a The Guardian query
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";

    // Create a new list of articles
    private final List<Article> articleList = new ArrayList<>();
    // Get a reference to the LoaderManager
    LoaderManager loaderManager = getLoaderManager();
    // Get a reference to the ArticleAdapter
    private ArticleAdapter mAdapter;
    // Get a reference to the TextView used to display when there is no article
    private TextView mEmptyStateTextView;
    // Get a reference to the RecyclerView View
    private RecyclerView mRecyclerView;
    // Get a reference to the SwipeRefreshLayout
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        // Set a swipe refresh layout
        swipeLayout = findViewById(R.id.swipe_container);
        // Set the color resources used in the progress animation from color resources
        swipeLayout.setColorSchemeResources(
                R.color.news,
                R.color.opinion,
                R.color.sport,
                R.color.culture,
                R.color.lifestyle);
        // Set a refresh listener of the swipe refresh layout
        swipeLayout.setOnRefreshListener(this);

        // Find a reference to the {@link RecyclerView} in the layout
        mRecyclerView = findViewById(R.id.list);
        // Set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Create a new adapter
        mAdapter = new ArticleAdapter(this, articleList);
        // Set the adapter on the recycler view so the list can be populated in the user interface
        mRecyclerView.setAdapter(mAdapter);

        // Find a reference to the {@link TextView} for the empty state
        mEmptyStateTextView = findViewById(R.id.empty_view);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Initialize the loader
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error:
            // Find the loading indicator
            View loadingIndicator = findViewById(R.id.loading_indicator);
            // Set its visibility to gone
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

        // Get the preferences
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

        String apiKey = BuildConfig.THE_GUARDIAN_API_KEY;
        // Append query params and its value
        uriBuilder.appendQueryParameter("from-date", "2018-08-01");
        uriBuilder.appendQueryParameter("show-fields", "byline,trailText,thumbnail,headline");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", numberArticles);
        uriBuilder.appendQueryParameter("q", "technology");
        uriBuilder.appendQueryParameter("api-key", apiKey);
        // Create a new loader for the completed uri
        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        // Find the loading indicator
        View loadingIndicator = findViewById(R.id.loading_indicator);
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        // If there is a list of articles
        if (articles != null && !articles.isEmpty()) {
            // Update the adapter
            mAdapter.addAll(articles);
            // Notify the RecyclerView
            mAdapter.notifyDataSetChanged();
            // Make the list visible
            mRecyclerView.setVisibility(View.VISIBLE);
            // Hide the empty state
            mEmptyStateTextView.setVisibility(View.GONE);
            // Notify swipeRefreshLayout that the refresh has finished
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                }
            });
        } else {
            // Otherwise, hide the list
            mRecyclerView.setVisibility(View.GONE);
            // Make the empty state visible
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            // Set it to display "No articles found."
            mEmptyStateTextView.setText(R.string.no_articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mRecyclerView.removeAllViewsInLayout();
    }

    @Override
    public void onRefresh() {
        // Re-create the Loader. If there is currently a Loader associated with this ID,
        // it will be canceled/stopped/destroyed.
        loaderManager.restartLoader(ARTICLE_LOADER_ID, null, this);
    }
}

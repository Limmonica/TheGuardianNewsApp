package com.example.limmonica.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity {

    /**
     * Sample JSON response for a The Guardian query
     */
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?from-date=2018-08-01&to-date=2018-08-16&show-fields=byline%2CtrailText&q=uk%2Ftechnology&api-key=a42dcdcf-932d-4091-862e-e4328fa79e1d";

    /**
     * Adapter for the list of articles
     */
    private ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        // Find a reference to the {@link ListView in the layout
        ListView articleListView = findViewById(R.id.list);

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

        // Start the AsyncTask to fetch the article data
        ArticleAsyncTask task = new ArticleAsyncTask();
        task.execute(GUARDIAN_REQUEST_URL);
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread and then update the
     * UI with the list of articles in the response.
     */
    private class ArticleAsyncTask extends AsyncTask<String, Void, List<Article>> {

        /**
         * This method runs on a background thread and performs the network request and returns a
         * list of {@link Article}s as the result.
         *
         * @param urls are the urls to which the app will connect to get the JSON data
         * @return a result which is a list of articles
         */
        @Override
        protected List<Article> doInBackground(String... urls) {

            // Don't perform the request if there are no URLs, or if the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            // Perform the connection, fetch the data, parse it and store it in a list of articles
            List<Article> result = QueryUtils.fetchArticleData(urls[0]);

            // Return the list of articles
            return result;
        }

        /**
         * This method receives, as input, the return value from the doInBackground() method. First
         * we clear out the adapter, to get rid of article data from a previous query to USGS.
         * Then we update the adapter with the new list of article, which will trigger the
         * ListView to re-populate its list items.
         *
         * @param data is a list of articles from the doInBackground() method
         */
        @Override
        protected void onPostExecute(List<Article> data) {

            // Clear the adapter of previous article data
            mAdapter.clear();

            // If there is a valid list of {@link Article}s, then add them to the adapter's dataset. This will trigger the ListView to update
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}

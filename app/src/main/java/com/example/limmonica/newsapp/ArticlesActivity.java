package com.example.limmonica.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ArticlesActivity extends AppCompatActivity {

    public static final String LOG_TAG = ArticlesActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        // Create a list of articles
        ArrayList<Article> articles = QueryUtils.extractArticles();
//        ArrayList<Article> articles = new ArrayList<>();
//        articles.add(new Article("2018-08-14T09:53:07Z", "Technology", R.drawable.test_img, "How to turn off Google's location tracking", "Samuel Gibbs", "Turning off location history won’t hide where you are when you use search, Maps or weather. Here’s how to stop being tracked", "https://www.theguardian.com/technology/2018/aug/14/how-to-turn-off-google-location-tracking"));
//        articles.add(new Article("2018-08-14T09:53:07Z", "Technology", R.drawable.test_img, "How to turn off Google's location tracking", "Samuel Gibbs", "Turning off location history won’t hide where you are when you use search, Maps or weather. Here’s how to stop being tracked", "https://www.theguardian.com/technology/2018/aug/14/how-to-turn-off-google-location-tracking"));
//        articles.add(new Article("2018-08-14T09:53:07Z", "Technology", R.drawable.test_img, "How to turn off Google's location tracking", "Samuel Gibbs", "Turning off location history won’t hide where you are when you use search, Maps or weather. Here’s how to stop being tracked", "https://www.theguardian.com/technology/2018/aug/14/how-to-turn-off-google-location-tracking"));
//        articles.add(new Article("2018-08-14T09:53:07Z", "Technology", R.drawable.test_img, "How to turn off Google's location tracking", "Samuel Gibbs", "Turning off location history won’t hide where you are when you use search, Maps or weather. Here’s how to stop being tracked", "https://www.theguardian.com/technology/2018/aug/14/how-to-turn-off-google-location-tracking"));
//        articles.add(new Article("2018-08-14T09:53:07Z", "Technology", R.drawable.test_img, "How to turn off Google's location tracking", "Samuel Gibbs", "Turning off location history won’t hide where you are when you use search, Maps or weather. Here’s how to stop being tracked", "https://www.theguardian.com/technology/2018/aug/14/how-to-turn-off-google-location-tracking"));

        // Find a reference to the {@link ListView in the layout
        ListView articleListView = findViewById(R.id.list);

        // Create a new adapter that takes the list of articles as input
        final ArticleAdapter adapter = new ArticleAdapter(this, articles);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(adapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current article that was clicked on
                Article currentArticle = adapter.getItem(position);

                // Convert the String Url into a Uri object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getArticleUrl());

                // Create a new intent to view the article Uri
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Set the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }
}

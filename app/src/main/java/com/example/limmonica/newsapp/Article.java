package com.example.limmonica.newsapp;

/**
 * An {@link Article} contains information related to a single article.
 */
public class Article {

    /**
     * Date of the article
     */
    private String mDate;
    /**
     * Section of the article
     */
    private String mSection;
    /**
     * Title of the article
     */
    private String mTitle;
    /**
     * Author of the article
     */
    private String mAuthor;
    /**
     * Trail/preview of the article
     */
    private String mTrail;
    /**
     * Url of the article
     */
    private String mUrl;

    /**
     * Constructs a new {@link Article} object.
     *
     * @param date      is the publishing date of the article
     * @param section   is the section the article belongs to
     * @param title     is the title of the article
     * @param author    is the author of the article
     * @param trail     is the preview of the article
     * @param url       is the url of the article
     */
    public Article(String date, String section, String title,
                   String author, String trail, String url) {
        mDate = date;
        mSection = section;
        mTitle = title;
        mAuthor = author;
        mTrail = trail;
        mUrl = url;
    }

    /**
     * @return the date of the article
     */
    public String getArticleDate() {
        return mDate;
    }

    /**
     * @return the section of the article
     */
    public String getArticleSection() {
        return mSection;
    }

    /**
     * @return the title of the article
     */
    public String getArticleTitle() {
        return mTitle;
    }

    /**
     * @return the author of the article
     */
    public String getArticleAuthor() {
        return mAuthor;
    }

    /**
     * @return the trail of the article
     */
    public String getArticleTrail() {
        return mTrail;
    }

    /**
     * @return the url of the article
     */
    public String getArticleUrl() {
        return mUrl;
    }
}
package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.NewsItem;

import java.util.ArrayList;

/**
 * Created by Leonardo on 6/26/14.
 */
public interface NewsFeedProtocol {
    public void didReceiveNewsFeed(ArrayList<NewsItem> newsItemArrayList);
    public void didReceiveNewsFeedError(String errorMessage);
}

package com.dsapps.newsreader.Interface;

import com.dsapps.newsreader.Model.IconsBetterIdea;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Shridhar on 09-May-18.
 */

public interface IconsBetterIdeaService {
    @GET
    Call<IconsBetterIdea> getIconUrl(@Url String url);
}

package com.dsapps.newsreader.Interface;

import com.dsapps.newsreader.Common.Common;
import com.dsapps.newsreader.Model.Website;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Shridhar on 08-May-18.
 */

public interface NewsService
{
    @GET("v2/sources?language=en&apiKey="+ Common.API_KEY)
    Call<Website> getSources();
}

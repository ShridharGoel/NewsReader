package com.dsapps.newsreader.Common;

import com.dsapps.newsreader.Interface.IconsBetterIdeaService;
import com.dsapps.newsreader.Interface.NewsService;
import com.dsapps.newsreader.Remote.IconsBetterIdeaClient;
import com.dsapps.newsreader.Remote.RetrofitClient;

/**
 * Created by Shridhar on 08-May-18.
 */

public class Common
{
    private static final String BASE_URL="https://newsapi.org/";

    public static final String API_KEY="547c781393be4e48ae58facda207bada";

    public static NewsService getNewsService()
    {
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconsBetterIdeaService getIconService()
    {
        return IconsBetterIdeaClient.getClient().create(IconsBetterIdeaService.class);
    }

    //https://newsapi.org/v2/sources?apiKey=547c781393be4e48ae58facda207bada

    public static String getApiUrl(String source, String apiKey)
    {
        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source)
                .append("&apiKey=")
                .append(apiKey)
                .toString();
    }
}

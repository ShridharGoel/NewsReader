package com.dsapps.newsreader;

// Powered by NewsAPI.org

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dsapps.newsreader.Adapter.ListSourceAdapter;
import com.dsapps.newsreader.Common.Common;
import com.dsapps.newsreader.Interface.NewsService;
import com.dsapps.newsreader.Model.Website;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListSourceAdapter adapter;
    SpotsDialog dialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });

        //Init cache
        Paper.init(this);

        //Init service
        mService= Common.getNewsService();

        //Init View
        listWebsite=(RecyclerView)findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);

        dialog=new SpotsDialog(this);
        
        loadWebsiteSource(false);
    }

    private void loadWebsiteSource(boolean isRefreshed) {
        if(!isRefreshed) {
            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty()) //If having cache
            {
                Website website = new Gson().fromJson(cache, Website.class); //Converts Json to Object
                adapter = new ListSourceAdapter(getBaseContext(), website);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            } else //If not having cache
            {
                dialog.show();
                //Fetch new data
                mService.getSources().enqueue(new Callback<Website>() {
                    @Override
                    public void onResponse(Call<Website> call, Response<Website> response) {
                        adapter = new ListSourceAdapter(getBaseContext(), response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //Save to cache
                        Paper.book().write("cache", new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<Website> call, Throwable t) {

                    }
                });

                dialog.dismiss();
            }
        }
            else
            {
                dialog.show();
                //Fetch new data
                mService.getSources().enqueue(new Callback<Website>() {
                    @Override
                    public void onResponse(Call<Website> call, Response<Website> response) {
                        adapter=new ListSourceAdapter(getBaseContext(),response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //Save to cache
                        Paper.book().write("cache", new Gson().toJson(response.body()));

                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<Website> call, Throwable t) {

                    }
                });
                dialog.dismiss();

            }


    }
}

package com.dsapps.newsreader;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dsapps.newsreader.Adapter.ListNewsAdapter;
import com.dsapps.newsreader.Common.Common;
import com.dsapps.newsreader.Interface.NewsService;
import com.dsapps.newsreader.Model.Article;
import com.dsapps.newsreader.Model.News;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNews extends AppCompatActivity {

    KenBurnsView kbv;
    DiagonalLayout diagonalLayout;
    SpotsDialog dialog;
    NewsService mService;
    TextView topAuthor,topTitle;
    SwipeRefreshLayout swipeRefreshLayout;

    String source="",sortBy="",webLatestUrl="";

    RecyclerView recyclerNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        mService= Common.getNewsService();



        dialog=new SpotsDialog(this);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, true);
            }
        });

        diagonalLayout=(DiagonalLayout)findViewById(R.id.diagonal_layout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ListNews.this, DetailedArticle.class);
                intent.putExtra("webUrl",webLatestUrl);
                startActivity(intent);
            }
        });

        kbv=(KenBurnsView)findViewById(R.id.top_image);
        topAuthor=(TextView)findViewById(R.id.top_author);
        topTitle=(TextView)findViewById(R.id.top_title);

        recyclerNews=(RecyclerView)findViewById(R.id.recycler_news);
        recyclerNews.setHasFixedSize(true);
        recyclerNews.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent()!=null)
        {
            source=getIntent().getStringExtra("website");

            if(!source.isEmpty())
            {
                loadNews(source, false);
            }
        }



    }

    private void loadNews(String source, boolean isRefreshed)
    {
        if(!isRefreshed)
        {
            dialog.show();
            mService.getLatestArticles(Common.getApiUrl(source, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();

                            //Get first article
                            Picasso.with(getBaseContext()).load(response.body().getArticles().get(0).getUrlToImage()).into(kbv);

                            topAuthor.setText(response.body().getArticles().get(0).getAuthor());
                            topTitle.setText(response.body().getArticles().get(0).getTitle());
                            webLatestUrl=response.body().getArticles().get(0).getUrl();

                            //Get remaining articles
                            List<Article> articleList=response.body().getArticles();
                            articleList.remove(0);

                            ListNewsAdapter adapter=new ListNewsAdapter(articleList, getBaseContext());
                            recyclerNews.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        }

        else
        {
            dialog.show();
            mService.getLatestArticles(Common.getApiUrl(source, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();

                            //Get first article
                            Picasso.with(getBaseContext()).load(response.body().getArticles().get(0).getUrlToImage()).into(kbv);

                            topAuthor.setText(response.body().getArticles().get(0).getAuthor());
                            topTitle.setText(response.body().getArticles().get(0).getTitle());
                            webLatestUrl=response.body().getArticles().get(0).getUrl();

                            //Get remaining articles
                            List<Article> articleList=response.body().getArticles();
                            articleList.remove(0);  //We had already loaded first article in DiagonalLayout, so we need to remove it.

                            ListNewsAdapter adapter=new ListNewsAdapter(articleList, getBaseContext());
                            adapter.notifyDataSetChanged();
                            recyclerNews.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });

            swipeRefreshLayout.setRefreshing(false);
        }
    }
}

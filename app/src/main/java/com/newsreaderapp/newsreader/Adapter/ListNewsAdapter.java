package com.dsapps.newsreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsapps.newsreader.Common.ISO8601DateParser;
import com.dsapps.newsreader.DetailedArticle;
import com.dsapps.newsreader.Interface.ItemClickListener;
import com.dsapps.newsreader.Model.Article;
import com.dsapps.newsreader.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shridhar on 14-May-18.
 */

class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    ItemClickListener itemClickListener;
    TextView articleTitle;
    RelativeTimeTextView articleTime;
    CircleImageView articleImage;


    public ListNewsViewHolder(View itemView) {
        super(itemView);

        articleTitle=(TextView)itemView.findViewById(R.id.article_title);
        articleImage=(CircleImageView)itemView.findViewById(R.id.article_image);
        articleTime=(RelativeTimeTextView)itemView.findViewById(R.id.article_time);

        itemView.setOnClickListener(this);
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public TextView getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(TextView articleTitle) {
        this.articleTitle = articleTitle;
    }

    public RelativeTimeTextView getArticleTime() {
        return articleTime;
    }

    public void setArticleTime(RelativeTimeTextView articleTime) {
        this.articleTime = articleTime;
    }

    public CircleImageView getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(CircleImageView articleImage) {
        this.articleImage = articleImage;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

}

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder>
{
    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.news_layout, parent, false);
        return new ListNewsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ListNewsViewHolder holder, int position)
    {
        Picasso.with(context).load(articleList.get(position).getUrlToImage())
                .into(holder.articleImage);

        if(articleList.get(position).getTitle().length()>65)
        {
            holder.articleTitle.setText(articleList.get(position).getTitle().substring(0,65)+"...");
        }
        else
        {
            holder.articleTitle.setText(articleList.get(position).getTitle());
        }

        Date date=null;

        try
        {
            date= ISO8601DateParser.parse(articleList.get(position).getPublishedAt());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        holder.articleTime.setReferenceTime(date.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailedArticle.class);
                intent.putExtra("webUrl",articleList.get(position).getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
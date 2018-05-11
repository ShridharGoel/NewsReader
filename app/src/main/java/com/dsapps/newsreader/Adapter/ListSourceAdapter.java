package com.dsapps.newsreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dsapps.newsreader.Common.Common;
import com.dsapps.newsreader.Interface.IconsBetterIdeaService;
import com.dsapps.newsreader.Interface.ItemClickListener;
import com.dsapps.newsreader.ListNews;
import com.dsapps.newsreader.Model.IconsBetterIdea;
import com.dsapps.newsreader.Model.Website;
import com.dsapps.newsreader.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Shridhar on 09-May-18.
 */

class ListSourceViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{

    ItemClickListener itemClickListener;

    TextView sourceTitle;
    CircleImageView sourceImage;




    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    public ListSourceViewHolder(View itemView) {
        super(itemView);

        sourceImage=(CircleImageView)itemView.findViewById(R.id.source_image);
        sourceTitle=(TextView)itemView.findViewById(R.id.source_name);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder>
{

    private Context context;
    private Website website;

    IconsBetterIdeaService mService;

    public ListSourceAdapter(Context context, Website website) {
        this.context = context;
        this.website = website;

        mService=Common.getIconService();
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.source_layout, parent, false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder holder, int position) {
        StringBuilder iconBetterAPI=new StringBuilder("https://icons.better-idea.org/allicons.json?url=");
        iconBetterAPI.append(website.getSources().get(position).getUrl());

        mService.getIconUrl(iconBetterAPI.toString())
                .enqueue(new Callback<IconsBetterIdea>() {
                    @Override
                    public void onResponse(@NonNull Call<IconsBetterIdea> call, @NonNull Response<IconsBetterIdea> response) {
                        if (response.body()!=null && response.body().getIcons()!=null && response.body().getIcons().size() > 0
                                && !TextUtils.isEmpty(response.body().getIcons().get(0).getUrl()))
                        {
                            Picasso.with(context)
                                    .load(response.body().getIcons().get(0).getUrl())
                                    .into(holder.sourceImage);
                        }

                    }

                    @Override
                    public void onFailure(Call<IconsBetterIdea> call, Throwable t) {

                    }
                });

        holder.sourceTitle.setText(website.getSources().get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent=new Intent(context, ListNews.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("website",website.getSources().get(position).getId());
                intent.putExtra("sortBy",website.getSources().get(position).getSortByAvailable().get(0));
                intent.putExtra("sortBy","top");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return website.getSources().size();
    }
}

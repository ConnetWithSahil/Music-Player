package com.developer.musicapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.Viewloader> {

    ArrayList<AudioStructure> songlist;
    Context context;
    SearchView searchView;



    public SongAdapter(ArrayList<AudioStructure> songlist, Context context) {
        this.songlist = songlist;
        this.context = context;
    }

    @Override
    public Viewloader onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_items,parent,false);
        return new SongAdapter.Viewloader(view);
    }

    @Override
    public void onBindViewHolder(Viewloader holder, int position) {
        AudioStructure songData = songlist.get(position);
            holder.textView.setText(songData.title);

            if (MusicPlayer.currentIndex==position){
            holder.textView.setTextColor(Color.parseColor("#FF0000"));
        }else {
            holder.textView.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              MusicPlayer.getInstance().reset();
              MusicPlayer.currentIndex = holder.getAdapterPosition();
                Intent intent = new Intent(context,Music_player.class);
                intent.putExtra("List",songlist);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return songlist.size();
    }

    public class Viewloader extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        public Viewloader(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.music_title);
            imageView = itemView.findViewById(R.id.music_icon);
        }
    }
}

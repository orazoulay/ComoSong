package com.sapps.songprocess.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sapps.songprocess.R;
import com.sapps.songprocess.fragments.ChooseSongProperties;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {
    private List<String> mSongs;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int rowIndex = Integer.MAX_VALUE;
    private Context context;

    public SongsAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mSongs = data;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.tvSong.setText(mSongs.get(position));

        if (rowIndex == position) {
            holder.tvSong.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvSong.setTextColor(context.getResources().getColor(R.color.white));

        } else {
            holder.tvSong.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tvSong.setTextColor(context.getResources().getColor(R.color.textViewColor));

        }

    }


    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvSong;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSong = itemView.findViewById(R.id.tvSong);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(v, getAdapterPosition(), getItemViewType());
            rowIndex = getAdapterPosition();
            notifyDataSetChanged();

        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    @Override
    public int getItemViewType(int position) {
        return ChooseSongProperties.SONG;
    }
}


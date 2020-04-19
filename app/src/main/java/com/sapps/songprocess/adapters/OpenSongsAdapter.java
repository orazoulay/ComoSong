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
import com.sapps.songprocess.data.OpenSong;
import com.sapps.songprocess.data.User;
import com.sapps.songprocess.fragments.ChooseSongProperties;
import com.sapps.songprocess.fragments.MainFragment;

import java.util.List;

public class OpenSongsAdapter extends RecyclerView.Adapter<OpenSongsAdapter.UserViewHolder> {
    private List<OpenSong> mOpenSongs;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int rowIndex = Integer.MAX_VALUE;
    private Context context;

    public OpenSongsAdapter(Context context, List<OpenSong> openSongs) {
        this.mInflater = LayoutInflater.from(context);
        this.mOpenSongs = openSongs;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_open_song, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.tvSong.setText(mOpenSongs.get(position).getSongName());
        holder.tvSongSender.setText("מנהל פרויקט - "+mOpenSongs.get(position).getSongSenderName());

        if (rowIndex == position) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvSong.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvSongSender.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tvSong.setTextColor(context.getResources().getColor(R.color.textViewColor));
            holder.tvSongSender.setTextColor(context.getResources().getColor(R.color.textViewColor));
        }
    }


    @Override
    public int getItemCount() {
        return mOpenSongs.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvSong;
        TextView tvSongSender;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSong = itemView.findViewById(R.id.tvSong);
            tvSongSender = itemView.findViewById(R.id.tvSongSender);
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
        return MainFragment.OPENSONG;
    }
}


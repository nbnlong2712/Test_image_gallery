package com.longtraidep.imagegallery.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longtraidep.imagegallery.Object.DateVideo;
import com.longtraidep.imagegallery.R;
import com.longtraidep.imagegallery.Object.Video;

import java.util.List;

public class DateVideoAdapter extends RecyclerView.Adapter<DateVideoAdapter.DateVideoHolder> {

    private Activity mActivity;
    private List<DateVideo> mDateVideos;

    public DateVideoAdapter(Activity activity)
    {
        mActivity = activity;
    }
    public void setData(List<DateVideo> list)
    {
        mDateVideos = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DateVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_video, parent, false);
        return new DateVideoAdapter.DateVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateVideoHolder holder, int position) {
        DateVideo dateVideo = mDateVideos.get(position);
        holder.mDateVideo.setText(dateVideo.getDate());
        setVideoItemRecycler(holder.mRecyclerViewDateVideo, mDateVideos.get(position).getVideos());
    }

    @Override
    public int getItemCount() {
        if(mDateVideos!= null)
            return mDateVideos.size();
        return 0;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    public class DateVideoHolder extends RecyclerView.ViewHolder
    {
        private TextView mDateVideo;
        private RecyclerView mRecyclerViewDateVideo;

        public DateVideoHolder(@NonNull View itemView) {
            super(itemView);
            mDateVideo = (TextView) itemView.findViewById(R.id.date_video);
            mRecyclerViewDateVideo = (RecyclerView) itemView.findViewById(R.id.recyclerview_date_video);
        }
    }

    private void setVideoItemRecycler(RecyclerView recyclerView, List<Video> videoList)
    {
        VideoAdapter videoAdapter = new VideoAdapter(mActivity);
        videoAdapter.setData(videoList);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
        recyclerView.setAdapter(videoAdapter);
    }
}

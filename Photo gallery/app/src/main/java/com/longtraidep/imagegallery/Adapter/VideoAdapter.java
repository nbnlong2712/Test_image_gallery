package com.longtraidep.imagegallery.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.longtraidep.imagegallery.Activity.FullScreenVideoActivity;
import com.longtraidep.imagegallery.R;
import com.longtraidep.imagegallery.Object.Video;

import java.io.File;
import java.text.DateFormat;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private Activity mActivity;
    private List<Video> mVideoList;

    public VideoAdapter(Activity activity) {
        mActivity = activity;
    }
    public void setData(List<Video> videoList)
    {
        mVideoList = videoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = mVideoList.get(position);
        if(video == null)
        {
            return;
        }
        holder.bind(video);
        Glide.with(mActivity).load(new File(video.getThumb())).into(holder.mImageVideo);
    }

    @Override
    public int getItemCount() {
        if(mVideoList != null)
            return mVideoList.size();
        return 0;
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView mImageVideo;
        private Video mVideo;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageVideo = (ImageView) itemView.findViewById(R.id.img_video);
        }

        public void bind(Video video)
        {
            mVideo = video;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(mActivity, FullScreenVideoActivity.class);

            i.putExtra("path", mVideo.getPath());
            String strDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(mVideo.getDateAdded().getTime());
            i.putExtra("size", mVideo.getSize()+"");
            i.putExtra("dateTime", strDate);
            i.putExtra("duration", mVideo.getDuration());
            i.putExtra("thumb", mVideo.getThumb());
            mActivity.startActivity(i);
        }
    }
}

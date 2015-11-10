package com.example.hanzh.gankio_han.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hanzh.gankio_han.R;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;
    public ImageView mImageView;

    public MyRecyclerViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.tv_title);
        mImageView=(ImageView) itemView.findViewById(R.id.iv_meizhi);
    }
}

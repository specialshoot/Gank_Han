package com.example.hanzh.gankio_han.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hanzh.gankio_han.R;
import com.example.hanzh.gankio_han.view.RatioImageView;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyStaggeredViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;
    public RatioImageView mImageView;
    public View meizhitu;

    public MyStaggeredViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.tv_title);
        mImageView=(RatioImageView) itemView.findViewById(R.id.iv_meizhi);
        mImageView.setOriginalSize(50,50);
        meizhitu=itemView;
    }
}

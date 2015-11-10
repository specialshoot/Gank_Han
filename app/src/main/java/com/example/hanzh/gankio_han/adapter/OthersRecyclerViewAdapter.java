package com.example.hanzh.gankio_han.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hanzh.gankio_han.R;
import com.example.hanzh.gankio_han.WebActivity;
import com.example.hanzh.gankio_han.model.CategoricalData;
import com.example.hanzh.gankio_han.model.Gank;
import com.example.hanzh.gankio_han.utils.StringStyleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class OthersRecyclerViewAdapter extends AnimRecyclerViewAdapter<OthersRecyclerViewAdapter.ViewHolder> {

    public Context mContext;
    public List<Gank> mGankList;
    public LayoutInflater mLayoutInflater;

    public OthersRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mGankList=new ArrayList<>();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other, parent, false);
        return new ViewHolder(v);
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        try {
            SpannableStringBuilder builder = new SpannableStringBuilder(mGankList.get(position).getDesc()).append(
                    StringStyleUtils.format(holder.mTextView.getContext(), " ( "+mGankList.get(position).getPublishedAt().substring(0,10)+" )",
                            R.style.ViaTextAppearance));
            CharSequence gankText = builder.subSequence(0, builder.length());
            holder.mTextView.setText(gankText);
            showItemAnim(holder.mTextView,position);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "未能找到数据", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mGankList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView=(TextView)itemView.findViewById(R.id.others_tv_title);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gank gank = mGankList.get(getLayoutPosition());
                    Intent intent = new Intent(v.getContext(), WebActivity.class);
                    intent.putExtra("Desc", gank.getDesc());
                    intent.putExtra("Url", gank.getUrl());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}

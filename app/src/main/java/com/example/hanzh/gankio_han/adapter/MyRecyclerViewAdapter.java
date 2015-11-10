package com.example.hanzh.gankio_han.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hanzh.gankio_han.R;
import com.example.hanzh.gankio_han.model.CategoricalData;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public Context mContext;
    public CategoricalData mDatas;
    public LayoutInflater mLayoutInflater;

    public MyRecyclerViewAdapter(Context mContext,CategoricalData Datas) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = Datas;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_meizhi, parent, false);
        MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
        return mViewHolder;
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });

        }

        try {
            holder.mTextView.setText(mDatas.getResults().get(position).getDesc());
            Glide.with(mContext).load(mDatas.getResults().get(position).getUrl()).into(holder.mImageView);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "未能找到数据", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.getResults().size();
    }
}

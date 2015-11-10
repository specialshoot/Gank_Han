package com.example.hanzh.gankio_han.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.example.hanzh.gankio_han.App;
import com.example.hanzh.gankio_han.R;
import com.example.hanzh.gankio_han.model.Gank;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyStaggeredViewAdapter extends RecyclerView.Adapter<MyStaggeredViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position,Gank gank);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public Context mContext;
    public List<Gank> ganks;
    public LayoutInflater mLayoutInflater;

    public MyStaggeredViewAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        ganks=new ArrayList<>();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public MyStaggeredViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_meizhi, parent, false);
        MyStaggeredViewHolder mViewHolder = new MyStaggeredViewHolder(mView);
        return mViewHolder;
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final MyStaggeredViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.mTextView, position,ganks.get(position));
                }
            });

            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.mImageView, position,ganks.get(position));
                }
            });

            holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.mTextView, position);
                    return true;
                }
            });

            holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.mImageView, position);
                    return true;
                }
            });

        }

        try {
            holder.mTextView.setText(ganks.get(position).getUpdatedAt() + " Provided by " + ganks.get(position).getWho());
            Glide.with(mContext).load(ganks.get(position).getUrl()).centerCrop().into(holder.mImageView).getSize(
                    new SizeReadyCallback() {

                        @Override
                        public void onSizeReady(int width, int height) {
                            if (!holder.meizhitu.isShown())
                                holder.meizhitu.setVisibility(View.VISIBLE);
                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "未能找到数据", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return ganks.size();
    }
}

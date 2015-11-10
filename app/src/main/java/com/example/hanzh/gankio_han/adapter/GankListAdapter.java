/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.hanzh.gankio_han.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hanzh.gankio_han.R;
import com.example.hanzh.gankio_han.utils.StringStyleUtils;
import com.example.hanzh.gankio_han.WebActivity;
import com.example.hanzh.gankio_han.model.Gank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by drakeet on 8/11/15.
 */
public class GankListAdapter extends AnimRecyclerViewAdapter<GankListAdapter.ViewHolder> {

    public Context context;
    public List<Gank> mGankList;

    public GankListAdapter(Context context) {
        this.context=context;
        mGankList=new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gank gank = mGankList.get(position);
        if (position == 0) {
            showCategory(holder);
        }
        else {
            boolean theCategoryOfLastEqualsToThis =
                    mGankList.get(position - 1).getType().equals(mGankList.get(position).getType());
            if (!theCategoryOfLastEqualsToThis) {
                showCategory(holder);
            }
            else {
                hideCategory(holder);
            }
        }
        holder.category.setText(gank.getType());
        SpannableStringBuilder builder = new SpannableStringBuilder(gank.getDesc()).append(
                StringStyleUtils.format(holder.gank.getContext(), " (via. " + gank.getWho() + ")",
                        R.style.ViaTextAppearance));
        CharSequence gankText = builder.subSequence(0, builder.length());

        holder.gank.setText(gankText);
        showItemAnim(holder.gank, position);
    }

    private boolean isVisibleOf(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    private void showCategory(ViewHolder holder) {
        if (!isVisibleOf(holder.category)) holder.category.setVisibility(View.VISIBLE);
    }

    private void hideCategory(ViewHolder holder) {
        if (isVisibleOf(holder.category)) holder.category.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mGankList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;
        TextView gank;

        public ViewHolder(View itemView) {
            super(itemView);
            category=(TextView)itemView.findViewById(R.id.tv_category);
            gank=(TextView)itemView.findViewById(R.id.tv_title);
            gank.setOnClickListener(new View.OnClickListener() {
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

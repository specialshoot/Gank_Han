package com.example.hanzh.gankio_han.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hanzh.gankio_han.R;
import com.example.hanzh.gankio_han.adapter.OthersRecyclerViewAdapter;
import com.example.hanzh.gankio_han.model.CategoricalData;
import com.example.hanzh.gankio_han.model.Gank;
import com.example.hanzh.gankio_han.utils.ToastUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class OthersFragment extends Fragment {

    @Bind(R.id.other_pulltorefresh)
    PtrFrameLayout ptrFrame;
    @Bind(R.id.rv_gank)
    RecyclerView mRecyclerView;

    private static final int IOS = 1;
    private static final int ANDROID = 2;
    private static final int TUIJIAN = 3;
    private static final int ZIYUAN = 4;
    private OthersRecyclerViewAdapter mAdapter;
    private View mView;
    private int flag;
    private CategoricalData meizhiData;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = (int) getArguments().get("flag");
        switch (flag) {
            case IOS:
                url = "http://gank.io/api/data/iOS/40/1";
                break;
            case ANDROID:
                url = "http://gank.io/api/data/Android/40/1";
                break;
            case TUIJIAN:
                url = "http://gank.io/api/data/瞎推荐/40/1";
                break;
            case ZIYUAN:
                url = "http://gank.io/api/data/拓展资源/40/1";
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_others, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewAction();
    }

    private void initViewAction() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new OthersRecyclerViewAdapter(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        getData(url);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData(url);
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    @OnClick(R.id.others_backtotop)
    void others_backtotop() {
        ToastUtils.showShort(getActivity(),"返回顶部");
        mRecyclerView.smoothScrollToPosition(0);
    }

    private void getData(String url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        //enqueue开启一步线程访问网络
        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Request request, IOException e) {
                                                System.out.println("client onFailure");
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ToastUtils.showShort(getActivity(), "网络错误");
                                                        ptrFrame.refreshComplete();
                                                    }
                                                });
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onResponse(Response response) throws IOException {
                                                if (response.isSuccessful()) {
                                                    String body = response.body().string();
                                                    meizhiData = null;
                                                    CategoricalData data = new Gson().fromJson(body, CategoricalData.class);
                                                    if (data.isError() == true) {
                                                        ToastUtils.showShort(getActivity(), "没有新数据");
                                                        System.out.println("no data error");
                                                        ptrFrame.refreshComplete();
                                                    } else {
                                                        int count = data.getResults().size();
                                                        if (count <= 0) {
                                                            ToastUtils.showShort(getActivity(), "没有新数据");
                                                            System.out.println("no data error");
                                                            ptrFrame.refreshComplete();
                                                        } else {
                                                            meizhiData = data;
                                                            getActivity().runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            // 刷新时模拟数据的变化
                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    try {
                                                                                        mAdapter.mGankList.clear();
                                                                                        mAdapter.mGankList.addAll(meizhiData.getResults());
                                                                                        mAdapter.notifyDataSetChanged();
                                                                                    }catch (Exception e){
                                                                                        e.printStackTrace();
                                                                                    }finally {
                                                                                        ptrFrame.refreshComplete();
                                                                                    }
                                                                                }
                                                                            }, 1000);
                                                                        }
                                                                    }
                                                            );
                                                        }
                                                    }
                                                }
                                            }
                                        }

        );
    }
}

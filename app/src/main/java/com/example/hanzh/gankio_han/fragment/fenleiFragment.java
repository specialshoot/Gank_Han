package com.example.hanzh.gankio_han.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hanzh.gankio_han.App;
import com.example.hanzh.gankio_han.PictureActivity;
import com.example.hanzh.gankio_han.R;
import com.example.hanzh.gankio_han.adapter.MyStaggeredViewAdapter;
import com.example.hanzh.gankio_han.model.CategoricalData;
import com.example.hanzh.gankio_han.model.Gank;
import com.example.hanzh.gankio_han.utils.ToastUtils;
import com.google.gson.Gson;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class fenleiFragment extends Fragment implements MyStaggeredViewAdapter.OnItemClickListener {

    private static String TAG = "fenlei";
    private static final int TOPDATA = 0;   //数据顶端
    private static final int BOTTOMDATA = 1;    //数据底端
    private static final int PRELOAD_SIZE = 2;
    boolean mIsFirstTimeTouchBottom = true;
    int mPage = 1;
    int bottomPage = 1;

    private View mView;
    @BindView(R.id.id_pulltorefresh)
    PtrFrameLayout ptrFrame;
    private MyStaggeredViewAdapter mStaggeredAdapter;
    @BindView(R.id.id_recyclerview)
    RecyclerView mRecyclerView;
    //private RecyclerView.LayoutManager mLayoutManager;
    private static final int SPAN_COUNT = 2;    //两列
    private CategoricalData meizhiData;
    private static final String staticUrl = "http://gank.io/api/data/福利/10/";
    private String url = "";
    private List<Gank> mGankList;

    public fenleiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        url = staticUrl + mPage;
        QueryBuilder query = new QueryBuilder(Gank.class).appendOrderDescBy("publishedAt");

        mGankList = new ArrayList<>();
        ArrayList<Gank> temp = App.sDb.query(query);
        Log.v(TAG, "有" + temp.size() + "条数据");
        mGankList.addAll(temp);
        mView = inflater.inflate(R.layout.fragment_fenlei, container, false);
        ButterKnife.bind(this, mView);
        //设置瀑布流布局,SPAN_COUNT列
        final StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredAdapter = new MyStaggeredViewAdapter(getActivity());
        mStaggeredAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mStaggeredAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(getScrollToBottomListener(mLayoutManager));
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        url = staticUrl + mPage;
                        getFenLeiData(url, TOPDATA);
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mGankList.size() > 0) {
            getFenLeiFromOrm(mGankList);
        }
        getFenLeiData(url, TOPDATA);
    }

    @OnClick(R.id.fenlei_backtotop)
    void fenlei_backtotop() {
        ToastUtils.showShort(getActivity(), "返回顶部");
        mRecyclerView.smoothScrollToPosition(0);
    }

    RecyclerView.OnScrollListener getScrollToBottomListener(
            final StaggeredGridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1] >= mStaggeredAdapter.getItemCount() - PRELOAD_SIZE;
                if (isBottom) {
                    if (!mIsFirstTimeTouchBottom) {
                        //到达页面底端
                        ToastUtils.showShort(getActivity(), "到达页面底端");
                        bottomPage += 1;
                        url = staticUrl + bottomPage;
                        getFenLeiData(url, BOTTOMDATA);
                    } else {
                        mIsFirstTimeTouchBottom = false;
                    }
                }
            }
        };
    }

    //MyStaggeredViewAdapter
    @Override
    public void onItemClick(View view, int position, Gank gank) {

        switch (view.getId()) {
            case R.id.iv_meizhi:
                Intent intent = new Intent();
                intent.setClass(getActivity(), PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Url", gank.getUrl());
                bundle.putString("Desc", " Provided By " + gank.getWho());
                bundle.putString("PicId", gank.getPublishedAt());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_title:
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        ToastUtils.showShort(getActivity(), "long click!");
    }

    private void saveGanks(List<Gank> ganks) {
        //App.sDb.insert(ganks, ConflictAlgorithm.Abort);
        App.sDb.save(ganks);
    }

    private void getFenLeiFromOrm(List<Gank> ganks) {
        for (int i = 0; i < ganks.size(); i++) {
            mStaggeredAdapter.ganks.add(ganks.get(i));
        }
        mStaggeredAdapter.notifyDataSetChanged();
    }

    private void getFenLeiData(String url, final int DATA) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        switch (DATA) {
            case 0:
                ToastUtils.showShort(getActivity(), "刷新顶端数据");
                break;
            case 1:
                ToastUtils.showShort(getActivity(), "刷新底端数据");
        }
        //enqueue开启一步线程访问网络
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort(getActivity(), "网络错误，请检查网络");
                        ptrFrame.refreshComplete();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    meizhiData = null;
                    CategoricalData data = new Gson().fromJson(body, CategoricalData.class);
                    if (data.isError() == true) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort(getActivity(), "没有新数据");
                                ptrFrame.refreshComplete();
                            }
                        });
                        Log.v(TAG, "no data error");
                    } else {
                        if (data.getResults().size() == 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShort(getActivity(), "没有新数据");
                                    ptrFrame.refreshComplete();
                                }
                            });
                            Log.v(TAG, "no data error");
                        } else {
                            meizhiData = data;
                            for (int i = 0; i < data.getResults().size(); i++) {
                                Gank gank = data.getResults().get(i);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 刷新时模拟数据的变化
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                int tempMeizhiCount = meizhiData.getResults().size();
                                                if (tempMeizhiCount == 0) {
                                                    ToastUtils.showShort(getActivity(), "没有数据刷新");
                                                } else {
                                                    if (App.sDb.queryCount(Gank.class) <= 0) {
                                                        //第一次没有数据
                                                        List<Gank> listOnce = new ArrayList<Gank>();
                                                        listOnce.addAll(meizhiData.getResults());
                                                        saveGanks(listOnce);
                                                        QueryBuilder query = new QueryBuilder(Gank.class).appendOrderDescBy("publishedAt");
                                                        listOnce = App.sDb.query(query);
                                                        Log.v(TAG, "listOnce -> " + listOnce.toString());
                                                        getFenLeiFromOrm(listOnce);
                                                        listOnce.clear();
                                                    } else {
                                                        int newAdd = 0;   //新加入数据数目
                                                        List<Gank> list = new ArrayList<Gank>();
                                                        for (int i = 0; i < tempMeizhiCount; i++) {

                                                            QueryBuilder qb = new QueryBuilder(Gank.class)
                                                                    .columns(new String[]{"_id"})
                                                                    .where("url = ?", new String[]{meizhiData.getResults().get(i).getUrl()});
                                                            long count = App.sDb.queryCount(qb);
                                                            if (count > 0) {
                                                                Log.v(TAG, "已经存在第" + i + "个");
                                                            } else {
                                                                newAdd++;
                                                                switch (DATA) {
                                                                    case 0:
                                                                        mStaggeredAdapter.ganks.add(0, meizhiData.getResults().get(i));
                                                                        list.add(0, meizhiData.getResults().get(i));
                                                                        break;
                                                                    case 1:
                                                                        mStaggeredAdapter.ganks.add(meizhiData.getResults().get(i));
                                                                        list.add(meizhiData.getResults().get(i));
                                                                        break;
                                                                    default:
                                                                        break;
                                                                }
                                                            }
                                                        }
                                                        if (newAdd > 0) {
                                                            saveGanks(list);
                                                            mStaggeredAdapter.notifyDataSetChanged();
                                                            list.clear();
                                                        } else {
                                                            ToastUtils.showShort(getActivity(), "没有新数据加入");
                                                        }
                                                    }
                                                }
                                            } catch (Exception e) {
                                                ToastUtils.showShort(getActivity(), "没有新数据加入");
                                                e.printStackTrace();
                                            } finally {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ptrFrame.refreshComplete();
                                                    }
                                                });
                                            }
                                        }
                                    }, 1000);
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}

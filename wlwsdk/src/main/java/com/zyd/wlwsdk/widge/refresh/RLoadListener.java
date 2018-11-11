package com.zyd.wlwsdk.widge.refresh;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * 时间：on 2017/12/2 09:35.
 * 作者：by hygo04
 * 功能：整合SwipeRefreshLayout、BaseRecyclerViewAdapterHelper的刷新加载功能，封装
 */

public class RLoadListener<B extends BaseQuickAdapter> implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private int pageNum = 10; // 一页默认10条数据
    private RefreshLoadListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private B mAdapter;
    private boolean isEntryRefresh = false; // 是否进入刷新，默认不刷新


    public interface RefreshLoadListener {
        void onRefresh();
        void onLoading();
    }

    private RLoadListener() {

    }

    private RLoadListener(RLoadListener<B> listener) {
        this.pageNum = listener.pageNum;
        this.mListener = listener.mListener;
        this.swipeRefreshLayout = listener.swipeRefreshLayout;
        this.mRecyclerView = listener.mRecyclerView;
        this.mAdapter = listener.mAdapter;
        this.isEntryRefresh = listener.isEntryRefresh;

        init();
    }

    private void init() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this);
        }
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    @Override
    public void onRefresh() {
        if (mListener == null) return;
        if (swipeRefreshLayout != null) {
            if (mAdapter == null) {
                mListener.onRefresh();
            } else if (!mAdapter.isLoading()) {
                mListener.onRefresh();
                mAdapter.setEnableLoadMore(false); // 刷新时，不能加载
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (mListener == null) return;
        if (mAdapter != null && mAdapter.getData().size() != 0 && mAdapter.getData().size() % pageNum == 0) {
            if (swipeRefreshLayout == null) {
                mListener.onLoading();
            } else if (!swipeRefreshLayout.isRefreshing()) {
                mListener.onLoading();
                swipeRefreshLayout.setEnabled(false); // 加载时，不能刷新
            }
        } else if (mAdapter != null && mAdapter.getData().size() != 0 && mAdapter.getData().size() < 10) {
            if (swipeRefreshLayout == null) {
                mListener.onLoading();
            } else if (!swipeRefreshLayout.isRefreshing()) {
                mListener.onLoading();
                swipeRefreshLayout.setEnabled(false); // 加载时，不能刷新
            }
        }
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
            this.onRefresh();
        }
    }

    /**
     * 刷新完成
     */
    public void setRefreshComplete() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            // 刷新完成后，开启上拉加载
            setLoadMoreTrue();
        }
    }

    /**
     * 加载完成操作类型的枚举类
     */
    public enum LoadType{
        end, complete, fail
    }
    /**
     * 加载完成由于有多种状态
     */
    public void setLoadMore(LoadType type) {
        if (mAdapter != null) {
            switch(type) {
                case end:
                    mAdapter.loadMoreEnd();
                    break;
                case complete:
                    mAdapter.loadMoreComplete();
                    break;
                case fail:
                    mAdapter.loadMoreFail();
                    break;
            }
        }
        // 加载完成后，开启下拉刷新
        setRefreshLayoutTrue();
    }

    /**
     * 解除（加载时，不能刷新）的状态
     */
    public void setRefreshLayoutTrue() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(true);
        }
    }

    /**
     * 解除（刷新时，不能加载）的状态
     */
    public void setLoadMoreTrue() {
        if (mAdapter != null) {
            mAdapter.setEnableLoadMore(true);
        }
    }

    public static class Builder<B extends BaseQuickAdapter> {
        private RLoadListener<B> mListener;

        public Builder() {
            this.mListener = new RLoadListener<>();
        }

        public Builder<B> setPageNum(int pageNum) {
            mListener.pageNum = pageNum;
            return this;
        }

        public Builder<B> setRefreshLoadListener(@NonNull RefreshLoadListener listener) {
            mListener.mListener = listener;
            return this;
        }

        public Builder<B> setSwipeRefreshLayout(SwipeRefreshLayout layout) {
            mListener.swipeRefreshLayout = layout;
            return this;
        }

        public Builder<B> setRecyclerView(@NonNull RecyclerView recyclerView) {
            mListener.mRecyclerView = recyclerView;
            return this;
        }

        public Builder<B> setAdapter(@NonNull B adapter) {
            mListener.mAdapter = adapter;
            return this;
        }

        public Builder<B> setEntryRefresh(boolean refresh) {
            mListener.isEntryRefresh = refresh;
            return this;
        }

        public RLoadListener<B> create() {
            return new RLoadListener<>(mListener);
        }
    }
}

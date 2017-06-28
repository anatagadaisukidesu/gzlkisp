package com.gzlk.android.isp.fragment.activity.notice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gzlk.android.isp.R;
import com.gzlk.android.isp.activity.BaseActivity;
import com.gzlk.android.isp.adapter.RecyclerViewAdapter;
import com.gzlk.android.isp.api.activity.AppNoticeRequest;
import com.gzlk.android.isp.api.listener.OnMultipleRequestListener;
import com.gzlk.android.isp.fragment.base.BaseFragment;
import com.gzlk.android.isp.fragment.base.BaseSwipeRefreshSupportFragment;
import com.gzlk.android.isp.holder.activity.NoticeViewHolder;
import com.gzlk.android.isp.listener.OnTitleButtonClickListener;
import com.gzlk.android.isp.listener.OnViewHolderClickListener;
import com.gzlk.android.isp.model.activity.Activity;
import com.gzlk.android.isp.model.activity.AppNotice;

import java.util.List;

/**
 * <b>功能描述：</b>通知列表<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/06/28 21:21 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/06/28 21:21 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class NoticeListFragment extends BaseSwipeRefreshSupportFragment {

    public static NoticeListFragment newInstance(String params) {
        NoticeListFragment nlf = new NoticeListFragment();
        Bundle bundle = new Bundle();
        // 传过来的tid
        bundle.putString(PARAM_QUERY_ID, params);
        nlf.setArguments(bundle);
        return nlf;
    }

    public static void open(BaseFragment fragment, String tid) {
        fragment.openActivity(NoticeListFragment.class.getName(), tid, true, false);
    }

    public static void open(Context context, int requestCode, String tid) {
        BaseActivity.openActivity(context, NoticeListFragment.class.getName(), tid, requestCode, true, false);
    }

    private String mActivityId;
    private NoticeAdapter mAdapter;

    @Override
    protected void onDelayRefreshComplete(@DelayType int type) {

    }

    @Override
    public void doingInResume() {
        // 通知列表
        setCustomTitle(R.string.ui_activity_notice_list_fragment_title);
        setRightText(R.string.ui_base_text_new);
        setRightTitleClickListener(new OnTitleButtonClickListener() {
            @Override
            public void onClick() {
                resultSucceededActivity();
                //openActivity(NoticeCreatorFragment.class.getName(), mQueryId, true, true);
            }
        });

        initializeAdapter();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_top_paddingable_swipe_recycler_view;
    }

    @Override
    protected boolean shouldSetDefaultTitleEvents() {
        return false;
    }

    @Override
    protected void destroyView() {

    }

    @Override
    protected void onSwipeRefreshing() {
        loadingNotices();
    }

    @Override
    protected void onLoadingMore() {
        loadingNotices();
    }

    @Override
    protected String getLocalPageTag() {
        return null;
    }

    private void fetchingActivity() {
        if (isEmpty(mActivityId)) {
            Activity act = Activity.getByTid(mQueryId);
            if (null != act) {
                mActivityId = act.getId();
            }
        }
    }

    private void loadingNotices() {
        fetchingActivity();
        setLoadingText(R.string.ui_activity_notice_list_loading_notices);
        displayLoading(true);
        displayNothing(false);
        AppNoticeRequest.request().setOnMultipleRequestListener(new OnMultipleRequestListener<AppNotice>() {
            @Override
            public void onResponse(List<AppNotice> list, boolean success, int totalPages, int pageSize, int total, int pageNumber) {
                super.onResponse(list, success, totalPages, pageSize, total, pageNumber);
                if (success) {
                    if (null != list) {
                        if (list.size() >= pageSize) {
                            remotePageNumber++;
                            isLoadingComplete(false);
                        } else {
                            isLoadingComplete(true);
                        }
                        mAdapter.update(list, false);
                    } else {
                        isLoadingComplete(true);
                    }
                } else {
                    isLoadingComplete(true);
                }
                displayLoading(false);
                displayNothing(mAdapter.getItemCount() < 1);
            }
        }).list(mActivityId);
    }

    private void initializeAdapter() {
        if (null == mAdapter) {
            mAdapter = new NoticeAdapter();
            mRecyclerView.setAdapter(mAdapter);
            loadingNotices();
        }
    }

    private OnViewHolderClickListener onViewHolderClickListener = new OnViewHolderClickListener() {
        @Override
        public void onClick(int index) {
            AppNotice notice = mAdapter.get(index);
            openActivity(NoticeDetailsFragment.class.getName(), notice.getId(), true, false);
        }
    };

    private class NoticeAdapter extends RecyclerViewAdapter<NoticeViewHolder, AppNotice> {

        @Override
        public NoticeViewHolder onCreateViewHolder(View itemView, int viewType) {
            NoticeViewHolder holder = new NoticeViewHolder(itemView, NoticeListFragment.this);
            holder.addOnViewHolderClickListener(onViewHolderClickListener);
            return holder;
        }

        @Override
        public int itemLayout(int viewType) {
            return R.layout.holder_view_activity_notice_item;
        }

        @Override
        public void onBindHolderOfView(NoticeViewHolder holder, int position, @Nullable AppNotice item) {
            holder.showContent(item);
        }

        @Override
        protected int comparator(AppNotice item1, AppNotice item2) {
            return 0;
        }
    }
}
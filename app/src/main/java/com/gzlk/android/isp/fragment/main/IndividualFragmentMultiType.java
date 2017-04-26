package com.gzlk.android.isp.fragment.main;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gzlk.android.isp.R;
import com.gzlk.android.isp.api.listener.OnRequestListListener;
import com.gzlk.android.isp.api.user.DocumentRequest;
import com.gzlk.android.isp.api.user.MomentRequest;
import com.gzlk.android.isp.application.App;
import com.gzlk.android.isp.etc.Utils;
import com.gzlk.android.isp.fragment.base.BaseSwipeRefreshSupportFragment;
import com.gzlk.android.isp.fragment.individual.MomentNewFragment;
import com.gzlk.android.isp.holder.BaseViewHolder;
import com.gzlk.android.isp.holder.IndividualFunctionViewHolder;
import com.gzlk.android.isp.holder.MomentViewHolder;
import com.gzlk.android.isp.lib.Json;
import com.gzlk.android.isp.listener.OnLiteOrmTaskExecutedListener;
import com.gzlk.android.isp.listener.OnLiteOrmTaskExecutingListener;
import com.gzlk.android.isp.model.Dao;
import com.gzlk.android.isp.model.Model;
import com.gzlk.android.isp.model.user.User;
import com.gzlk.android.isp.model.user.document.Document;
import com.gzlk.android.isp.model.user.moment.Moment;
import com.gzlk.android.isp.multitype.adapter.BaseMultiTypeAdapter;
import com.gzlk.android.isp.multitype.binder.IndividualFunctionalViewBinder;
import com.gzlk.android.isp.multitype.binder.user.DocumentViewBinder;
import com.gzlk.android.isp.multitype.binder.user.MomentViewBinder;
import com.gzlk.android.isp.multitype.binder.user.UserHeaderViewBinder;
import com.gzlk.android.isp.task.OrmTask;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能描述：</b><br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/04/20 22:15 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/04/20 22:15 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class IndividualFragmentMultiType extends BaseSwipeRefreshSupportFragment {

    private static final String PARAM_SHOWN = "title_bar_shown";
    private static final String PARAM_SELECTED = "function_selected";
    private static final String PAGE_TAG = "ifmt_page_%d_";
    private boolean isTitleBarShown = false;
    private int selectedFunction = 0;

    @Override
    protected void getParamsFromBundle(Bundle bundle) {
        super.getParamsFromBundle(bundle);
        isTitleBarShown = bundle.getBoolean(PARAM_SHOWN, false);
        selectedFunction = bundle.getInt(PARAM_SELECTED, 0);
    }

    @Override
    protected void saveParamsToBundle(Bundle bundle) {
        bundle.putBoolean(PARAM_SHOWN, isTitleBarShown);
        bundle.putInt(PARAM_SELECTED, selectedFunction);
        super.saveParamsToBundle(bundle);
    }

    /**
     * 标题栏是否已经显示了
     */
    public boolean isTitleBarShown() {
        return isTitleBarShown;
    }

    private SoftReference<View> toolBarView;

    public IndividualFragmentMultiType setToolBar(View view) {
        if (null == toolBarView || null == toolBarView.get()) {
            toolBarView = new SoftReference<>(view);
        }
        return this;
    }

    private SoftReference<View> textView;

    public void setToolBarTextView(View view) {
        if (null == textView || null == textView.get()) {
            textView = new SoftReference<>(view);
        }
    }

    @Override
    protected void onDelayRefreshComplete(@DelayType int type) {

    }

    @Override
    public void doingInResume() {
        // 这里不缓存选择了的图片，选择了一张图片之后就立即打开新发布窗口
        isSupportCacheSelected = false;
        localPageTag = format(PAGE_TAG, selectedFunction);
        initializeAdapter();
        autoRefreshing();
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
        performRefresh();
    }

    @Override
    protected void onLoadingMore() {
        performLoadingLocalModels();
    }

    /**
     * 尝试自动刷新
     */
    private void autoRefreshing() {
        if (!isNeedRefresh()) {
            return;
        }
        refreshing();
        performRefresh();
    }

    private void performRefresh() {
        switch (selectedFunction) {
            case 0:
                // 刷新动态列表
                refreshingMoments();
                break;
            case 1:
                // 刷新档案列表
                refreshingDocuments();
                break;
            case 2:
                // 刷新收藏列表
                refreshingFavorites();
                break;
        }
    }

    /**
     * 拉取我的最新说说列表
     */
    @SuppressWarnings("ConstantConditions")
    private void refreshingMoments() {
        MomentRequest.request().setOnRequestListListener(new OnRequestListListener<Moment>() {

            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(List<Moment> list, boolean success, int totalPages, int pageSize, int total, int pageNumber) {
                super.onResponse(list, success, totalPages, pageSize, total, pageNumber);
                if (success) {
                    remoteTotalPages = totalPages;
                    remotePageNumber = pageNumber;
                    remoteTotalCount = total;
                    remotePageSize = pageSize;
                    if (list.size() > 0) {
                        if (list.size() < remotePageSize) {
                            // 如果这一页不满整页说明远程已经没有数据了
                        }
                        new Dao<>(Moment.class).save(list);
                        if (selectedFunction == 0) {
                            // 下拉刷新的时候需要清空已显示的记录列表
                            adapter.update((List<Model>) (Object) list);
                        }
                        //appendListHeader(true);
                    }
                }
                stopRefreshing();
            }
        }).list(App.app().UserId());
    }

    /**
     * 拉取我的档案列表
     */
    @SuppressWarnings("ConstantConditions")
    private void refreshingDocuments() {
        DocumentRequest.request().setOnRequestListListener(new OnRequestListListener<Document>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(List<Document> list, boolean success, int totalPages, int pageSize, int total, int pageNumber) {
                super.onResponse(list, success, totalPages, pageSize, total, pageNumber);
                if (success) {
                    if (list.size() > 0) {
                        new Dao<>(Document.class).save(list);
                        if (selectedFunction == 1) {
                            adapter.update((List<Model>) (Object) list);
                        }
                        //appendListHeader(false);
                    }
                }
                stopRefreshing();
            }
        }).list(App.app().UserId());
    }

    private void refreshingFavorites() {
        stopRefreshing();
    }

    private void adjustLoadingMorePages(int loadedCount) {
        if (loadedCount >= PAGE_SIZE) {
            // 取出了一整页则对应的本地页码增1，下次会获取下一页
            localPageNumber++;
            // 还未加载完，下次还要继续加载下一页
            isLoadingComplete(false);
        } else {
            // 没有更多了
            isLoadingComplete(true);
        }
    }

    /**
     * 加载本地缓存
     */
    private void performLoadingLocalModels() {
        switch (selectedFunction) {
            case 0:
                // 加载更多的动态列表
                loadingLocalMoment();
                break;
            case 1:
                // 加载更多的档案列表
                loadingLocalDocuments();
                break;
            case 2:
                break;
        }
    }

    /**
     * 加载更多
     */
    private void loadingLocalMoment() {
        new OrmTask<Moment>().addOnLiteOrmTaskExecutedListener(new OnLiteOrmTaskExecutedListener<Moment>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onExecuted(boolean modified, List<Moment> result) {
                if (null != result) {
                    adjustLoadingMorePages(result.size());
                    //resetToday();
                    if (selectedFunction == 0) {
                        adapter.add((List<Model>) (Object) result, false);
                    }
                }
            }
        }).addOnLiteOrmTaskExecutingListener(new OnLiteOrmTaskExecutingListener<Moment>() {
            @Override
            public boolean isModifiable() {
                return false;
            }

            @SuppressWarnings("ConstantConditions")
            @Override
            public List<Moment> executing(OrmTask<Moment> task) {
                QueryBuilder<Moment> builder = new QueryBuilder<>(Moment.class)
                        .whereEquals(Model.Field.UserId, App.app().UserId())
                        .appendOrderDescBy(Model.Field.CreateDate)
                        .limit(localPageNumber, PAGE_SIZE);
                return new Dao<>(Moment.class).query(builder);
            }
        }).exec();
    }

    private void loadingLocalDocuments() {
        new OrmTask<Document>().addOnLiteOrmTaskExecutingListener(new OnLiteOrmTaskExecutingListener<Document>() {
            @Override
            public boolean isModifiable() {
                return false;
            }

            @SuppressWarnings("ConstantConditions")
            @Override
            public List<Document> executing(OrmTask<Document> task) {
                QueryBuilder<Document> builder = new QueryBuilder<>(Document.class)
                        .whereEquals(Model.Field.UserId, App.app().UserId())
                        .appendOrderDescBy(Model.Field.CreateDate)
                        .limit(localPageNumber, PAGE_SIZE);
                return new Dao<>(Document.class).query(builder);
            }
        }).addOnLiteOrmTaskExecutedListener(new OnLiteOrmTaskExecutedListener<Document>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onExecuted(boolean modified, List<Document> result) {
                if (null != result) {
                    adjustLoadingMorePages(result.size());
                    if (selectedFunction == 1) {
                        adapter.add((List<Model>) (Object) result, false);
                    }
                } else {
                    isLoadingComplete(true);
                }
            }
        }).exec();
    }

    private IndividualAdapter adapter;
    private Model functions;
    private Moment today;

    // 功能列表
    private Model functions() {
        if (null == functions) {
            functions = new Model();
            functions.setId("only for functions");
        }
        return functions;
    }

    /**
     * 重置“今天”的条目
     */
    private void resetToday() {
        adapter.add(today(), true);
    }

    // 今天
    private Moment today() {
        if (null == today) {
            today = new Moment();
            today.setId(getString(R.string.ui_text_moment_item_default_today));
        }
        // 设置时间为今天最后一秒，在排序时会一直排在最前面
        today.setCreateDate(Utils.formatDateOfNow("yyyy-MM-dd 23:59:59"));
        return today;
    }

    @SuppressWarnings("ConstantConditions")
    private void appendListHeader(boolean needToday) {
        adapter.add(App.app().Me(), 0);
        adapter.add(functions(), 1);
        if (needToday) {
            adapter.add(today(), 2);
        }
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    private void initializeAdapter() {
        if (null == adapter) {
            // 这里不需要直接上传，只需要把选择的图片传递给新建动态页面即可，上传在那里实现
            isSupportDirectlyUpload = false;
            // 添加图片选择
            addOnImageSelectedListener(imageSelectedListener);
            mRecyclerView.addOnScrollListener(scrollListener);
            adapter = new IndividualAdapter();
            adapter.register(User.class, new UserHeaderViewBinder().setFragment(this));
            adapter.register(Model.class, new IndividualFunctionalViewBinder(functionChangeListener).setFragment(this));
            adapter.register(Moment.class, new MomentViewBinder(boundDataListener).addOnGotPositionListener(gotPositionListener).setFragment(this));
            adapter.register(Document.class, new DocumentViewBinder().setFragment(this));
            mRecyclerView.setAdapter(adapter);
            appendListHeader(selectedFunction == 0);
            // 自动加载本地缓存中的记录
            performLoadingLocalModels();
//            String json = getString(R.string.temp_json_moment_multi);
//            ArrayList<Moment> moments = Json.gson().fromJson(json, new TypeToken<ArrayList<Moment>>() {
//            }.getType());
//            new Dao<>(Moment.class).save(moments);
//            adapter.add((ArrayList<Model>) (Object) moments, false);
        }
    }

    private OnImageSelectedListener imageSelectedListener = new OnImageSelectedListener() {
        @Override
        public void onImageSelected(ArrayList<String> selected) {
            // 打开新建动态页面
            openActivity(MomentNewFragment.class.getName(), Json.gson().toJson(selected), true, true);
        }
    };

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        private int scrolledY = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!isViewPagerDisplayedCurrent()) {
                return;
            }
            scrolledY += dy;
            if (scrolledY >= 0 && scrolledY <= 500) {
                float alpha = scrolledY * 0.005f;
                if (null != toolBarView && null != toolBarView.get()) {
                    toolBarView.get().setAlpha(alpha);
                    isTitleBarShown = toolBarView.get().getAlpha() >= 1;
                }
                if (null != textView && null != textView.get()) {
                    textView.get().setAlpha(alpha);
                }
            }
        }
    };

    private void resetList() {
        int size = adapter.getItemCount();
        while (size > 2) {
            adapter.remove(size - 1);
            size = adapter.getItemCount();
        }
        if (selectedFunction == 0) {
            adapter.add(today());
        }
    }

    private IndividualFunctionViewHolder.OnFunctionChangeListener functionChangeListener = new IndividualFunctionViewHolder.OnFunctionChangeListener() {
        @Override
        public void onChange(int index) {
            if (selectedFunction != index) {
                // 重置本地页码
                localPageNumber = 0;
                selectedFunction = index;
                localPageTag = format(PAGE_TAG, selectedFunction);
                resetList();
                // 重新加载本地缓存记录
                performLoadingLocalModels();
                // 尝试从服务器上拉取新纪录
                autoRefreshing();
                // 拉取不同类型的数据并显示
                //ToastHelper.make().showMsg(StringHelper.getStringArray(R.array.ui_individual_functions)[selectedFunction].replaceAll("\\d\\|", ""));
            }
        }
    };

    private MomentViewHolder.OnGotPositionListener gotPositionListener = new MomentViewHolder.OnGotPositionListener() {
        @Override
        public Moment previous(int myPosition) {
            if (myPosition > 1) {
                Model model = adapter.get(myPosition - 1);
                if (model instanceof Moment) {
                    return (Moment) model;
                }
            }
            return null;
        }
    };

    private BaseViewHolder.OnHandlerBoundDataListener<Model> boundDataListener = new BaseViewHolder.OnHandlerBoundDataListener<Model>() {
        @Override
        public Moment onHandlerBoundData(BaseViewHolder holder) {
            Model model = adapter.get(holder.getAdapterPosition());
            return (model instanceof Moment) ? ((Moment) model) : null;
        }
    };

    private class IndividualAdapter extends BaseMultiTypeAdapter<Model> {

        @Override
        protected int comparator(Model item1, Model item2) {
            if (item1 instanceof Moment && item2 instanceof Moment) {
                // 按照创建时间倒序排序
                int compared = ((Moment) item1).getCreateDate().compareTo(((Moment) item2).getCreateDate());
                return compared == 0 ? 0 : -compared;
            }
            return 0;
        }
    }
}
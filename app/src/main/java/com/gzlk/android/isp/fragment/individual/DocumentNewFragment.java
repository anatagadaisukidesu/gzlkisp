package com.gzlk.android.isp.fragment.individual;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.TimePickerView;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.gzlk.android.isp.R;
import com.gzlk.android.isp.adapter.RecyclerViewAdapter;
import com.gzlk.android.isp.api.listener.OnRequestListener;
import com.gzlk.android.isp.api.user.DocumentRequest;
import com.gzlk.android.isp.application.App;
import com.gzlk.android.isp.etc.Utils;
import com.gzlk.android.isp.fragment.base.BaseSwipeRefreshSupportFragment;
import com.gzlk.android.isp.helper.StringHelper;
import com.gzlk.android.isp.helper.ToastHelper;
import com.gzlk.android.isp.holder.AttachmentViewHolder;
import com.gzlk.android.isp.holder.SimpleClickableViewHolder;
import com.gzlk.android.isp.holder.SimpleInputableViewHolder;
import com.gzlk.android.isp.holder.ToggleableViewHolder;
import com.gzlk.android.isp.listener.OnTitleButtonClickListener;
import com.gzlk.android.isp.listener.OnViewHolderClickListener;
import com.gzlk.android.isp.model.Dao;
import com.gzlk.android.isp.model.user.document.Document;
import com.hlk.hlklib.lib.inject.Click;
import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.view.ClearEditText;
import com.hlk.hlklib.lib.view.CorneredView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <b>功能描述：</b>个人新增档案<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/04/25 09:50 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/04/25 09:50 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class DocumentNewFragment extends BaseSwipeRefreshSupportFragment {

    // UI
    @ViewId(R.id.ui_document_individual_title)
    private View titleInputView;
    @ViewId(R.id.ui_document_individual_source)
    private View sourceView;
    @ViewId(R.id.ui_document_individual_time)
    private View timeView;
    @ViewId(R.id.ui_document_individual_content)
    private ClearEditText contentView;
    @ViewId(R.id.ui_document_individual_attachments_layout)
    private LinearLayout attachmentLayout;

    // holder
    private SimpleInputableViewHolder titleHolder;
    private SimpleClickableViewHolder sourceHolder;
    private SimpleClickableViewHolder timeHolder;
    private ToggleableViewHolder privacyHolder;

    // data
    private String[] strings;
    private FileAdapter mAdapter;
    // 文件选择
    private FilePickerDialog filePickerDialog;
    DialogProperties properties;

    @Override
    protected void onDelayRefreshComplete(@DelayType int type) {

    }

    @Override
    public void doingInResume() {
        setCustomTitle(R.string.ui_text_document_create_fragment_title);
        setRightText(R.string.ui_base_text_save);
        setRightTitleClickListener(new OnTitleButtonClickListener() {
            @Override
            public void onClick() {
                ToastHelper.make().showMsg("暂时无法上传附件");
                tryCreateDocument();
            }
        });
        initializeHolders();
    }

    private void tryCreateDocument() {
        String title = titleHolder.getValue();
        if (StringHelper.isEmpty(title)) {
            ToastHelper.make().showMsg(R.string.ui_text_document_create_title_invalid);
            return;
        }
        String content = contentView.getValue();
        Utils.hidingInputBoard(contentView);
        createDocument(title, content);
    }

    @SuppressWarnings("ConstantConditions")
    private void createDocument(String title, String content) {
        DocumentRequest.request().setOnRequestListener(new OnRequestListener<Document>() {
            @Override
            public void onResponse(Document document, boolean success, String message) {
                super.onResponse(document, success, message);
                if (success) {
                    if (null != document) {
                        new Dao<>(Document.class).save(document);
                    }
                    ToastHelper.make().showMsg(message);
                    finish();
                }
            }
        }).add(title, content, Document.Type.TEXT, App.app().UserId(), App.app().Me().getName(), App.app().UserToken());
    }

    @Override
    protected boolean shouldSetDefaultTitleEvents() {
        return false;
    }

    @Override
    protected void destroyView() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_document_new;
    }

    @Override
    protected void onSwipeRefreshing() {

    }

    @Override
    protected void onLoadingMore() {

    }

    private void initializeHolders() {
        if (null == strings) {
            strings = StringHelper.getStringArray(R.array.ui_individual_new_document);
        }
        if (null == titleHolder) {
            titleHolder = new SimpleInputableViewHolder(titleInputView, this);
        }
        titleHolder.showContent(strings[0]);
        if (null == sourceHolder) {
            sourceHolder = new SimpleClickableViewHolder(sourceView, this);
        }
        sourceHolder.showContent(strings[1]);
        if (null == timeHolder) {
            timeHolder = new SimpleClickableViewHolder(timeView, this);
            timeHolder.addOnViewHolderClickListener(viewHolderClickListener);
        }
        showCreateDate(new Date());
        if (null == privacyHolder) {
            privacyHolder = new ToggleableViewHolder(mRootView, this);
        }
        privacyHolder.showContent(strings[3]);
        if (null == filePickerDialog) {
            properties = new DialogProperties();
            // 选择文件
            properties.selection_type = DialogConfigs.FILE_SELECT;
            // 可以多选
            properties.selection_mode = DialogConfigs.MULTI_MODE;
            // 最多可选文件数量
            properties.maximum_count = getMaxSelectable();
            // 文件扩展名过滤
            //properties.extensions = StringHelper.getStringArray(R.array.ui_base_file_pick_types);
            filePickerDialog = new FilePickerDialog(Activity(), properties);
            filePickerDialog.setTitle(StringHelper.getString(R.string.ui_text_document_picker_title));
            filePickerDialog.setPositiveBtnName(StringHelper.getString(R.string.ui_base_text_confirm));
            filePickerDialog.setNegativeBtnName(StringHelper.getString(R.string.ui_base_text_cancel));
            filePickerDialog.setDialogSelectionListener(dialogSelectionListener);
        }
        if (null == mAdapter) {
            mAdapter = new FileAdapter();
            mRecyclerView.setSupportLoadingMore(false);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private int itemHeight = 0;

    private void resetAttachmentsLayoutHeight() {
        if (itemHeight == 0) {
            itemHeight = attachmentLayout.getHeight() / mAdapter.getItemCount();
        }
        int screenHeight = getScreenHeight();
        int top = attachmentLayout.getTop();
        int bot = attachmentLayout.getBottom();
        int height = attachmentLayout.getMeasuredHeight();
        int items = itemHeight * mAdapter.getItemCount();
        log(format("top:%d,bottom:%d,height:%d/%d,mheight:%d", top, bot, bot - top, attachmentLayout.getHeight(), attachmentLayout.getMeasuredHeight()));
        if (top + items + itemHeight > screenHeight) {
            height = screenHeight - top - itemHeight;
        } else {
            height = items;
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) attachmentLayout.getLayoutParams();
        params.height = height;
        attachmentLayout.setLayoutParams(params);
    }

    private void resetAttachmentsHeight() {
//        Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                resetAttachmentsLayoutHeight();
//            }
//        });
    }

    private OnViewHolderClickListener viewHolderClickListener = new OnViewHolderClickListener() {
        @Override
        public void onClick(int index) {
            switch (index) {
                case 1:
                    // 选择日期
                    openDatePicker();
                    break;
            }
        }
    };

    private DialogSelectionListener dialogSelectionListener = new DialogSelectionListener() {
        @Override
        public void onSelectedFilePaths(String[] strings) {
            mAdapter.update(Arrays.asList(strings));
            resetAttachmentsHeight();
        }
    };

    private void resetSelectedFiles() {
        int size = mAdapter.getItemCount();
        if (size > 0) {
            List<String> tmp = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                tmp.add(mAdapter.get(i));
            }
            filePickerDialog.markFiles(tmp);
        }
    }

    @Click({R.id.ui_tool_attachment_button})
    private void elementClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ui_tool_attachment_button:
                Utils.hidingInputBoard(contentView);
                resetSelectedFiles();
                filePickerDialog.show();
                break;
        }
    }

    private void showCreateDate(Date date) {
        timeHolder.showContent(StringHelper.format(strings[2], Utils.format("yyyy年MM月dd日", date)));
    }

    private void openDatePicker() {
        Utils.hidingInputBoard(contentView);
        TimePickerView tpv = new TimePickerView.Builder(Activity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                showCreateDate(date);
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setTitleBgColor(getColor(R.color.colorPrimary))
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)
                .setContentSize(getFontDimension(R.dimen.ui_static_sp_20))
                .setOutSideCancelable(false)
                .isCenterLabel(true).isDialog(false).build();
        tpv.setDate(Calendar.getInstance());
        tpv.show();
    }

    private OnViewHolderClickListener attachmentViewHolderClickListener = new OnViewHolderClickListener() {
        @Override
        public void onClick(int index) {
            mAdapter.remove(index);
            resetAttachmentsHeight();
        }
    };

    private class FileAdapter extends RecyclerViewAdapter<AttachmentViewHolder, String> {
        @Override
        public AttachmentViewHolder onCreateViewHolder(View itemView, int viewType) {
            AttachmentViewHolder holder = new AttachmentViewHolder(itemView, DocumentNewFragment.this);
            holder.addOnViewHolderClickListener(attachmentViewHolderClickListener);
            return holder;
        }

        @Override
        public int itemLayout(int viewType) {
            return R.layout.holder_view_attachment;
        }

        @Override
        public void onBindHolderOfView(final AttachmentViewHolder holder, int position, @Nullable String item) {
            holder.showContent(item);
            Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (itemHeight <= 0) {
                        itemHeight = holder.itemView.getMeasuredHeight() + getDimension(R.dimen.ui_static_dp_half);
                    }
                    resetAttachmentsHeight();
                }
            });
        }

        @Override
        protected int comparator(String item1, String item2) {
            return item1.compareTo(item2);
        }
    }
}

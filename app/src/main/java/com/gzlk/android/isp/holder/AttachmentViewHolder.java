package com.gzlk.android.isp.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzlk.android.isp.R;
import com.gzlk.android.isp.etc.Utils;
import com.gzlk.android.isp.fragment.base.BaseFragment;
import com.hlk.hlklib.lib.inject.Click;
import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.hlklib.lib.view.CustomTextView;

import java.io.File;
import java.util.Locale;

/**
 * <b>功能描述：</b>档案中的附件item<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/04/25 17:01 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/04/25 17:01 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class AttachmentViewHolder extends BaseViewHolder {

    @ViewId(R.id.ui_holder_view_attachment_icon)
    private CustomTextView iconTextView;
    @ViewId(R.id.ui_holder_view_attachment_name)
    private TextView nameTextView;
    @ViewId(R.id.ui_holder_view_attachment_additional)
    private LinearLayout additionalView;
    @ViewId(R.id.ui_holder_view_attachment_path)
    private TextView pathTextView;
    @ViewId(R.id.ui_holder_view_attachment_size)
    private TextView sizeTextView;

    public AttachmentViewHolder(View itemView, BaseFragment fragment) {
        super(itemView, fragment);
        ViewUtility.bind(this, itemView);
    }

    public void showContent(String filePath) {
        String name = filePath.substring(filePath.lastIndexOf('/') + 1);
        String ext = name.substring(name.lastIndexOf('.') + 1);
        iconTextView.setText(getFileExtension(ext));
        nameTextView.setText(name);
        pathTextView.setText(filePath.replace(name, ""));
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            sizeTextView.setText(Utils.formatSize(file.length()));
        } else {
            sizeTextView.setText(null);
        }
    }

    public static int getFileExtension(String ext) {
        int res = R.string.ui_icon_attachment_unknown;
        ext = ext.toLowerCase(Locale.getDefault());
        if (ext.contains("doc")) {
            res = R.string.ui_icon_attachment_word;
        } else if (ext.contains("xls")) {
            res = R.string.ui_icon_attachment_excel;
        } else if (ext.contains("ppt")) {
            res = R.string.ui_icon_attachment_powerpoint;
        } else {
            switch (ext) {
                case "pdf":
                    res = R.string.ui_icon_attachment_pdf;
                    break;
                case "rar":
                    res = R.string.ui_icon_attachment_rar;
                    break;
                case "zip":
                    res = R.string.ui_icon_attachment_zip;
                    break;
                case "mp4":
                    res = R.string.ui_icon_attachment_mp4;
                    break;
                case "jif":
                case "jpg":
                case "bmp":
                case "jpeg":
                    res = R.string.ui_icon_attachment_picture;
                    break;
            }
        }
        return res;
    }

    @Click({R.id.ui_holder_view_attachment_delete})
    private void elementClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ui_holder_view_attachment_delete:
                if (null != mOnViewHolderClickListener) {
                    mOnViewHolderClickListener.onClick(getAdapterPosition());
                }
                break;
        }
    }
}

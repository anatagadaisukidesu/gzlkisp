package com.gzlk.android.isp.model.organization;

import com.gzlk.android.isp.R;
import com.gzlk.android.isp.helper.StringHelper;

/**
 * <b>功能描述：</b>关注的组织<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/05/07 09:46 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/05/07 09:46 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class Concern extends Organization {

    // 关注类型：1=上级组织，2=友好组织
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取关注的组织类型
     */
    public static String getTypeString(int type) {
        return StringHelper.getStringArray(R.array.ui_organization_concerned_type)[type];
    }
}

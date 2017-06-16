package com.gzlk.android.isp.nim.model.extension;

import com.gzlk.android.isp.nim.model.parser.NoticeAttachmentParser;

/**
 * <b>功能描述：</b>通知消息类实体<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/06/15 18:17 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/06/15 18:17 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class NoticeAttachment extends BaseAttachment {

    // 所属活动的id
    private String actId;
    // 通知标题
    private String title;
    // 通知内容
    private String content;
    //创建者Id
    private String creatorId;
    //创建者名称
    private String creatorName;
    //修改时间
    private String modifyDate;

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toJson(boolean b) {
        return NoticeAttachmentParser.packData(this);
    }
}

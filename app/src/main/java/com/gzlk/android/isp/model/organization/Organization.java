package com.gzlk.android.isp.model.organization;

import com.gzlk.android.isp.model.Model;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * <b>功能描述：</b>组织<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/05/07 08:56 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/05/07 08:56 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
@Table(Organization.Table.GROUP)
public class Organization extends Model {

    public static class Table {
        public static final String GROUP = "organization";
        public static final String SQUAD = "squad";
        public static final String INVITATION = "invitation";
        public static final String ROLE = "role";
        public static final String RELATION = "relation";
        public static final String PERMISSION = "permission";
        public static final String MEMBER = "member";
        public static final String CONCERN = "concern";
        public static final String JOIN = "joinGroup";
    }

    public static class Field {
        public static final String GroupId = "groupId";
        public static final String GroupName = "groupName";
        public static final String UpperId = "upperId";
        public static final String UpperName = "upperName";
        public static final String Nature = "nature";
        public static final String Logo = "logo";
        public static final String MemberNumber = "memberNum";
        public static final String Verified = "verified";
        public static final String RoleId = "roleId";
        public static final String RoleName = "roleName";
        public static final String SquadId = "squadId";
        public static final String PermissionId = "permissionId";
        public static final String GroupLogo = "groupLogo";
    }

    @Column(Model.Field.Name)
    private String name;           //组织名
    @Column(Field.UpperId)
    private String upId;           //上级组织ID
    @Column(Field.UpperName)
    private String upName;         //上级组织名称
    @Column(Field.Nature)
    private String nature;         //组织性质
    @Column(Field.Logo)
    private String logo;           //组织LOGO
    @Column(Model.Field.CreateDate)
    private String createDate;     //创建时间
    @Column(Model.Field.CreatorId)
    private String creatorId;      //创建者ID
    @Column(Model.Field.CreatorName)
    private String creatorName;    //创建者名称
    @Column(Field.MemberNumber)
    private String memberNum;      //成员数
    @Column(Field.Verified)
    private boolean verified;      //是否组织认证

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }

    public String getUpName() {
        return upName;
    }

    public void setUpName(String upName) {
        this.upName = upName;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
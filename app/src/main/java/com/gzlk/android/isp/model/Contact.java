package com.gzlk.android.isp.model;

import com.gzlk.android.isp.etc.Utils;
import com.gzlk.android.isp.helper.StringHelper;
import com.gzlk.android.isp.model.user.User;
import com.hlk.hlklib.etc.Cryptography;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * <b>功能描述：</b>手机通讯录<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/05/09 16:38 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/05/09 16:38 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
@Table("phoneContact")
public class Contact extends Model {

    @Column(Field.Name)
    private String name;
    @Column(User.Field.Phone)
    private String phone;
    @Column(User.Field.Spell)
    private String spell;
    @Column("invited")
    private boolean invited;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setId(Cryptography.md5(this.name));
        setSpell(Utils.transformPinyin(this.name));
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpell() {
        if (StringHelper.isEmpty(spell)) {
            spell = Utils.transformPinyin(name);
        }
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }
}

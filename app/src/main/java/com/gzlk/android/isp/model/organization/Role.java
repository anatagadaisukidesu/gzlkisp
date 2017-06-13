package com.gzlk.android.isp.model.organization;

import com.gzlk.android.isp.model.Dao;
import com.gzlk.android.isp.model.Model;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能描述：</b>组织内角色<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/05/07 09:18 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/05/07 09:18 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
@Table(Organization.Table.ROLE)
public class Role extends Model {

    public interface Field {
        String RoleName = "roleName";
        String RoleCode = "roleCode";
        String PermissionIds = "permissionIds";
    }

    /**
     * 角色类型
     */
    public interface Type {
        /**
         * 创建者
         */
        int CREATOR = 1;
        /**
         * 管理员
         */
        int MANAGER = 2;
        /**
         * 普通成员
         */
        int NORMAL = 3;
        /**
         * 档案管理员
         */
        int ARCHIVE_MANAGER = 4;
    }

    /**
     * 通过角色id查找角色的详细信息
     */
    public static Role getRole(String roleId) {
        return new Dao<>(Role.class).query(roleId);
    }

    /**
     * 通过roleName获取roleType
     */
    private static int getRoleType(String roleName) {
        if (isEmpty(roleName) || roleName.contains("普通成员") || roleName.contains("活动参与者")) {
            return Type.NORMAL;
        }
        if (roleName.contains("群创建者") || roleName.contains("活动发起者")) {
            return Type.CREATOR;
        }
        if (roleName.contains("群管理员")) {
            return Type.MANAGER;
        }
        if (roleName.contains("档案管理员")) {
            return Type.ARCHIVE_MANAGER;
        }
        return Type.NORMAL;
    }

    //角色名称
    @Column(Field.RoleName)
    private String roleName;
    //角色编码
    @Column(Field.RoleCode)
    private String rolCode;

    @Ignore
    private int roleType;
    //角色所拥有的权限
    @Ignore
    private ArrayList<Permission> perList;
    // 权限id列表
    @Column(Field.PermissionIds)
    private ArrayList<String> permissionIds;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRolCode() {
        return rolCode;
    }

    public void setRolCode(String rolCode) {
        this.rolCode = rolCode;
    }

    public int getRoleType() {
        if (0 == roleType) {
            roleType = getRoleType(roleName);
        }
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public ArrayList<Permission> getPerList() {
        getPermissions();
        return perList;
    }

    public void setPerList(ArrayList<Permission> perList) {
        this.perList = perList;
        savePermissionIds();
    }

    /**
     * 保存角色的权限列表以便以后再查询
     */
    public void savePermissionIds() {
        if (null != perList) {
            permissionIds = new ArrayList<>();
            for (Permission per : perList) {
                permissionIds.add(per.getId());
            }
            new Dao<>(Permission.class).save(perList);
        }
    }

    private void getPermissions() {
        if (null == perList) {
            perList = (ArrayList<Permission>) Permission.getPermissions(permissionIds);
        }
    }

    public ArrayList<String> getPermissionIds() {
        getPermissions();
        return permissionIds;
    }

    public void setPermissionIds(ArrayList<String> permissionIds) {
        this.permissionIds = permissionIds;
    }
}

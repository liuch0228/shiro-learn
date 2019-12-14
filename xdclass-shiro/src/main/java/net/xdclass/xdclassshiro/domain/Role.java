package net.xdclass.xdclassshiro.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 角色
 */
public class Role implements Serializable {
 private int id;
 private String name;
 private String description;
    /**
     * 关联查询角色对应的权限集合
     */
 private List<Permission> permissionList;

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

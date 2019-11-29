package net.xdclass.xdclassshiro.dao;

import net.xdclass.xdclassshiro.domain.Permission;
import net.xdclass.xdclassshiro.domain.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionMapper {

    @Select("select p.id id,p.name name,p.url url from permission p left join role_permission rp on rp.permission_id = p.id\n" +
            "where rp.role_id= #{roleId}")
    List<Permission> findPermissionListByRoleId(@Param("roleId") int roleId);

}

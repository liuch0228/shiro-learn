package net.xdclass.xdclassshiro.dao;

import net.xdclass.xdclassshiro.domain.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface RoleMapper {

    @Select("select ur.role_id id,r.name name,r.description description from role r\n" +
            "left join user_role ur  on r.id = ur.role_id where ur.user_id = #{userId}")
    @Results(
            value={
                    @Result(id=true,property = "id",column="id"),
                    @Result(property = "name",column="name"),
                    @Result(property = "description",column="description"),
                    @Result(property = "permissionList",column="id",
                    many = @Many(select = "net.xdclass.xdclassshiro.dao.PermissionMapper.findPermissionListByRoleId",fetchType = FetchType.DEFAULT))
            }
    )
    List<Role> findRoleListByUserId(@Param("userId") int userId);
}

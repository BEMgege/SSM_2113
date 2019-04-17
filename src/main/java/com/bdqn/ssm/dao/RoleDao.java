package com.bdqn.ssm.dao;

import com.bdqn.ssm.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 15001 on 2019/4/8.
 */
public interface RoleDao {
    public List<Role> getRole();
    public int getAllBill(@Param("roleName")String roleName,
                          @Param("roleCode")String roleCode);
}

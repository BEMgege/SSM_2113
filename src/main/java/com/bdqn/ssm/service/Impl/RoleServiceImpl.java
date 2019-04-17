package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.RoleDao;
import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 15001 on 2019/4/8.
 */
@Service
public class RoleServiceImpl implements RoleService{
    @Resource
    private RoleDao ud;


    @Override
    public List<Role> getUserList() {

        return ud.getRole();
    }
}

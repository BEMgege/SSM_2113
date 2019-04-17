package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.UserDao;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 15001 on 2019/4/5.
 */
@Service
public class UserServiceImpl implements UserService {



    @Resource
    private UserDao ud;

    @Override
    public User getLoginUser(String userCode, String userPassword) {
        return ud.getLoginUser(userCode,userPassword);
    }

    @Override
    public int getAllUser(@Param("userName") String userName, @Param("userRole") Integer userRole) {
        return ud.getAllUser(userName,userRole);
    }

    @Override
    public List<User> getUserList(@Param("userName") String userName,
                                  @Param("userRole") Integer userRole,
                                  @Param("currentPageNo") Integer currentPageNo,
                                  @Param("pageSize") Integer pageSize) {
        currentPageNo=(currentPageNo-1)*pageSize;
        return ud.getUserList(userName,userRole,currentPageNo,pageSize);
    }

    @Override
    public int addUser(User user) {
        return ud.addUser(user);
    }

    @Override
    public User getUserById(String id) {
        return ud.getUserById(id);
    }

    @Override
    public int modify(User user) {
        return ud.modify(user);
    }

    @Override
    public int delUser(String id) {
        return ud.delUser(id);
    }

    @Override
    public User getLoginUserCode(String userCode) {
        return ud.getLoginUserCode(userCode);
    }
}

package com.bdqn.ssm.service;

import com.bdqn.ssm.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 15001 on 2019/4/5.
 */
public interface UserService {
    public User getLoginUser(@Param("userCode") String userCode,
                             @Param("userPassword")String userPassword);

    //统计用户总记录数
    public int getAllUser(@Param("userName") String userName,
                          @Param("userRole") Integer userRole);
    //通过用户名以及角色进行模糊查询，分页查询用户信息
    public List<User> getUserList(@Param("userName") String userName,
                                  @Param("userRole") Integer userRole,
                                  @Param("currentPageNo") Integer currentPageNo,
                                  @Param("pageSize") Integer pageSize);
    //添加
    public int addUser(User user);
    //根据userid查询user
    public User getUserById(String id);
    //修改用户信息。表单提交处理
    public int modify(User user);
    //删除
    public int delUser(String id);

    /**
     * 通过userCode获得user
     * @param userCode
     * @return
     */
    public User getLoginUserCode(String userCode);
}

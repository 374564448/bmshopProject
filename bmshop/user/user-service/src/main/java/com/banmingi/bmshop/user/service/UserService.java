package com.banmingi.bmshop.user.service;

import com.banmingi.bmshop.user.mapper.UserMapper;
import com.banmingi.bmshop.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther 半命i 2019/11/18
 * @description
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户数据的校验: 用户名唯一性、手机号.
     * @param data
     * @param type:1校验用户名 2校验手机号
     * @return
     */
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        if (type == 1) {
            record.setUsername(data);
        } else if (type == 2) {
            record.setPhone(data);
        } else {
            return null;
        }
        return this.userMapper.selectCount(record) == 0;
    }
}

package com.banmingi.bmshop.user.service;

import com.banmingi.bmshop.common.utils.NumberUtils;
import com.banmingi.bmshop.user.mapper.UserMapper;
import com.banmingi.bmshop.user.pojo.User;
import com.banmingi.bmshop.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @auther 半命i 2019/11/18
 * @description
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:";

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

    /**
     * 发送验证码.
     * @param phone
     */
    public void sendVerifyCode(String phone) {
        if (StringUtils.isBlank(phone)) {
            return;
        }
        //生成验证码
        String code = NumberUtils.generateCode(6);

        //发送消息到消息队列
        Map<String,String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        this.amqpTemplate.convertAndSend("verifycode.sms",msg);

        //把验证码保存到redis中
        this.redisTemplate.opsForValue().set(KEY_PREFIX + phone,code,5, TimeUnit.MINUTES);
    }

    /**
     * 注册用户.
     * @param user
     * @param code
     */
    public void register(User user, String code) {
        //查询redis中的验证码
        String redisCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //校验验证码
        if (!StringUtils.equals(code,redisCode)) {
            return;
        }

        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        //新增用户
        user.setId(null);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);

        //删除redis中存储的验证码
        this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
    }

    /**
     * 查找用户.
     * @param username
     * @param password
     * @return
     */
    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);

        //根据用户名查询数据库获取盐值
        User user = this.userMapper.selectOne(record);
        if (user == null) {
            return null;
        }
        //获取用户输入的密码进行加盐加密
        password = CodecUtils.md5Hex(password,user.getSalt());

        //和数据库中的密码进行比较
        if (StringUtils.equals(password,user.getPassword())) {
            return user;
        }
        return null;
    }
}

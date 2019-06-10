package com.bcms.service;

import com.bcms.entity.User;

import java.util.Optional;

/**
 * @className UserService
 * @descrition 用户业务逻辑接口
 * @author Jackie
 * @date 2018/12/28 15:14
 */
public interface UserService extends BaseService<User, Long> {
    /**
     * 根据帐号和密码查询用户
     *
     * @param account  帐号
     * @param password 密码
     * @return Optional<User>
     */
    Optional<User> findByAccountAndPassword(String account, String password);

    /**
     * 根据openid查询用户
     *
     * @param openid 用户微信唯一标识
     * @return Optional<User>
     */
    Optional<User> findByOpenId(String openid);
}

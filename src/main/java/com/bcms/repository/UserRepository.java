package com.bcms.repository;

import com.bcms.entity.User;

import java.util.Optional;

/**
 * @className UserRepository
 * @descrition 用户数据访问接口
 * @author Jackie
 * @date 2018/12/28 15:10
 */
public interface UserRepository extends BaseRepository<User, Long> {
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

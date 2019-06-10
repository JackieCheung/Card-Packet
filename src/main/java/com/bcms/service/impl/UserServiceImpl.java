package com.bcms.service.impl;

import com.bcms.entity.User;
import com.bcms.repository.UserRepository;
import com.bcms.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Jackie
 * @className UserServiceImpl
 * @descrition 用户业务逻辑接口实现类
 * @date 2018/12/28 15:14
 */
@Data
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super.setBaseRepository(userRepository);
        this.userRepository = userRepository;
    }



    /**
     * 根据帐号和密码查找用户
     *
     * @param account  帐号
     * @param password 密码
     * @return Optional<User>
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<User> findByAccountAndPassword(String account, String password) {
        return userRepository.findByAccountAndPassword(account, password);
    }

    /**
     * 根据openid查询用户
     *
     * @param openid 用户微信唯一标识
     * @return Optional<User>
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<User> findByOpenId(String openid) {
        return userRepository.findByOpenId(openid);
    }
}

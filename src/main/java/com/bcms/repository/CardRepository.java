package com.bcms.repository;

import com.bcms.entity.Card;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @className CardRepository
 * @descrition 卡片数据访问接口
 * @author Jackie
 * @date 2019/4/7 22:17
 */
@Repository
public interface CardRepository extends BaseRepository<Card, Long> {
    /**
     * 根据卡号查询银行卡
     *
     * @param number 卡号
     * @return Optional<Card>
     */
    Optional<Card> findByNumber(String number);

    /**
     * 根据用户id查询卡列表
     *
     * @param id 用户id
     * @return List<Card>
     */
    List<Card> findByUserId(Long id);

    /**
     * 修改卡密码
     *
     * @param id       卡id
     * @param password 卡密码
     */
    @Modifying
    @Query("update Card c set c.password = :password where c.id = :id")
    void modifyPasswordById(@Param("id") Long id, @Param("password") String password);
}

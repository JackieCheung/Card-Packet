package com.bcms.service;


import com.bcms.common.JsonResponse;
import com.bcms.entity.Card;
import com.bcms.model.CardDTO;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName CardService
 * @Descrition 卡片业务逻辑接口
 * @Author Jackie
 * @Date 2019/4/7 22:32
 */
public interface CardService extends BaseService<Card, Long> {
    /**
     * 新增或修改卡片
     *
     * @param cardDTO card数据传输对象
     * @return JsonResponse Json响应结果
     */
    JsonResponse addOrUpdateCard(CardDTO cardDTO);

    /**
     * 根据卡号获取银行卡
     *
     * @param number 卡号
     * @return Optional<Card>
     */
    Optional<Card> findByNumber(String number);

    /**
     * 根据用户id获取卡列表
     *
     * @param id 用户id
     * @return List<Card>
     */
    List<Card> findByUserId(Long id);

    /**
     * 修改卡密码
     *
     * @param id       卡id
     * @param password 密码
     * @return JsonResponse Json响应结果
     */
    JsonResponse modifyPasswordById(Long id, String password);
}

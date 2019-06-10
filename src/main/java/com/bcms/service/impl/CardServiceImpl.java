package com.bcms.service.impl;

import com.bcms.common.BeanUtils;
import com.bcms.common.JsonResponse;
import com.bcms.entity.Card;
import com.bcms.model.CardDTO;
import com.bcms.model.CardVO;
import com.bcms.repository.CardRepository;
import com.bcms.repository.UserRepository;
import com.bcms.service.CardService;
import com.bcms.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcms.common.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author Jackie
 * @className CardServiceImpl
 * @descrition 卡片业务逻辑接口实现类
 * @date 2019/4/8 22:48
 */
@Data
@Service
@Transactional(rollbackFor = Exception.class)
public class CardServiceImpl extends BaseServiceImpl<Card, Long> implements CardService {

    private final CardRepository cardRepository;
    private final UserService userServiceImpl;

    /**
     * 密码正则表达式，六位纯数字
     */
    private static Pattern PASSWORD_PATTERN = Pattern.compile("^\\d{6}$");

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, UserService userServiceImpl) {
        super.setBaseRepository(cardRepository);
        this.cardRepository = cardRepository;
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * 新增或修改卡片
     *
     * @param cardDTO card数据传输对象
     * @return JsonResponse Json响应结果
     */
    @Override
    public JsonResponse addOrUpdateCard(CardDTO cardDTO) {
        Long id = cardDTO.getId();
        Long userId = cardDTO.getUserId();
        String number = cardDTO.getNumber();
        JsonResponse result = new JsonResponse();
        final Card[] cardArray = new Card[1];

        if (StringUtils.isNotBlank(number) && cardRepository.findByNumber(number).isPresent() && !cardRepository.findByNumber(number).get().getId().equals(id)) {
            return result.buildErrorResult("保存卡信息失败，该卡号已存在！");
        }

        if (userId == null) {
            return result.buildErrorResult("保存卡信息失败，用户id不能为空！");
        } else if (!this.userServiceImpl.existsById(userId)) {
            return result.buildErrorResult("保存卡信息失败，该用户不存在！");
        } else {
            // id为空，新增卡
            if (id == null) {
                cardArray[0] = new Card();
            } else {
                // 修改卡信息
                if (!this.existsById(id)) {
                    return result.buildErrorResult("修改卡信息失败，该卡id不存在！");
                } else {
                    cardRepository.findById(id).ifPresent(c -> cardArray[0] = c);
                }
            }
            try {
                BeanUtils.copyProperties(cardDTO, cardArray[0]);
                cardArray[0].setUser(userServiceImpl.findById(userId).get());
                return result.buildSuccessResult(new CardVO(cardRepository.save(cardArray[0])), "保存卡信息成功！");
            } catch (Exception e) {
                e.printStackTrace();
                return result.buildErrorResult("保存卡信息失败，" + e.getMessage());
            }
        }
    }

    /**
     * 根据卡号获取银行卡
     *
     * @param number 卡号
     * @return Optional<Card>
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Card> findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    /**
     * 根据用户id获取卡列表
     *
     * @param id 用户id
     * @return List<Card>
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Card> findByUserId(Long id) {
        return cardRepository.findByUserId(id);
    }

    /**
     * 修改卡密码
     *
     * @param id       卡id
     * @param password 密码
     * @return JsonResponse Json响应结果
     */
    @Override
    public JsonResponse modifyPasswordById(Long id, String password) {
        JsonResponse result = new JsonResponse();
        // 判断id是否为null
        if (id != null) {
            // 判断密码是否为null
            if (StringUtils.isNotBlank(password)) {
                // 判断密码的格式是否正确
                if (PASSWORD_PATTERN.matcher(password).matches()) {
                    // 判断卡是否存在
                    if (this.existsById(id)) {
                        try {
                            cardRepository.modifyPasswordById(id, password);
                            /// 下面代码同样的效果
                            /*cardRepository.findById(id).ifPresent(c -> {
                                c.setPassword(password);
                                cardRepository.save(c);
                            });*/
                            // 修改密码成功，返回修改后的卡信息
                            return result.buildSuccessResult(new CardVO(cardRepository.findById(id).isPresent() ? cardRepository.findById(id).get() : null), "修改密码成功！");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return result.buildErrorResult("修改密码失败，" + e.getMessage());
                        }
                    } else {
                        return result.buildErrorResult("修改密码失败，该卡不存在！");
                    }
                } else {
                    return result.buildErrorResult("修改密码失败，密码格式不正确，密码必须为6位纯数字！");
                }
            } else {
                return result.buildErrorResult("修改密码失败，密码不能为空！");
            }
        } else {
            return result.buildErrorResult("修改密码失败，卡id不能为空！");
        }
    }
}

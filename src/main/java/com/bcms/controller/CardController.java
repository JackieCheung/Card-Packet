package com.bcms.controller;

import com.bcms.common.JsonResponse;
import com.bcms.entity.Card;
import com.bcms.model.CardDTO;
import com.bcms.model.CardVO;
import com.bcms.service.CardService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jackie
 * @className CardController
 * @descrition Card控制器
 * @date 2019/4/8 22:59
 */
@Data
@RestController
@RequestMapping("/card")
public class CardController {
    private final CardService cardServiceImpl;

    @Autowired
    public CardController(CardService cardServiceImpl) {
        this.cardServiceImpl = cardServiceImpl;
    }

    /**
     * 新增卡
     *
     * @param cardDTO       cardDTO
     * @param bindingResult 参数校验结果
     * @return Map<String, Object>
     */
    @PostMapping("/save")
    public Map<String, Object> saveCard(@RequestBody @Valid CardDTO cardDTO, BindingResult bindingResult) {
        try {
            // 参数不合法，返回错误提示信息
            if (bindingResult.hasErrors()) {
                return JsonResponse.getErrorResult("参数不合法，" + bindingResult.getAllErrors().get(0).getDefaultMessage());
            } else {
                return cardServiceImpl.addOrUpdateCard(cardDTO).toMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getErrorResult("新增卡失败，" + e.getMessage());
        }
    }

    /**
     * 修改卡信息
     *
     * @param id      需要修改信息的卡的id
     * @param cardDTO cardDTO
     * @return Map<String, Object>
     */
    @PostMapping("/update/{id}")
    public Map<String, Object> updateCard(@PathVariable("id") Long id, @RequestBody @Valid CardDTO cardDTO, BindingResult bindingResult) {
        try {
            // 参数不合法，返回错误提示信息
            if (bindingResult.hasErrors()) {
                return JsonResponse.getErrorResult("参数不合法，" + bindingResult.getAllErrors().get(0).getDefaultMessage());
            } else {
                cardDTO.setId(id);
                return cardServiceImpl.addOrUpdateCard(cardDTO).toMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getErrorResult("修改卡信息失败，" + e.getMessage());
        }
    }

    /**
     * 修改卡密码
     *
     * @param id       需要修改密码的卡的id
     * @param password 新密码
     * @return Map<String, Object>
     */
    @PostMapping("/updatePassword/{id}")
    public Map<String, Object> updateCardPassword(@PathVariable("id") Long id, String password) {
        try {
            return cardServiceImpl.modifyPasswordById(id, password).toMap();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getErrorResult("修改密码失败，" + e.getMessage());
        }
    }

    /**
     * 查询某个用户所持有的卡
     *
     * @param userId 用户id
     * @return Map<String, Object>
     */
    @GetMapping("/all")
    public Map<String, Object> findAllCardByUserId(Long userId) {
        try {
            return JsonResponse.getSuccessResult(cardServiceImpl.findByUserId(userId).stream().map(CardVO::new), "查询该用户所持有的卡成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getErrorResult("查询该用户所持有的卡失败，" + e.getMessage());
        }
    }

    /**
     * 查询卡详情
     *
     * @param id 卡id
     * @return Map<String, Object>
     */
    @RequestMapping("/detail/{id}")
    public Map<String, Object> findById(@PathVariable("id") Long id) {
        try {
            Optional<Card> optionalCard = cardServiceImpl.findById(id);
            if (optionalCard.isPresent()) {
                return JsonResponse.getSuccessResult(new CardVO(optionalCard.get()), "查询卡详情成功！");
            } else {
                return JsonResponse.getErrorResult("查询卡详情失败，该卡不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getErrorResult("查询卡详情失败，" + e.getMessage());
        }
    }

    /**
     * 删除所选卡
     *
     * @param ids 需要删除的卡的id数组
     * @return Map<String, Object>
     */
    @RequestMapping("/delete")
    public Map<String, Object> deleteCard(@RequestParam(value = "ids[]") Long[] ids) {
        try {
            cardServiceImpl.deleteByIds(ids);
            return JsonResponse.getSuccessResult("删除所选卡成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getErrorResult("删除所选卡失败，" + e.getMessage());
        }
    }
}

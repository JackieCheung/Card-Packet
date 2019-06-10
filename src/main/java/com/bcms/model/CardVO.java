package com.bcms.model;

import com.bcms.common.BeanUtils;
import com.bcms.entity.Card;
import com.bcms.entity.CardTypeEnum;
import com.bcms.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Jackie
 * @className CardVO
 * @descrition Card视图对象，封装后端返回给前端的数据
 * @date 2019/4/7 22:43
 */
@Data
@NoArgsConstructor
public class CardVO {
    /**
     * 卡id
     */
    private Long id;
    /**
     * 用户
     */
    private User user;
    /**
     * 卡号
     */
    private String number;
    /**
     * 卡名
     */
    private String name;
    /**
     * 所属组织机构/发卡行
     */
    private String org;
    /**
     * 卡类型
     */
    private CardTypeEnum type;
    /**
     * 有效期
     */
    private String validDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 余额
     */
    private Double balance;
    /**
     * 密码
     */
    private String password;
    /**
     * 卡图片
     */
    private String imageUrl;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public CardVO(Card card) {
        BeanUtils.copyProperties(card, this);
    }
}

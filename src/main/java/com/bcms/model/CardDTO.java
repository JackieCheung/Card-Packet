package com.bcms.model;

import com.bcms.entity.CardTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Jackie
 * @className CardDTO
 * @descrition Card数据传输对象，封装前端传递给后端的数据
 * @date 2019/4/7 22:41
 */
@Data
@NoArgsConstructor
public class CardDTO {
    /**
     * 卡id
     */
    private Long id;
    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空！")
    private Long userId;
    /**
     * 卡号
     */
    @NotNull(message = "卡号不能为空！")
    private String number;
    /**
     * 卡名
     */
    @NotNull(message = "卡名不能为空！")
    private String name;
    /**
     * 所属组织机构/发卡行
     */
    @NotBlank(message = "所属组织机构/发卡行不能为空！")
    private String org;
    /**
     * 卡类型
     */
    @NotNull(message = "卡类型不能为空！")
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
    @Pattern(regexp = "^$|^\\d{6}$", message = "密码格式不正确，必须为6位纯数字！")
    private String password;

    /**
     * 卡图片
     */
    private String imageUrl;

}

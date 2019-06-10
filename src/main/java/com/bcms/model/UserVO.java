package com.bcms.model;

import com.bcms.common.BeanUtils;
import com.bcms.entity.GenderEnum;
import com.bcms.entity.User;
import com.bcms.entity.UserStateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Jackie
 * @className UserVO
 * @descrition User视图对象，封装后端返回给前端的数据
 * @date 2019/4/11 10:18
 */
@Data
@NoArgsConstructor
public class UserVO {
    /**
     * id
     */
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 登录帐号
     */
    private String account;
    /**
     * 微信用户的唯一标识
     */
    private String openId;
    /**
     * 状态
     */
    private UserStateEnum state;
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
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;
    /**
     * 身份证号码
     */
    private String identityCard;
    /**
     * 用户头像
     */
    private String profilePhoto;
    /**
     * 性别
     */
    private GenderEnum gender;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 语言
     */
    private String language;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 住址
     */
    private String address;

    public UserVO(User user) {
        BeanUtils.copyProperties(user, this);
    }
}

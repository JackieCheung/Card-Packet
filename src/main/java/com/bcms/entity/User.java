package com.bcms.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Jackie
 * @className User
 * @descrition 用户实体类，默认继承策略为单表
 * @date 2018/12/23 22:53
 */
@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "t_user")
@JsonIgnoreProperties(value = {"cards"})
public class User implements Serializable {
    /**
     * uuid
     */
    private static final long serialVersionUID = -599302865579046795L;
    /**
     * id
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    /**
     * 用户名
     */
    @Column(nullable = false)
    protected String name;
    /**
     * 昵称
     */
    protected String nickName;
    /**
     * 登录帐号
     */
    @Column(unique = true)
    protected String account;
    /**
     * 微信用户的唯一标识
     */
    @Column(unique = true, nullable = false)
    protected String openId;
    /**
     * 所拥有的卡
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    protected List<Card> cards;
    /**
     * 密码，反序列化时只可写，序列化时不可读
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String password;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    protected UserStateEnum state;
    /**
     * 创建时间
     */
    protected Date createTime;
    /**
     * 更新时间
     */
    protected Date updateTime;
    /**
     * 最后登录时间
     */
    protected Date lastLoginTime;
    /**
     * 身份证号码
     */
    protected String identityCard;
    /**
     * 用户头像
     */
    protected String profilePhoto;
    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    protected GenderEnum gender;
    /**
     * 年龄
     */
    protected Integer age;
    /**
     * 出生日期
     */
    @Temporal(TemporalType.DATE)
    protected Date birthday;
    /**
     * 手机号码
     */
    protected String phone;
    /**
     * 邮箱
     */
    protected String email;
    /**
     * 语言
     */
    protected String language;
    /**
     * 国家
     */
    protected String country;
    /**
     * 省份
     */
    protected String province;
    /**
     * 城市
     */
    protected String city;
    /**
     * 住址
     */
    protected String address;
    /**
     * 子类的鉴别器，存储关联的子类名称，用于区分属于不同子类类型的行
     */
    @Column(name = "dtype", insertable = false, updatable = false)
    protected String type;

    @PrePersist
    protected void initializeUser() {
        // 初始化用户信息
        this.setState(UserStateEnum.ENABLED);
        this.setName("wx_user");
        this.setNickName("微信用户");
        this.setPassword("123456");
        this.updateTime = this.createTime = new Date();
    }

    @PreUpdate
    protected void updateUser() {
        this.updateTime = new Date();
    }
}

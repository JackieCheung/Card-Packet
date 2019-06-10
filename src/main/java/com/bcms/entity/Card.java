package com.bcms.entity;

import com.bcms.common.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jackie
 * @className Card
 * @descrition 卡类
 * @date 2019/3/5 13:28
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "t_card")
public class Card implements Serializable {
    /**
     * uuid
     */
    private static final long serialVersionUID = -3790088273161687962L;
    /**
     * id
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 卡号
     */
    @Column(unique = true, nullable = false)
    private String number;
    /**
     * 所属组织机构/发卡行
     */
    @Column(nullable = false)
    private String org;
    /**
     * 卡名，银行卡的卡名默认为所属组织机构+卡类型
     */
    private String name;
    /**
     * 卡类型
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardTypeEnum type;
    /**
     * 有效期（信用卡）
     */
    private String validDate;
    /**
     * 备注
     */
    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(columnDefinition = "TEXT")
    private String remark;
    /**
     * 持卡人
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * 余额
     */
    @Column(columnDefinition = "double(10,2) default '0.00'")
    private Double balance;
    /**
     * 密码，长度限制为6位
     */
    @Column(length = 6)
    private String password;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 卡图片
     */
    private String imageUrl;

    @PrePersist
    protected void initializeCard() {
        this.updateTime = this.createTime = new Date();
    }

    @PreUpdate
    protected void updateCard() {
        this.updateTime = new Date();
    }
}

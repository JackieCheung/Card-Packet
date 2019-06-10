package com.bcms.entity;

/**
 * @className CardTypeEnum
 * @descrition 卡类型枚举类
 * @author Jackie
 * @date 2019/3/5 13:54
 */
public enum CardTypeEnum {
    /**
     * 借记卡
     */
    DEBIT,
    /**
     * 信用卡
     */
    CREDIT,
//    /**
//     * 理财卡
//     */
//    WEALTH,
    /**
     * 其他
     */
    OTHER,
    /**
     * 无法识别、无
     */
    UNRECOGNIZABLE
}

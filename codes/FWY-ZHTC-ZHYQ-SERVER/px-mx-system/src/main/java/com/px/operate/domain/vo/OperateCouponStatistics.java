package com.px.operate.domain.vo;


import lombok.Data;

@Data
public class OperateCouponStatistics {
    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 总数量
     */
    private Integer totalQuantity;

    /**
     * 已发放数量
     */
    private Integer issuedQuantity;

    /**
     * 已领取数量
     */
    private Integer receivedQuantity;

    /**
     * 已使用数量
     */
    private Integer usedQuantity;

    /**
     * 剩余数量
     */
    private Integer surplusQuantity;

    /**
     * 已发放面额（元）
     */
    private Double  issuedMoney;

    /**
     * 已领取面额（元）
     */
    private Double receivedMoney;

    /**
     * 已使用面额（元）
     */
    private Double usedMoney;
}

package com.px.operate.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class PendPayOrderRequestVo {
    /**
     * 查询车牌
     */
    private String licensePlateNumber;
    /**
     * 要查询的状态集合
     */
    private List<Integer> status;
    /**
     * 状态字符串
     */
    private String statusStr;
    /**
     * 单号
     */
    private String orderSn;
}

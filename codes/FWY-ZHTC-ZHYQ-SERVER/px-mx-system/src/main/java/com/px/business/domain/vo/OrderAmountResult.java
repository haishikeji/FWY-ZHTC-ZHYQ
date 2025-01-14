package com.px.business.domain.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderAmountResult {
    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单实付金额
     */
    private BigDecimal orderRealityAmount;

    /**
     * 停车时长
     */
    private Long duration;
}

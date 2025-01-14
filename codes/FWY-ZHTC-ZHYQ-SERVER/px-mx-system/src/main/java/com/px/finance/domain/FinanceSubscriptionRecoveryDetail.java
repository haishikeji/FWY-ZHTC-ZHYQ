
package com.px.finance.domain;

import com.px.common.annotation.Excel;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.core.domain.BaseEntity;

/**
 * 催缴明细对象 bus_finance_subscription_recovery_detail
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
@TableName("bus_finance_subscription_recovery_detail")
public class FinanceSubscriptionRecoveryDetail extends BaseEntity {

private static final long serialVersionUID=1L;

    /** 明细ID */
    private Long subscriptionRecoveryDetailId;

    /** 催缴ID */
            @Excel(name = "催缴ID")
    private Long subscriptionRecoveryId;

    /** 订单号 */
            @Excel(name = "订单号")
    private String orderSn;

    /** 是否缴费（0=否；1=是） */
            @Excel(name = "是否缴费", readConverterExp = "0==否；1=是")
    private Integer isPayment;

}

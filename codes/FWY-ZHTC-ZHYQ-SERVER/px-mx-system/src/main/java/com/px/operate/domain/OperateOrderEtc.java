
package com.px.operate.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.common.annotation.Excel;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.core.domain.BaseEntity;

/**
 * ETC欠费订单审核对象 bus_operate_order_etc
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
@TableName("bus_operate_order_etc")
public class OperateOrderEtc extends BaseEntity {

private static final long serialVersionUID=1L;

    /** ID */
    private Long orderEtcId;

    /** 用户 */
            @Excel(name = "用户")
    private Long userId;

    /** 车牌号码 */
            @Excel(name = "车牌号码")
    private String licencePlateSn;

    /** 订单ID */
            @Excel(name = "订单ID")
    private String orderId;

    /** 审核时间 */
            @JsonFormat(pattern = "yyyy-MM-dd")
            @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkTime;

    /** 审核状态 */
            @Excel(name = "审核状态")
    private Integer checkStatus;

    /** 审核人 */
            @Excel(name = "审核人")
    private String checkBy;

}

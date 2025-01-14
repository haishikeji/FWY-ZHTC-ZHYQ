
package com.px.operate.domain;

import com.px.common.annotation.Excel;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.core.domain.BaseEntity;

/**
 * 诺诺token信息对象 bus_operate_invoice_token
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
@TableName("bus_operate_invoice_token")
public class OperateInvoiceToken extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 发票tokenID
     */
    private Long invoiceTokenId;

    /**
     * token值
     */
    @Excel(name = "token值")
    private String accessToken;

    /**
     * token的有效时间
     */
    @Excel(name = "token的有效时间")
    private Integer expiresIn;

}

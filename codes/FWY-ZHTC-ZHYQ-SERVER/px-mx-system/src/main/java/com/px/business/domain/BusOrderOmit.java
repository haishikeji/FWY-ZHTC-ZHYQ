
package com.px.business.domain;

import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseVO;
import lombok.Data;

/**
 * 巡检遗漏报对象 bus_order_omit
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
public class BusOrderOmit extends BaseVO {

    /** 停车场 */
    private Long parkAreaId;

    /** 停车场 */
    @Excel(name = "停车场")
    private String parkAreaName;


    /** 遗漏总订单数（笔） */
    @Excel(name = "遗漏总订单数", readConverterExp = "笔")
    private Long missingOrderNumber;

    /** 遗漏次数最高泊位 */
    @Excel(name = "遗漏次数最高泊位")
    private String parkAreaBerthNames;

}

















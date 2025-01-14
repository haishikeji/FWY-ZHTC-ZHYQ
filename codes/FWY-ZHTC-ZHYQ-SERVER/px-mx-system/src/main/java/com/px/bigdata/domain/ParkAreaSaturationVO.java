package com.px.bigdata.domain;

import com.px.common.core.domain.BaseVO;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 车场饱和度
 */
@Data
public class ParkAreaSaturationVO extends BaseVO {

    private Long parkAreaId;

    /** 经度 */
    private BigDecimal lng;

    /** 纬度 */
    private BigDecimal lat;

    private Long parkAreaTypeId;
    /**
     * 车场名称
     */
    private String parkAreaName;

    /**
     * 总泊位数
     */
    private Integer berthNumber;

    /**
     * 泊位使用数量
     */
    private Integer berthUseNumber;

    /**
     * 车场饱和度
     */
    private BigDecimal parkAreaSaturation;
    private String parkAreaSaturationStr;
}


package com.px.operate.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.common.annotation.Excel;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.core.domain.BaseEntity;

/**
 * 访客车对象 bus_operate_visitor_car
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
@TableName("bus_operate_visitor_car")
public class OperateVisitorCar extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 特殊车ID
     */
    private Long visitorCarId;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String userName;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String phoneNumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 状态(0-正常/1-停用)
     */
    @Excel(name = "状态(0-正常/1-停用)")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

}

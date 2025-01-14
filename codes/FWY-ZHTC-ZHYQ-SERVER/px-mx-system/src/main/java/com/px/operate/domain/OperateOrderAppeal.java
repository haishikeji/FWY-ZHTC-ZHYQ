
package com.px.operate.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.common.annotation.Excel;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.core.domain.BaseEntity;

/**
 * 订单申诉对象 bus_operate_order_appeal
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
@TableName("bus_operate_order_appeal")
public class OperateOrderAppeal extends BaseEntity {

    private static final long serialVersionUID = 1L;

//    /**
//     * ID
//     */
//    private Long orderAppealId;
//
//    /**
//     * 用户
//     */
//    @Excel(name = "用户")
//    private Long userId;
//
//    /**
//     * 车牌号码
//     */
//    @Excel(name = "车牌号码")
//    private String licencePlateSn;
//
//    /**
//     * 订单ID
//     */
//    @Excel(name = "订单ID")
//    private String orderId;
//
//    /**
//     * 订单号
//     */
//    private String orderSn;
//
//    /**
//     * 订单类型:1=临停订单,2=包月订单
//     */
//    @Excel(name = "订单类型:1=临停订单,2=包月订单")
//    private Integer orderType;
//
//    /**
//     * 问题类型
//     */
//    @Excel(name = "问题类型")
//    private Integer questionType;
//
//    /**
//     * 审核时间
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
//    private Date checkTime;
//
//    /**
//     * 审核人
//     */
//    @Excel(name = "审核人")
//    private String checkBy;
//
//    /**
//     * 审核状态
//     */
//    @Excel(name = "审核状态")
//    private Integer checkStatus;
//
//    /**
//     * 用户名
//     */
//    @Excel(name = "用户名")
//    private String userNickName;
//
//    /**
//     * 联系电话
//     */
//    @Excel(name = "联系电话")
//    private String phonenumber;
//
//    /**
//     * 审核者
//     */
//    @Excel(name = "审核者")
//    private String adminNickName;


    /** ID */
    private Long orderAppealId;

    /** 用户 */
    @Excel(name = "用户")
    private Long userId;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phoneNumber;

    /** 车牌号码 */
    @Excel(name = "车牌号码")
    private String licensePlateNumber;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderSn;

    /** 订单类型:1=临停订单,2=包月订单 */
    @Excel(name = "订单类型:1=临停订单,2=包月订单")
    private String orderType;

    /** 问题类型 */
    @Excel(name = "问题类型")
    private String questionType;

    /** 审核人 */
    @Excel(name = "审核人")
    private String checkBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    /** 审核说明 */
    @Excel(name = "审核说明")
    private String checkRemark;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String checkStatus;

}

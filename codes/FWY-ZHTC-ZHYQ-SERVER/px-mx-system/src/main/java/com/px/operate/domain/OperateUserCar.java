
package com.px.operate.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.px.common.annotation.Excel;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.core.domain.BaseEntity;

/**
 * 会员车辆对象 bus_operate_user_car
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
@TableName("bus_operate_user_car")
public class OperateUserCar extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long userCarId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 抬头类型(1=蓝牌,2=新能源,3=黄牌,4=其他)
     */
    @Excel(name = "抬头类型(1=蓝牌,2=新能源,3=黄牌,4=其他)")
    private Integer carType;

    /**
     * 车牌号码
     */
    @Excel(name = "车牌号码")
    private String licensePlateNumber;

    /**
     * 是否默认
     */
    @Excel(name = "是否默认")
    private Integer isDef;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    //*********************附加字段**************************//
    @TableField(exist = false)
    private String keyWords;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String phoneNumber;
}

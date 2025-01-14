package com.px.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseEntity;

/**
 * 停车场设备对象 bus_park_area_device
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class BusParkAreaDevice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 停车场设备ID
     */
    private Long parkAreaDeviceId;

    /**
     * 停车场ID
     */
    @Excel(name = "停车场ID")
    private Long parkAreaId;

    /**
     * 类型(1-匝道/2-相机)
     */
    @Excel(name = "类型(1-匝道/2-相机)")
    private Integer type;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * 设备编号
     */
    @Excel(name = "设备编号")
    private String deviceCode;

    /**
     * 设备条形码
     */
    @Excel(name = "设备条形码")
    private String barCode;

    /**
     * 品牌
     */
    @Excel(name = "品牌")
    private String brand;

    /**
     * 型号
     */
    @Excel(name = "型号")
    private String model;

    /**
     * 状态(0-正常/1-已下线/2-故障维护/3-占用)
     */
    @Excel(name = "状态(0-正常/1-已下线/2-故障维护/3-占用)")
    private String status;

    /**
     * 最近上线时间
     */
    @Excel(name = "最近上线时间")
    private Long lastOnlineTime;

    /**
     * 产品id
     */
    @Excel(name = "产品id")
    private String productId;

    /**
     * 方向
     */
    @Excel(name = "方向")
    private String direction;

    /**
     * 设备mac地址
     */
    @Excel(name = "设备mac地址")
    private String macAddress;

    /**
     * 设备视频流地址
     */
    @Excel(name = "设备视频流地址")
    private String videoAddress;

    /**
     * 设备视频回放地址
     */
    @Excel(name = "设备视频回放地址")
    private String historyVideoAddress;

    /**
     * 设备登录用户名
     */
    @Excel(name = "设备登录用户名")
    private String username;

    /**
     * 设备登录密码
     */
    @Excel(name = "设备登录密码")
    private String password;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    public void setParkAreaDeviceId(Long parkAreaDeviceId) {
        this.parkAreaDeviceId = parkAreaDeviceId;
    }

    public Long getParkAreaDeviceId() {
        return parkAreaDeviceId;
    }

    public void setParkAreaId(Long parkAreaId) {
        this.parkAreaId = parkAreaId;
    }

    public Long getParkAreaId() {
        return parkAreaId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setLastOnlineTime(Long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Long getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    public String getVideoAddress() {
        return videoAddress;
    }

    public void setHistoryVideoAddress(String historyVideoAddress) {
        this.historyVideoAddress = historyVideoAddress;
    }

    public String getHistoryVideoAddress() {
        return historyVideoAddress;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    //*********************附加字段**************************//
    private String parkAreaName;

    public String getParkAreaName() {
        return parkAreaName;
    }

    public void setParkAreaName(String parkAreaName) {
        this.parkAreaName = parkAreaName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("parkAreaDeviceId", getParkAreaDeviceId())
                .append("parkAreaId", getParkAreaId())
                .append("type", getType())
                .append("deviceName", getDeviceName())
                .append("deviceCode", getDeviceCode())
                .append("barCode", getBarCode())
                .append("brand", getBrand())
                .append("model", getModel())
                .append("status", getStatus())
                .append("lastOnlineTime", getLastOnlineTime())
                .append("productId", getProductId())
                .append("direction", getDirection())
                .append("macAddress", getMacAddress())
                .append("videoAddress", getVideoAddress())
                .append("historyVideoAddress", getHistoryVideoAddress())
                .append("username", getUsername())
                .append("password", getPassword())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("parkAreaName", getParkAreaName())
                .toString();
    }
}

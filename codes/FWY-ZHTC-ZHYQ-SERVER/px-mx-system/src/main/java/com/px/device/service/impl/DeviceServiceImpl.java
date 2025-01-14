package com.px.device.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.px.common.utils.DateUtils;
import com.px.common.utils.StringUtils;
import com.px.device.domain.DeviceBerth;
import com.px.device.mapper.DeviceBerthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.device.mapper.DeviceMapper;
import com.px.device.domain.Device;
import com.px.device.service.IDeviceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class DeviceServiceImpl extends MPJBaseServiceImpl<DeviceMapper, Device> implements IDeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceBerthMapper deviceBerthMapper;

    /**
     * 查询设备
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    @Override
    public Device selectDeviceByDeviceId(Long deviceId) {
        return deviceMapper.selectDeviceByDeviceId(deviceId);
    }

    /**
     * 查询设备列表
     *
     * @param device 设备
     * @return 设备
     */
    @Override
    public List<Device> selectDeviceList(Device device) {
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 新增设备
     *
     * @param device 设备
     * @return 结果
     */
    @Override
    @Transactional
    public int insertDevice(Device device) {
        device.setCreateTime(DateUtils.getNowDate());
        int rows = deviceMapper.insertDevice(device);
        //新增设备与泊位管理
        insertDeviceBerth(device.getDeviceId(),device.getParkAreaBerthIds());
        return rows;
    }

    /**
     * 修改设备
     *
     * @param device 设备
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDevice(Device device) {
        device.setUpdateTime(DateUtils.getNowDate());
        //删除设备与泊位管理
        deviceBerthMapper.deleteDeviceBerthByDeviceId(device.getDeviceId());
        //新增设备与泊位管理
        insertDeviceBerth(device.getDeviceId(),device.getParkAreaBerthIds());
        return deviceMapper.updateDevice(device);
    }

    /**
     * 批量删除设备
     *
     * @param deviceIds 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByDeviceIds(Long[] deviceIds) {
        return deviceMapper.deleteDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除设备信息
     *
     * @param deviceId 设备主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByDeviceId(Long deviceId) {
        return deviceMapper.deleteDeviceByDeviceId(deviceId);
    }

    @Override
    public Device selectDeviceByCode(String code) {
        return deviceMapper.selectDeviceByCode(code);
    }


    public void insertDeviceBerth(Device device)
    {
        this.insertDeviceBerth(device.getDeviceId(), device.getParkAreaBerthIds());
    }
    public void insertDeviceBerth(Long deviceId, Long[] parkAreaBerthIds)
    {
        if (StringUtils.isNotEmpty(parkAreaBerthIds))
        {
            // 新增设备与泊位管理
            List<DeviceBerth> list = new ArrayList<DeviceBerth>(parkAreaBerthIds.length);
            for (Long berthId : parkAreaBerthIds)
            {
                DeviceBerth ur = new DeviceBerth();
                ur.setDeviceId(deviceId);
                ur.setParkAreaBerthId(berthId);
                list.add(ur);
            }
            deviceBerthMapper.batchDeviceBerth(list);
        }
    }
}

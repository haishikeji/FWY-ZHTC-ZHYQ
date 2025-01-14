
package com.px.device.service;

import java.util.List;

import com.github.yulichang.base.MPJBaseService;
import com.px.device.domain.DeviceBerth;

/**
 * 设备-泊位关联Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IDeviceBerthService extends MPJBaseService<DeviceBerth>
{
    /**
     * 查询设备-泊位关联
     *
     * @param deviceId 设备-泊位关联主键
     * @return 设备-泊位关联
     */
    public DeviceBerth selectDeviceBerthByDeviceId(Long deviceId);

    /**
     * 查询设备-泊位关联列表
     *
     * @param deviceBerth 设备-泊位关联
     * @return 设备-泊位关联集合
     */
    public List<DeviceBerth> selectDeviceBerthList(DeviceBerth deviceBerth);

    /**
     * 新增设备-泊位关联
     *
     * @param deviceBerth 设备-泊位关联
     * @return 结果
     */
    public int insertDeviceBerth(DeviceBerth deviceBerth);

    /**
     * 修改设备-泊位关联
     *
     * @param deviceBerth 设备-泊位关联
     * @return 结果
     */
    public int updateDeviceBerth(DeviceBerth deviceBerth);

    /**
     * 批量删除设备-泊位关联
     *
     * @param deviceIds 需要删除的设备-泊位关联主键集合
     * @return 结果
     */
    public int deleteDeviceBerthByDeviceIds(Long[] deviceIds);

    /**
     * 删除设备-泊位关联信息
     *
     * @param deviceId 设备-泊位关联主键
     * @return 结果
     */
    public int deleteDeviceBerthByDeviceId(Long deviceId);
}

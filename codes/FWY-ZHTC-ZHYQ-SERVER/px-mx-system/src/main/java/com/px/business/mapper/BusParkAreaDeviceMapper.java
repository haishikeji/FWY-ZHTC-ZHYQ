package com.px.business.mapper;

import java.util.List;

import com.github.yulichang.base.MPJBaseMapper;
import com.px.business.domain.BusParkAreaDevice;

/**
 * 停车场设备Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface BusParkAreaDeviceMapper extends MPJBaseMapper<BusParkAreaDevice>
{
    /**
     * 查询停车场设备
     *
     * @param parkAreaDeviceId 停车场设备主键
     * @return 停车场设备
     */
    public BusParkAreaDevice selectBusParkAreaDeviceByParkAreaDeviceId(Long parkAreaDeviceId);

    /**
     * 查询停车场设备列表
     *
     * @param busParkAreaDevice 停车场设备
     * @return 停车场设备集合
     */
    public List<BusParkAreaDevice> selectBusParkAreaDeviceList(BusParkAreaDevice busParkAreaDevice);

    /**
     * 新增停车场设备
     *
     * @param busParkAreaDevice 停车场设备
     * @return 结果
     */
    public int insertBusParkAreaDevice(BusParkAreaDevice busParkAreaDevice);

    /**
     * 修改停车场设备
     *
     * @param busParkAreaDevice 停车场设备
     * @return 结果
     */
    public int updateBusParkAreaDevice(BusParkAreaDevice busParkAreaDevice);

    /**
     * 删除停车场设备
     *
     * @param parkAreaDeviceId 停车场设备主键
     * @return 结果
     */
    public int deleteBusParkAreaDeviceByParkAreaDeviceId(Long parkAreaDeviceId);

    /**
     * 批量删除停车场设备
     *
     * @param parkAreaDeviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusParkAreaDeviceByParkAreaDeviceIds(Long[] parkAreaDeviceIds);
}

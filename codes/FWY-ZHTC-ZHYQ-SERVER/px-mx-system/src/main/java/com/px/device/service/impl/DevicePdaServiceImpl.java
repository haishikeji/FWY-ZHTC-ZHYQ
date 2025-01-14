package com.px.device.service.impl;

import java.util.List;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.px.common.constant.UserConstants;
import com.px.common.utils.DateUtils;
import com.px.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.device.mapper.DevicePdaMapper;
import com.px.device.domain.DevicePda;
import com.px.device.service.IDevicePdaService;

/**
 * PDA设备Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class DevicePdaServiceImpl extends MPJBaseServiceImpl<DevicePdaMapper, DevicePda> implements IDevicePdaService
{
    @Autowired
    private DevicePdaMapper devicePdaMapper;

    /**
     * 查询PDA设备
     *
     * @param devicePdaId PDA设备主键
     * @return PDA设备
     */
    @Override
    public DevicePda selectDevicePdaBydevicePdaId(Long devicePdaId)
    {
        return devicePdaMapper.selectDevicePdaBydevicePdaId(devicePdaId);
    }

    /**
     * 查询PDA设备列表
     *
     * @param devicePda PDA设备
     * @return PDA设备
     */
    @Override
    public List<DevicePda> selectDevicePdaList(DevicePda devicePda)
    {
        return devicePdaMapper.selectDevicePdaList(devicePda);
    }

    /**
     * 新增PDA设备
     *
     * @param devicePda PDA设备
     * @return 结果
     */
    @Override
    public int insertDevicePda(DevicePda devicePda)
    {
                devicePda.setCreateTime(DateUtils.getNowDate());
            return devicePdaMapper.insertDevicePda(devicePda);
    }

    /**
     * 修改PDA设备
     *
     * @param devicePda PDA设备
     * @return 结果
     */
    @Override
    public int updateDevicePda(DevicePda devicePda)
    {
                devicePda.setUpdateTime(DateUtils.getNowDate());
        return devicePdaMapper.updateDevicePda(devicePda);
    }

    /**
     * 批量删除PDA设备
     *
     * @param devicePdaIds 需要删除的PDA设备主键
     * @return 结果
     */
    @Override
    public int deleteDevicePdaBydevicePdaIds(Long[] devicePdaIds)
    {
        return devicePdaMapper.deleteDevicePdaBydevicePdaIds(devicePdaIds);
    }

    /**
     * 删除PDA设备信息
     *
     * @param devicePdaId PDA设备主键
     * @return 结果
     */
    @Override
    public int deleteDevicePdaBydevicePdaId(Long devicePdaId)
    {
        return devicePdaMapper.deleteDevicePdaBydevicePdaId(devicePdaId);
    }

    @Override
    public boolean checkCodeUnique(DevicePda devicePda) {
        Long userId = StringUtils.isNull(devicePda.getDevicePdaId()) ? -1L : devicePda.getDevicePdaId();
        DevicePda info = devicePdaMapper.checkCodeUnique(devicePda.getCode());
        if (StringUtils.isNotNull(info) && info.getDevicePdaId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}

package com.px.third.service.impl;

import java.util.List;

import com.github.yulichang.base.MPJBaseServiceImpl;
        import com.px.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.third.mapper.ThirdPatrolVehicleReportDataMapper;
import com.px.third.domain.ThirdPatrolVehicleReportData;
import com.px.third.service.IThirdPatrolVehicleReportDataService;

/**
 * 第三方巡逻车上报数据Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class ThirdPatrolVehicleReportDataServiceImpl extends MPJBaseServiceImpl<ThirdPatrolVehicleReportDataMapper, ThirdPatrolVehicleReportData> implements IThirdPatrolVehicleReportDataService
{
    @Autowired
    private ThirdPatrolVehicleReportDataMapper thirdPatrolVehicleReportDataMapper;

    /**
     * 查询第三方巡逻车上报数据
     *
     * @param dataId 第三方巡逻车上报数据主键
     * @return 第三方巡逻车上报数据
     */
    @Override
    public ThirdPatrolVehicleReportData selectThirdPatrolVehicleReportDataByDataId(Long dataId)
    {
        return thirdPatrolVehicleReportDataMapper.selectThirdPatrolVehicleReportDataByDataId(dataId);
    }

    /**
     * 查询第三方巡逻车上报数据列表
     *
     * @param thirdPatrolVehicleReportData 第三方巡逻车上报数据
     * @return 第三方巡逻车上报数据
     */
    @Override
    public List<ThirdPatrolVehicleReportData> selectThirdPatrolVehicleReportDataList(ThirdPatrolVehicleReportData thirdPatrolVehicleReportData)
    {
        return thirdPatrolVehicleReportDataMapper.selectThirdPatrolVehicleReportDataList(thirdPatrolVehicleReportData);
    }

    /**
     * 新增第三方巡逻车上报数据
     *
     * @param thirdPatrolVehicleReportData 第三方巡逻车上报数据
     * @return 结果
     */
    @Override
    public int insertThirdPatrolVehicleReportData(ThirdPatrolVehicleReportData thirdPatrolVehicleReportData)
    {
                thirdPatrolVehicleReportData.setCreateTime(DateUtils.getNowDate());
            return thirdPatrolVehicleReportDataMapper.insertThirdPatrolVehicleReportData(thirdPatrolVehicleReportData);
    }

    /**
     * 修改第三方巡逻车上报数据
     *
     * @param thirdPatrolVehicleReportData 第三方巡逻车上报数据
     * @return 结果
     */
    @Override
    public int updateThirdPatrolVehicleReportData(ThirdPatrolVehicleReportData thirdPatrolVehicleReportData)
    {
                thirdPatrolVehicleReportData.setUpdateTime(DateUtils.getNowDate());
        return thirdPatrolVehicleReportDataMapper.updateThirdPatrolVehicleReportData(thirdPatrolVehicleReportData);
    }

    /**
     * 批量删除第三方巡逻车上报数据
     *
     * @param dataIds 需要删除的第三方巡逻车上报数据主键
     * @return 结果
     */
    @Override
    public int deleteThirdPatrolVehicleReportDataByDataIds(Long[] dataIds)
    {
        return thirdPatrolVehicleReportDataMapper.deleteThirdPatrolVehicleReportDataByDataIds(dataIds);
    }

    /**
     * 删除第三方巡逻车上报数据信息
     *
     * @param dataId 第三方巡逻车上报数据主键
     * @return 结果
     */
    @Override
    public int deleteThirdPatrolVehicleReportDataByDataId(Long dataId)
    {
        return thirdPatrolVehicleReportDataMapper.deleteThirdPatrolVehicleReportDataByDataId(dataId);
    }

    @Override
    public ThirdPatrolVehicleReportData selectLastBerthReportData(String spaceNumber) {
        return thirdPatrolVehicleReportDataMapper.selectLastBerthReportData(spaceNumber);
    }

    @Override
    public ThirdPatrolVehicleReportData selectThirdPatrolVehicleReportDataByTaskId(String taskId) {
        return thirdPatrolVehicleReportDataMapper.selectThirdPatrolVehicleReportDataByTaskId(taskId);
    }
}

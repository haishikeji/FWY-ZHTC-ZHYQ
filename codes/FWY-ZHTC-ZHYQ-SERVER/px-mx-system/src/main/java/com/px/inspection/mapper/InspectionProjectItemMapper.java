package com.px.inspection.mapper;


import com.github.yulichang.base.MPJBaseMapper;
import com.px.inspection.domain.InspectionProjectItem;

import java.util.List;

/**
 * 【巡检任务检查项】Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface InspectionProjectItemMapper extends MPJBaseMapper<InspectionProjectItem>
{
    /**
     * 查询【巡检任务检查项】
     *
     * @param itemId 【巡检任务检查项】主键
     * @return 【巡检任务检查项】
     */
    public InspectionProjectItem selectInspectionProjectItemByItemId(Long itemId);

    public List<InspectionProjectItem> selectInspectionProjectItemByProjectSiteId(Long projectSiteId);

    /**
     * 查询【巡检任务检查项】列表
     *
     * @param inspectionProjectItem 【巡检任务检查项】
     * @return 【巡检任务检查项】集合
     */
    public List<InspectionProjectItem> selectInspectionProjectItemList(InspectionProjectItem inspectionProjectItem);

    /**
     * 新增【巡检任务检查项】
     *
     * @param inspectionProjectItem 【巡检任务检查项】
     * @return 结果
     */
    public int insertInspectionProjectItem(InspectionProjectItem inspectionProjectItem);

    /**
     * 修改【巡检任务检查项】
     *
     * @param inspectionProjectItem 【巡检任务检查项】
     * @return 结果
     */
    public int updateInspectionProjectItem(InspectionProjectItem inspectionProjectItem);

    /**
     * 删除【巡检任务检查项】
     *
     * @param itemId 【巡检任务检查项】主键
     * @return 结果
     */
    public int deleteInspectionProjectItemByItemId(Long itemId);

    /**
     * 批量删除【巡检任务检查项】
     *
     * @param itemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInspectionProjectItemByItemIds(Long[] itemIds);

    /**
     * 批量新增巡检任务检查项信息
     *
     * @param projectItemList 检查项列表
     * @return 结果
     */
    public int batchProjectItem(List<InspectionProjectItem> projectItemList);
}

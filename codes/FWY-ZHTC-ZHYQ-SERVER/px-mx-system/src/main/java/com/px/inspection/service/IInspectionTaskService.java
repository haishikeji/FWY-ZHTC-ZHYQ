
package com.px.inspection.service;

import java.util.List;

import com.github.yulichang.base.MPJBaseService;
import com.px.inspection.domain.InspectionTask;

/**
 * 巡检任务Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IInspectionTaskService extends MPJBaseService<InspectionTask>
{
    /**
     * 查询巡检任务
     *
     * @param taskId 巡检任务主键
     * @return 巡检任务
     */
    public InspectionTask selectInspectionTaskByTaskId(Long taskId);

    /**
     * 查询巡检任务列表
     *
     * @param inspectionTask 巡检任务
     * @return 巡检任务集合
     */
    public List<InspectionTask> selectInspectionTaskList(InspectionTask inspectionTask);

    public List<InspectionTask> selectInspectionTaskListDsec(InspectionTask inspectionTask);

    public List<InspectionTask> selectInspectionTaskListAsc(InspectionTask inspectionTask);

    /**
     * 新增巡检任务
     *
     * @param inspectionTask 巡检任务
     * @return 结果
     */
    public int insertInspectionTask(InspectionTask inspectionTask);

    /**
     * 修改巡检任务
     *
     * @param inspectionTask 巡检任务
     * @return 结果
     */
    public int updateInspectionTask(InspectionTask inspectionTask);

    /**
     * 批量删除巡检任务
     *
     * @param taskIds 需要删除的巡检任务主键集合
     * @return 结果
     */
    public int deleteInspectionTaskByTaskIds(Long[] taskIds);

    /**
     * 删除巡检任务信息
     *
     * @param taskId 巡检任务主键
     * @return 结果
     */
    public int deleteInspectionTaskByTaskId(Long taskId);

    public int handleTaskTimeout();
}

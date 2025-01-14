
package com.px.operate.service;

import java.util.List;

import com.github.yulichang.base.MPJBaseService;
import com.px.operate.domain.OperatePdaReportProblem;

/**
 * PDA上报问题Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IOperatePdaReportProblemService extends MPJBaseService<OperatePdaReportProblem>
{
    /**
     * 查询PDA上报问题
     *
     * @param reportProblemId PDA上报问题主键
     * @return PDA上报问题
     */
    public OperatePdaReportProblem selectOperatePdaReportProblemByReportProblemId(Long reportProblemId);

    /**
     * 查询PDA上报问题列表
     *
     * @param operatePdaReportProblem PDA上报问题
     * @return PDA上报问题集合
     */
    public List<OperatePdaReportProblem> selectOperatePdaReportProblemList(OperatePdaReportProblem operatePdaReportProblem);

    /**
     * 新增PDA上报问题
     *
     * @param operatePdaReportProblem PDA上报问题
     * @return 结果
     */
    public int insertOperatePdaReportProblem(OperatePdaReportProblem operatePdaReportProblem);

    /**
     * 修改PDA上报问题
     *
     * @param operatePdaReportProblem PDA上报问题
     * @return 结果
     */
    public int updateOperatePdaReportProblem(OperatePdaReportProblem operatePdaReportProblem);

    /**
     * 批量删除PDA上报问题
     *
     * @param reportProblemIds 需要删除的PDA上报问题主键集合
     * @return 结果
     */
    public int deleteOperatePdaReportProblemByReportProblemIds(Long[] reportProblemIds);

    /**
     * 删除PDA上报问题信息
     *
     * @param reportProblemId PDA上报问题主键
     * @return 结果
     */
    public int deleteOperatePdaReportProblemByReportProblemId(Long reportProblemId);

    public int checkOperatePdaReportProblem(OperatePdaReportProblem operatePdaReportProblem);
}

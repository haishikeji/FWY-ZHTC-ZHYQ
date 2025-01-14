
package com.px.business.service;

import java.util.List;

import com.github.yulichang.base.MPJBaseService;
import com.px.business.domain.BusTempFreeRule;
import com.px.business.domain.vo.CalculateFreeResult;
import com.px.business.domain.vo.CalculateTempFreeRule;

/**
 * 临停收费规则Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IBusTempFreeRuleService extends MPJBaseService<BusTempFreeRule>
{
    /**
     * 查询临停收费规则
     *
     * @param tempFreeRuleId 临停收费规则主键
     * @return 临停收费规则
     */
    public BusTempFreeRule selectBusTempFreeRuleByTempFreeRuleId(Long tempFreeRuleId);

    /**
     * 查询临停收费规则列表
     *
     * @param busTempFreeRule 临停收费规则
     * @return 临停收费规则集合
     */
    public List<BusTempFreeRule> selectBusTempFreeRuleList(BusTempFreeRule busTempFreeRule);

    /**
     * 新增临停收费规则
     *
     * @param busTempFreeRule 临停收费规则
     * @return 结果
     */
    public int insertBusTempFreeRule(BusTempFreeRule busTempFreeRule);

    /**
     * 修改临停收费规则
     *
     * @param busTempFreeRule 临停收费规则
     * @return 结果
     */
    public int updateBusTempFreeRule(BusTempFreeRule busTempFreeRule);

    /**
     * 批量删除临停收费规则
     *
     * @param tempFreeRuleIds 需要删除的临停收费规则主键集合
     * @return 结果
     */
    public int deleteBusTempFreeRuleByTempFreeRuleIds(Long[] tempFreeRuleIds);

    /**
     * 删除临停收费规则信息
     *
     * @param tempFreeRuleId 临停收费规则主键
     * @return 结果
     */
    public int deleteBusTempFreeRuleByTempFreeRuleId(Long tempFreeRuleId);

    public CalculateFreeResult calculateFree(CalculateTempFreeRule tempFreeRule);

    public BusTempFreeRule selectTempRuleByParkAreaId(Long parkAreaId);
}

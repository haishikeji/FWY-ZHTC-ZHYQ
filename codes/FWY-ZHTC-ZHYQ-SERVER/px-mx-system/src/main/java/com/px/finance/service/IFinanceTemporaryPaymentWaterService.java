
package com.px.finance.service;

import java.util.List;

import com.github.yulichang.base.MPJBaseService;
import com.px.finance.domain.FinanceTemporaryPaymentWater;

/**
 * 临停支付流水Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IFinanceTemporaryPaymentWaterService extends MPJBaseService<FinanceTemporaryPaymentWater>
{
    /**
     * 查询临停支付流水
     *
     * @param temporaryPaymentWaterId 临停支付流水主键
     * @return 临停支付流水
     */
    public FinanceTemporaryPaymentWater selectFinanceTemporaryPaymentWaterByTemporaryPaymentWaterId(Long temporaryPaymentWaterId);

    /**
     * 查询临停支付流水列表
     *
     * @param financeTemporaryPaymentWater 临停支付流水
     * @return 临停支付流水集合
     */
    public List<FinanceTemporaryPaymentWater> selectFinanceTemporaryPaymentWaterList(FinanceTemporaryPaymentWater financeTemporaryPaymentWater);

    /**
     * 新增临停支付流水
     *
     * @param financeTemporaryPaymentWater 临停支付流水
     * @return 结果
     */
    public int insertFinanceTemporaryPaymentWater(FinanceTemporaryPaymentWater financeTemporaryPaymentWater);

    /**
     * 修改临停支付流水
     *
     * @param financeTemporaryPaymentWater 临停支付流水
     * @return 结果
     */
    public int updateFinanceTemporaryPaymentWater(FinanceTemporaryPaymentWater financeTemporaryPaymentWater);

    /**
     * 批量删除临停支付流水
     *
     * @param temporaryPaymentWaterIds 需要删除的临停支付流水主键集合
     * @return 结果
     */
    public int deleteFinanceTemporaryPaymentWaterByTemporaryPaymentWaterIds(Long[] temporaryPaymentWaterIds);

    /**
     * 删除临停支付流水信息
     *
     * @param temporaryPaymentWaterId 临停支付流水主键
     * @return 结果
     */
    public int deleteFinanceTemporaryPaymentWaterByTemporaryPaymentWaterId(Long temporaryPaymentWaterId);
}

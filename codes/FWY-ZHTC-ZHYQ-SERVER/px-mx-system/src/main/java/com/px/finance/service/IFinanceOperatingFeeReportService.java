
package com.px.finance.service;

import java.util.List;

import com.github.yulichang.base.MPJBaseService;
import com.px.finance.domain.FinanceOperatingFeeReport;

/**
 * 运营缴费率报Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IFinanceOperatingFeeReportService extends MPJBaseService<FinanceOperatingFeeReport>
{
    /**
     * 查询运营缴费率报列表
     *
     * @param financeOperatingFeeReport 运营缴费率报
     * @return 运营缴费率报集合
     */
    public List<FinanceOperatingFeeReport> selectFinanceOperatingFeeReportList(FinanceOperatingFeeReport financeOperatingFeeReport);
}

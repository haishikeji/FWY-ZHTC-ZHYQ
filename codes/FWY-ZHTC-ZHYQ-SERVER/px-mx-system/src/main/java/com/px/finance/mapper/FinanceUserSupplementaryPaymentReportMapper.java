package com.px.finance.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.px.finance.domain.FinanceUserSupplementaryPaymentReport;

import java.util.List;

/**
 * 用户补缴报表Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface FinanceUserSupplementaryPaymentReportMapper extends MPJBaseMapper<FinanceUserSupplementaryPaymentReport>
{
    /**
     * 查询用户补缴报表列表
     *
     * @param financeUserSupplementaryPaymentReport 用户补缴报表
     * @return 用户补缴报表集合
     */
    public List<FinanceUserSupplementaryPaymentReport> selectFinanceUserSupplementaryPaymentReportList(FinanceUserSupplementaryPaymentReport financeUserSupplementaryPaymentReport);

}

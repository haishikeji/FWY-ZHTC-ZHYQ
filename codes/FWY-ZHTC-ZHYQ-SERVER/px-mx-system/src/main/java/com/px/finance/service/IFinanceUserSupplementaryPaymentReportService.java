package com.px.finance.service;

import com.github.yulichang.base.MPJBaseService;
import com.px.finance.domain.FinanceUserSupplementaryPaymentReport;

import java.util.List;

public interface IFinanceUserSupplementaryPaymentReportService extends MPJBaseService<FinanceUserSupplementaryPaymentReport> {
    public List<FinanceUserSupplementaryPaymentReport> selectFinanceUserSupplementaryPaymentReportList(FinanceUserSupplementaryPaymentReport financeUserSupplementaryPaymentReport);

}

package com.px.finance.service;

import com.github.yulichang.base.MPJBaseService;
import com.px.finance.domain.FinanceCollectionReport;

import java.util.List;

public interface IFinanceCollectionReportService extends MPJBaseService<FinanceCollectionReport> {
    public List<FinanceCollectionReport> selectFinanceCollectionReportList(FinanceCollectionReport financeCollectionReport);
}

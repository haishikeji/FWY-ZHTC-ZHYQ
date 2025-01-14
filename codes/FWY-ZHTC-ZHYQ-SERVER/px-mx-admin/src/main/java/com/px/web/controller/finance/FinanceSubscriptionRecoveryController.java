package com.px.web.controller.finance;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.px.common.annotation.Log;
import com.px.common.core.controller.BaseController;
import com.px.common.enums.BusinessType;
import com.px.finance.domain.FinanceSubscriptionRecovery;
import com.px.finance.service.IFinanceSubscriptionRecoveryService;
import com.px.common.utils.poi.ExcelUtil;
import com.px.common.core.page.TableDataInfo;

/**
 * 订阅消息追缴Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/finance/subscriptionRecovery")
public class FinanceSubscriptionRecoveryController extends BaseController
{
    @Autowired
    private IFinanceSubscriptionRecoveryService financeSubscriptionRecoveryService;

    /**
     * 查询订阅消息追缴列表
     */
    @PreAuthorize("@ss.hasPermi('finance:subscriptionRecovery:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinanceSubscriptionRecovery financeSubscriptionRecovery)
    {
        startPage();
        List<FinanceSubscriptionRecovery> list = financeSubscriptionRecoveryService.selectFinanceSubscriptionRecoveryList(financeSubscriptionRecovery);
        return getDataTable(list);
    }

    /**
     * 导出订阅消息追缴列表
     */
    @PreAuthorize("@ss.hasPermi('finance:subscriptionRecovery:export')")
    @Log(title = "订阅消息追缴", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinanceSubscriptionRecovery financeSubscriptionRecovery)
    {
        List<FinanceSubscriptionRecovery> list = financeSubscriptionRecoveryService.selectFinanceSubscriptionRecoveryList(financeSubscriptionRecovery);
        ExcelUtil<FinanceSubscriptionRecovery> util = new ExcelUtil<FinanceSubscriptionRecovery>(FinanceSubscriptionRecovery.class);
        util.exportExcel(response, list, "订阅消息追缴数据");
    }

//    /**
//     * 获取订阅消息追缴详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('finance:subscriptionRecovery:query')")
//    @GetMapping(value = "/{subscriptionRecoveryId}")
//    public AjaxResult getInfo(@PathVariable("subscriptionRecoveryId") Long subscriptionRecoveryId)
//    {
//        return success(financeSubscriptionRecoveryService.selectFinanceSubscriptionRecoveryBySubscriptionRecoveryId(subscriptionRecoveryId));
//    }
//
//    /**
//     * 新增订阅消息追缴
//     */
//    @PreAuthorize("@ss.hasPermi('finance:subscriptionRecovery:add')")
//    @Log(title = "订阅消息追缴", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    public AjaxResult add(@RequestBody FinanceSubscriptionRecovery financeSubscriptionRecovery)
//    {
//        return toAjax(financeSubscriptionRecoveryService.insertFinanceSubscriptionRecovery(financeSubscriptionRecovery));
//    }
//
//    /**
//     * 修改订阅消息追缴
//     */
//    @PreAuthorize("@ss.hasPermi('finance:subscriptionRecovery:edit')")
//    @Log(title = "订阅消息追缴", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    public AjaxResult edit(@RequestBody FinanceSubscriptionRecovery financeSubscriptionRecovery)
//    {
//        return toAjax(financeSubscriptionRecoveryService.updateFinanceSubscriptionRecovery(financeSubscriptionRecovery));
//    }
//
//    /**
//     * 删除订阅消息追缴
//     */
//    @PreAuthorize("@ss.hasPermi('finance:subscriptionRecovery:remove')")
//    @Log(title = "订阅消息追缴", businessType = BusinessType.DELETE)
//	@PostMapping("/delete/{subscriptionRecoveryIds}")
//    public AjaxResult remove(@PathVariable Long[] subscriptionRecoveryIds)
//    {
//        return toAjax(financeSubscriptionRecoveryService.deleteFinanceSubscriptionRecoveryBySubscriptionRecoveryIds(subscriptionRecoveryIds));
//    }
}

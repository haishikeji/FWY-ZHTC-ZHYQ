package com.px.web.controller.operate;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.px.operate.domain.vo.OperateCouponStatistics;
import com.px.operate.domain.vo.StatisticsBaseVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.px.common.annotation.Log;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import com.px.operate.domain.OperateCoupon;
import com.px.operate.service.IOperateCouponService;
import com.px.common.utils.poi.ExcelUtil;
import com.px.common.core.page.TableDataInfo;

/**
 * 优惠券Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/operate/coupon")
public class OperateCouponController extends BaseController
{
    @Autowired
    private IOperateCouponService operateCouponService;

    /**
     * 查询优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('operate:coupon:list')")
    @GetMapping("/page")
    public TableDataInfo page(OperateCoupon operateCoupon)
    {
        startPage();
        List<OperateCoupon> list = operateCouponService.selectOperateCouponList(operateCoupon);
        return getDataTable(list);
    }

    /**
     * 查询优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('operate:coupon:list')")
    @GetMapping("/list")
    public AjaxResult list(OperateCoupon operateCoupon)
    {
        List<OperateCoupon> list = operateCouponService.selectOperateCouponList(operateCoupon);
        return success(list);
    }

    /**
     * 导出优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('operate:coupon:export')")
    @Log(title = "优惠券", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OperateCoupon operateCoupon)
    {
        List<OperateCoupon> list = operateCouponService.selectOperateCouponList(operateCoupon);
        ExcelUtil<OperateCoupon> util = new ExcelUtil<OperateCoupon>(OperateCoupon.class);
        util.exportExcel(response, list, "优惠券数据");
    }

    /**
     * 获取优惠券详细信息
     */
    @PreAuthorize("@ss.hasPermi('operate:coupon:query')")
    @GetMapping(value = "/{couponId}")
    public AjaxResult getInfo(@PathVariable("couponId") Long couponId)
    {
        return success(operateCouponService.selectOperateCouponByCouponId(couponId));
    }

    /**
     * 新增优惠券
     */
    @PreAuthorize("@ss.hasPermi('operate:coupon:add')")
    @Log(title = "优惠券", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody OperateCoupon operateCoupon)
    {
        return toAjax(operateCouponService.insertOperateCoupon(operateCoupon));
    }

    /**
     * 修改优惠券
     */
    @PreAuthorize("@ss.hasPermi('operate:coupon:edit')")
    @Log(title = "优惠券", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody OperateCoupon operateCoupon)
    {
        return toAjax(operateCouponService.updateOperateCoupon(operateCoupon));
    }

    /**
     * 删除优惠券
     */
    @PreAuthorize("@ss.hasPermi('operate:coupon:remove')")
    @Log(title = "优惠券", businessType = BusinessType.DELETE)
	@PostMapping("/delete/{couponIds}")
    public AjaxResult remove(@PathVariable Long[] couponIds)
    {
        return toAjax(operateCouponService.deleteOperateCouponByCouponIds(couponIds));
    }

    //region 统计
    /**
     * 查询优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('operate:coupon:statistics')")
    @GetMapping("/pageStatistics")
    public TableDataInfo pageStatistics(OperateCoupon operateCoupon)
    {
        startPage();
        List<OperateCouponStatistics> list = operateCouponService.selectOperateCouponStatisticsList(operateCoupon);
        return getDataTable(list);
    }
    //endregion

    @GetMapping("/trendChart")
    public AjaxResult trendChart(OperateCoupon operateCoupon)
    {
        List<StatisticsBaseVO> list = operateCouponService.selectTrendEchartList(operateCoupon);
        return success(list);
    }
}

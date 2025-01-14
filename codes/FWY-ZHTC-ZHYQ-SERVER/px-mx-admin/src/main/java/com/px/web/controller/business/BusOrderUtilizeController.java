package com.px.web.controller.business;

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
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import com.px.business.domain.BusOrderUtilize;
import com.px.business.service.IBusOrderUtilizeService;
import com.px.common.utils.poi.ExcelUtil;
import com.px.common.core.page.TableDataInfo;

/**
 * 泊位利用率报表Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/business/utilize")
public class BusOrderUtilizeController extends BaseController
{
    @Autowired
    private IBusOrderUtilizeService busOrderUtilizeService;

    /**
     * 查询泊位利用率报表列表
     */
    @PreAuthorize("@ss.hasPermi('business:utilize:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusOrderUtilize busOrderUtilize)
    {
        startPage();
        List<BusOrderUtilize> list = busOrderUtilizeService.selectBusOrderUtilizeList(busOrderUtilize);
        return getDataTable(list);
    }

    /**
     * 导出泊位利用率报表列表
     */
    @PreAuthorize("@ss.hasPermi('business:utilize:export')")
    @Log(title = "泊位利用率报表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusOrderUtilize busOrderUtilize)
    {
        List<BusOrderUtilize> list = busOrderUtilizeService.selectBusOrderUtilizeList(busOrderUtilize);
        ExcelUtil<BusOrderUtilize> util = new ExcelUtil<BusOrderUtilize>(BusOrderUtilize.class);
        util.exportExcel(response, list, "泊位利用率报表数据");
    }


    /**
     * 查询泊位利用率报表列表
     */
    @PreAuthorize("@ss.hasPermi('business:utilize:list')")
    @GetMapping("/trendChart")
    public AjaxResult trendChart(BusOrderUtilize busOrderUtilize)
    {
        List<BusOrderUtilize> list = busOrderUtilizeService.selectUtilizeTrendChart(busOrderUtilize);
        return success(list);
    }
}

package com.px.web.controller.operate;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.px.operate.domain.OperateFeedback;
import com.px.operate.service.IOperateFeedbackService;
import com.px.common.utils.poi.ExcelUtil;
import com.px.common.core.page.TableDataInfo;

/**
 * 用户反馈Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/operate/feedback")
public class OperateFeedbackController extends BaseController
{
    @Autowired
    private IOperateFeedbackService operateFeedbackService;

    /**
     * 查询用户反馈列表
     */
    @PreAuthorize("@ss.hasPermi('operate:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(OperateFeedback operateFeedback)
    {
        startPage();
        List<OperateFeedback> list = operateFeedbackService.selectOperateFeedbackList(operateFeedback);
        return getDataTable(list);
    }

    /**
     * 导出用户反馈列表
     */
    @PreAuthorize("@ss.hasPermi('operate:feedback:export')")
    @Log(title = "用户反馈", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OperateFeedback operateFeedback)
    {
        List<OperateFeedback> list = operateFeedbackService.selectOperateFeedbackList(operateFeedback);
        ExcelUtil<OperateFeedback> util = new ExcelUtil<OperateFeedback>(OperateFeedback.class);
        util.exportExcel(response, list, "用户反馈数据");
    }

    /**
     * 获取用户反馈详细信息
     */
    @PreAuthorize("@ss.hasPermi('operate:feedback:query')")
    @GetMapping(value = "/{feedbackId}")
    public AjaxResult getInfo(@PathVariable("feedbackId") Long feedbackId)
    {
        return success(operateFeedbackService.selectOperateFeedbackByFeedbackId(feedbackId));
    }

    /**
     * 新增用户反馈
     */
    @PreAuthorize("@ss.hasPermi('operate:feedback:add')")
    @Log(title = "用户反馈", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody OperateFeedback operateFeedback)
    {
        return toAjax(operateFeedbackService.insertOperateFeedback(operateFeedback));
    }

    /**
     * 修改用户反馈
     */
    @PreAuthorize("@ss.hasPermi('operate:feedback:edit')")
    @Log(title = "用户反馈", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody OperateFeedback operateFeedback)
    {
        return toAjax(operateFeedbackService.updateOperateFeedback(operateFeedback));
    }

    /**
     * 删除用户反馈
     */
    @PreAuthorize("@ss.hasPermi('operate:feedback:remove')")
    @Log(title = "用户反馈", businessType = BusinessType.DELETE)
	@PostMapping("/delete/{feedbackIds}")
    public AjaxResult remove(@PathVariable Long[] feedbackIds)
    {
        return toAjax(operateFeedbackService.deleteOperateFeedbackByFeedbackIds(feedbackIds));
    }

    /**
     * 处理用户反馈
     */
    @PreAuthorize("@ss.hasPermi('operate:feedback:edit')")
    @Log(title = "用户反馈", businessType = BusinessType.UPDATE)
    @PostMapping("/solve")
    public AjaxResult solve(@RequestBody OperateFeedback operateFeedback)
    {
        operateFeedback.setUpdateBy(getUsername());
        return toAjax(operateFeedbackService.solveOperateFeedback(operateFeedback));
    }
}

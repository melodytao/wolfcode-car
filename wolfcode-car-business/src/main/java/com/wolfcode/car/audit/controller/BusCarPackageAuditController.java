package com.wolfcode.car.audit.controller;

import com.wolfcode.car.audit.domain.BusCarPackageAudit;
import com.wolfcode.car.audit.domain.vo.BusCarPackageAuditVo;
import com.wolfcode.car.audit.service.IBusCarPackageAuditService;
import com.wolfcode.car.common.annotation.Log;
import com.wolfcode.car.common.core.controller.BaseController;
import com.wolfcode.car.common.core.domain.AjaxResult;
import com.wolfcode.car.common.core.page.TableDataInfo;
import com.wolfcode.car.common.enums.BusinessType;
import com.wolfcode.car.common.utils.poi.ExcelUtil;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 套餐审核Controller
 * 
 * @author wolfcode
 * @date 2022-09-26
 */
@RestController
@RequestMapping("/business/audit")
public class BusCarPackageAuditController extends BaseController
{
    @Autowired
    private IBusCarPackageAuditService busCarPackageAuditService;

    /**
     * 查询套餐审核列表
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusCarPackageAudit busCarPackageAudit)
    {
        startPage();
        List<BusCarPackageAudit> list = busCarPackageAuditService.selectBusCarPackageAuditList(busCarPackageAudit);
        return getDataTable(list);
    }

    /**
     * 导出套餐审核列表
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:export')")
    @Log(title = "套餐审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusCarPackageAudit busCarPackageAudit)
    {
        List<BusCarPackageAudit> list = busCarPackageAuditService.selectBusCarPackageAuditList(busCarPackageAudit);
        ExcelUtil<BusCarPackageAudit> util = new ExcelUtil<BusCarPackageAudit>(BusCarPackageAudit.class);
        util.exportExcel(response, list, "套餐审核数据");
    }

    /**
     * 获取套餐审核详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busCarPackageAuditService.selectBusCarPackageAuditById(id));
    }

    /**
     * 新增套餐审核
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:add')")
    @Log(title = "套餐审核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusCarPackageAudit busCarPackageAudit)
    {
        return toAjax(busCarPackageAuditService.insertBusCarPackageAudit(busCarPackageAudit));
    }

    /**
     * 修改套餐审核
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:edit')")
    @Log(title = "套餐审核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusCarPackageAudit busCarPackageAudit)
    {
        return toAjax(busCarPackageAuditService.updateBusCarPackageAudit(busCarPackageAudit));
    }

    /**
     * 删除套餐审核
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:remove')")
    @Log(title = "套餐审核", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busCarPackageAuditService.deleteBusCarPackageAuditByIds(ids));
    }

    @GetMapping("/todo")
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:todo')")
    public TableDataInfo todoQuery(BusCarPackageAuditVo auditVo){
        List<BusCarPackageAudit> busCarPackageAudits = busCarPackageAuditService.todoQuery(auditVo);
        return getDataTable(busCarPackageAudits);
    }

    @PostMapping("/audit")
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:audit')")
    public AjaxResult audit(@RequestBody BusCarPackageAuditVo auditVo){
        busCarPackageAuditService.audit(auditVo);
        return AjaxResult.success();
    }

    /**
     * 审批历史
     *
     * @param instanceId
     * @return
     */
    @GetMapping("/history/{instanceId}")
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:history')")
    public TableDataInfo listHistory(@PathVariable String instanceId) {
        startPage();
        return getDataTable(busCarPackageAuditService.listHistory(instanceId));
    }

    /**
     * 进度查看流程图
     * @param id
     * @param response
     * @throws IOException
     */
    @GetMapping("/process/{id}")
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:processImg')")
    public void processImg(@PathVariable Long id, HttpServletResponse response) throws IOException {
        InputStream inputStream = busCarPackageAuditService.getProcessInputStream(id);
        IOUtils.copy(inputStream, response.getOutputStream());
    }


    /**
     * 我的已办
     *
     * @param auditVo
     * @return
     */
    @GetMapping("/done")
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:done')")
    public TableDataInfo doneQuery(BusCarPackageAuditVo auditVo) {
        return getDataTable(busCarPackageAuditService.doneQuery(auditVo));
    }

    /**
     * 撤销审核
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:cancel')")
    public AjaxResult cancelApply(@PathVariable Long id) {
        busCarPackageAuditService.cancelApply(id);
        return AjaxResult.success();
    }

}

package com.wolfcode.car.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wolfcode.car.common.annotation.Log;
import com.wolfcode.car.common.core.controller.BaseController;
import com.wolfcode.car.common.core.domain.AjaxResult;
import com.wolfcode.car.common.enums.BusinessType;
import com.wolfcode.car.audit.domain.BusCarPackageAudit;
import com.wolfcode.car.audit.service.IBusCarPackageAuditService;
import com.wolfcode.car.common.utils.poi.ExcelUtil;
import com.wolfcode.car.common.core.page.TableDataInfo;

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
}

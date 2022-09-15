package com.wolfcode.car.web.controller.monitor;

import com.wolfcode.car.common.annotation.Log;
import com.wolfcode.car.common.core.controller.BaseController;
import com.wolfcode.car.common.core.domain.AjaxResult;
import com.wolfcode.car.common.core.page.TableDataInfo;
import com.wolfcode.car.common.enums.BusinessType;
import com.wolfcode.car.common.utils.poi.ExcelUtil;
import com.wolfcode.car.system.domain.SysOperLog;
import com.wolfcode.car.system.service.ISysOperLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 操作日志管理
 */
@Api(value = "操作日志管理")
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperLogController extends BaseController {
    @Autowired
    private ISysOperLogService operLogService;

    @ApiOperation("获取操作日志列表")
    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }


    @ApiOperation("导出操作日志列表")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog) {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }


    @ApiOperation("批量删除操作日志")
    @ApiImplicitParam(name = "operIds", value = "批量删除操作日志",
            dataType = "Long", allowMultiple = true, dataTypeClass = Long.class,
            paramType = "path")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @ApiOperation("清空操作日志")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize(("@ss.hasPermi('monitor:operlog:remove')"))
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        operLogService.cleanOperLog();
        return AjaxResult.success();
    }
}

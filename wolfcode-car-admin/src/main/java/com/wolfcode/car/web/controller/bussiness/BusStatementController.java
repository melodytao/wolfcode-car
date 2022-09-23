package com.wolfcode.car.web.controller.bussiness;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.wolfcode.car.appointment.domain.BusStatement;
import com.wolfcode.car.appointment.service.IBusAppointmentService;
import com.wolfcode.car.appointment.service.IBusStatementService;
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
import com.wolfcode.car.common.utils.poi.ExcelUtil;
import com.wolfcode.car.common.core.page.TableDataInfo;

/**
 * 结算单Controller
 * 
 * @author wolfcode
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/business/statement")
public class BusStatementController extends BaseController
{
    @Autowired
    private IBusStatementService busStatementService;
    @Autowired
    private IBusAppointmentService busAppointmentService;
    /**
     * 查询结算单列表
     */
    @PreAuthorize("@ss.hasPermi('business:statement:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusStatement busStatement)
    {
        startPage();
        List<BusStatement> list = busStatementService.selectBusStatementList(busStatement);
        return getDataTable(list);
    }



    /**
     * 获取结算单详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:statement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busStatementService.selectBusStatementById(id));
    }

    /**
     * 新增结算单
     */
    @PreAuthorize("@ss.hasPermi('business:statement:add')")
    @Log(title = "结算单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusStatement busStatement)
    {
        return toAjax(busStatementService.insertBusStatement(busStatement));
    }

    /**
     * 修改结算单
     */
    @PreAuthorize("@ss.hasPermi('business:statement:edit')")
    @Log(title = "结算单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusStatement busStatement)
    {
        return toAjax(busStatementService.updateBusStatement(busStatement));
    }

    /**
     * 删除结算单,允许一个一个删除
     */
    @PreAuthorize("@ss.hasPermi('business:statement:remove')")
    @Log(title = "结算单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(busStatementService.deleteBusStatementById(id));
    }

    @PutMapping("/pay/{id}")
    @PreAuthorize("@ss.hasPermi('business:statementItem:pay')")
    public AjaxResult pay(@PathVariable Long id){
        busStatementService.pay(id);
        return AjaxResult.success("支付成功");
    }

    /**
     * 生成结算单
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:generateStatement')")
    @PutMapping("/generate/{id}")
    public AjaxResult generateStatement(@PathVariable Long id) {
        BusStatement statement = busAppointmentService.generateStatement(id);
        return AjaxResult.success(statement.getId());
    }
}

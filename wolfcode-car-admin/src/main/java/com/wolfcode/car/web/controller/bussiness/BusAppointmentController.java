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
import com.wolfcode.car.appointment.domain.BusAppointment;
import com.wolfcode.car.appointment.service.IBusAppointmentService;
import com.wolfcode.car.common.utils.poi.ExcelUtil;
import com.wolfcode.car.common.core.page.TableDataInfo;

/**
 * 养修信息预约Controller
 * 
 * @author wolfcode
 * @date 2022-09-19
 */
@RestController
@RequestMapping("/business/appointment")
public class BusAppointmentController extends BaseController
{
    @Autowired
    private IBusAppointmentService busAppointmentService;

    /**
     * 查询养修信息预约列表
     */
    @PreAuthorize("@ss.hasPermi('bussiness:appointment:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusAppointment busAppointment)
    {
        startPage();
        List<BusAppointment> list = busAppointmentService.selectBusAppointmentList(busAppointment);
        return getDataTable(list);
    }

    /**
     * 新增养修信息预约
     */
    @PreAuthorize("@ss.hasPermi('bussiness:appointment:add')")
    @Log(title = "养修信息预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusAppointment busAppointment)
    {
        return toAjax(busAppointmentService.insertBusAppointment(busAppointment));
    }

    /**
     * 修改养修信息预约
     */
    @PreAuthorize("@ss.hasPermi('bussiness:appointment:edit')")
    @Log(title = "养修信息预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusAppointment busAppointment)
    {
        return toAjax(busAppointmentService.updateBusAppointment(busAppointment));
    }

    /**
     * 获取养修信息预约详细信息
     */
    @PreAuthorize("@ss.hasPermi('bussiness:appointment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busAppointmentService.selectBusAppointmentById(id));
    }

    /**
     * 删除养修信息预约
     */
    @PreAuthorize("@ss.hasPermi('bussiness:appointment:remove')")
    @Log(title = "养修信息预约", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busAppointmentService.deleteBusAppointmentByIds(ids));
    }


    /**
     * 确认到店
     * @param id
     * @return
     */
    @PutMapping("/arrival/{id}")
    @Log(title = "养修信息预约", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:appointment:arrival')")
    public AjaxResult arrival(@PathVariable Long id) {
        busAppointmentService.arrival(id);
        return AjaxResult.success();
    }

    /**
     * 取消预约
     * @param id
     * @return
     */
    @RequestMapping("/cancel/{id}")
    @Log(title = "养修信息预约", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:appointment:cancel')")
    public AjaxResult cancel(@PathVariable Long id) {
        busAppointmentService.cancel(id);
        return AjaxResult.success();
    }
}
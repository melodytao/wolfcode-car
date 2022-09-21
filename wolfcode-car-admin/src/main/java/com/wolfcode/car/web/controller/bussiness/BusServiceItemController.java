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
import com.wolfcode.car.serviceitem.domain.BusServiceItem;
import com.wolfcode.car.serviceitem.service.IBusServiceItemService;
import com.wolfcode.car.common.utils.poi.ExcelUtil;
import com.wolfcode.car.common.core.page.TableDataInfo;

/**
 * 服务项Controller
 * 
 * @author wolfcode
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/business/serviceitem")
public class BusServiceItemController extends BaseController
{
    @Autowired
    private IBusServiceItemService busServiceItemService;

    /**
     * 查询服务项列表
     */
    @PreAuthorize("@ss.hasPermi('business:serviceitem:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusServiceItem busServiceItem)
    {
        startPage();
        List<BusServiceItem> list = busServiceItemService.selectBusServiceItemList(busServiceItem);
        return getDataTable(list);
    }

    /**
     * 获取服务项详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:serviceitem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busServiceItemService.selectBusServiceItemById(id));
    }

    /**
     * 新增服务项
     */
    @PreAuthorize("@ss.hasPermi('business:serviceitem:add')")
    @Log(title = "服务项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusServiceItem busServiceItem)
    {
        return toAjax(busServiceItemService.insertBusServiceItem(busServiceItem));
    }

    /**
     * 修改服务项
     */
    @PreAuthorize("@ss.hasPermi('business:serviceitem:edit')")
    @Log(title = "服务项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusServiceItem busServiceItem)
    {
        return toAjax(busServiceItemService.updateBusServiceItem(busServiceItem));
    }

    /**
     * 删除服务项
     */
    @PreAuthorize("@ss.hasPermi('business:serviceitem:remove')")
    @Log(title = "服务项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busServiceItemService.deleteBusServiceItemByIds(ids));
    }

    /**
     * 上架
     *
     * @param id
     * @return
     */
    @PutMapping("/saleOn/{id}")
    @Log(title = "服务项", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:serviceItem:saleOn')")
    public AjaxResult saleOn(@PathVariable Long id) {
        busServiceItemService.saleOn(id);
        return AjaxResult.success();
    }

    /**
     * 下架
     *
     * @param id
     * @return
     */
    @PutMapping("/saleOff/{id}")
    @Log(title = "服务项", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:serviceItem:saleOff')")
    public AjaxResult saleOff(@PathVariable Long id) {
        busServiceItemService.saleOff(id);
        return AjaxResult.success();
    }

}

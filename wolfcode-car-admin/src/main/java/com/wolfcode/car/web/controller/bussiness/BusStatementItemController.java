package com.wolfcode.car.web.controller.bussiness;

import com.wolfcode.car.appointment.domain.BusStatementItem;
import com.wolfcode.car.appointment.domain.vo.BusStatementItemsVo;
import com.wolfcode.car.appointment.service.IBusStatementItemService;
import com.wolfcode.car.common.core.controller.BaseController;
import com.wolfcode.car.common.core.domain.AjaxResult;
import com.wolfcode.car.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/statementItem")
public class BusStatementItemController extends BaseController {
    @Autowired
    private IBusStatementItemService busStatementItemService;

    @GetMapping
    @PreAuthorize("@ss.hasPermi('business:statementItem:list')")
    public TableDataInfo list(BusStatementItem busStatementItem){
        startPage();
        List<BusStatementItem> list = busStatementItemService.
                selectBusStatementItemList(busStatementItem);
        return getDataTable(list);
    }
    @PostMapping
    @PreAuthorize(("@ss.hasPermi('business:statementItem:save')"))
    public AjaxResult save(@RequestBody BusStatementItemsVo busStatementItemsVo){
        busStatementItemService.saveBusStatementItems(busStatementItemsVo);
        return AjaxResult.success("保存成功");
    }
}

package com.wolfcode.car.report.controller;

import com.wolfcode.car.appointment.domain.BusAppointment;
import com.wolfcode.car.common.core.controller.BaseController;
import com.wolfcode.car.common.core.page.TableDataInfo;
import com.wolfcode.car.report.domain.info.StatisticalConsumptionInfo;
import com.wolfcode.car.report.domain.info.StatisticalCustomerInfo;
import com.wolfcode.car.report.domain.info.StatisticalShopIncomeInfo;
import com.wolfcode.car.report.domain.vo.ConsumptionVo;
import com.wolfcode.car.report.domain.vo.CustomerVo;
import com.wolfcode.car.report.domain.vo.ShopIncomeVo;
import com.wolfcode.car.report.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController extends BaseController {
    @Autowired
    private IReportService reportService;

    /**
     * 店铺收入列表
     */
    @PreAuthorize("@ss.hasPermi('business:report:income')")
    @GetMapping("/list")
    public TableDataInfo list(ShopIncomeVo shopIncomeVo) {
        startPage();
        List<StatisticalShopIncomeInfo> list =
                reportService.statisticShopIncome(shopIncomeVo);
        return getDataTable(list);
    }

    /**
     * 店铺收入列表
     */
    @PreAuthorize("@ss.hasPermi('business:report:consumptionlist')")
    @GetMapping("/consumption")
    public TableDataInfo consumptionList(ConsumptionVo consumptionVo) {
        startPage();
        List<StatisticalConsumptionInfo> list =
                reportService.statisticConsumption(consumptionVo);
        return getDataTable(list);
    }


    /**
     * 手机号统计
     */
    @PreAuthorize("@ss.hasPermi('business:report:customer')")
    @GetMapping("/customer")
    public TableDataInfo customer(CustomerVo customerVo) {
        startPage();
        List<StatisticalCustomerInfo> list =
                reportService.statisticCustomer(customerVo);
        return getDataTable(list);
    }

}

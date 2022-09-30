package com.wolfcode.car.report.service.impl;

import com.wolfcode.car.report.domain.info.StatisticalConsumptionInfo;
import com.wolfcode.car.report.domain.info.StatisticalCustomerInfo;
import com.wolfcode.car.report.domain.info.StatisticalShopIncomeInfo;
import com.wolfcode.car.report.domain.vo.ConsumptionVo;
import com.wolfcode.car.report.domain.vo.CustomerVo;
import com.wolfcode.car.report.domain.vo.ShopIncomeVo;
import com.wolfcode.car.report.mapper.ReportMappper;
import com.wolfcode.car.report.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReportServiceImpl implements IReportService {
    @Autowired
    private ReportMappper reportMappper;

    /**
     * 统计店铺收入
     * @param shopIncomeVo
     * @return
     */
    @Override
    public List<StatisticalShopIncomeInfo> statisticShopIncome(
            ShopIncomeVo shopIncomeVo) {
        return reportMappper.statisticShopIncome(shopIncomeVo);
    }

    @Override
    public List<StatisticalConsumptionInfo> statisticConsumption(ConsumptionVo consumptionVo) {
        return reportMappper.statisticConsumption(consumptionVo);
    }

    @Override
    public List<StatisticalCustomerInfo> statisticCustomer(CustomerVo customerVo) {
        return reportMappper.statisticCustomer(customerVo);
    }
}

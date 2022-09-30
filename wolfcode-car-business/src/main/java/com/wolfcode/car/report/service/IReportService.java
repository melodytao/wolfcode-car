package com.wolfcode.car.report.service;

import com.wolfcode.car.report.domain.info.StatisticalConsumptionInfo;
import com.wolfcode.car.report.domain.info.StatisticalCustomerInfo;
import com.wolfcode.car.report.domain.info.StatisticalShopIncomeInfo;
import com.wolfcode.car.report.domain.vo.ConsumptionVo;
import com.wolfcode.car.report.domain.vo.CustomerVo;
import com.wolfcode.car.report.domain.vo.ShopIncomeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报表服务
 */
public interface IReportService {

    /**
     * 统计店铺收入
     * @param shopIncomeVo
     * @return
     */
    List<StatisticalShopIncomeInfo> statisticShopIncome(
            @Param("shopIncomeVo") ShopIncomeVo shopIncomeVo);

    /**
     * 统计消费单类型
     * @param consumptionVo
     * @return
     */
    List<StatisticalConsumptionInfo> statisticConsumption(
            @Param("consumptionVo") ConsumptionVo consumptionVo);

    /**
     * 手机号码消费统计
     * @param customerVo
     * @return
     */
    List<StatisticalCustomerInfo> statisticCustomer(
            @Param("customerVo") CustomerVo customerVo);
}

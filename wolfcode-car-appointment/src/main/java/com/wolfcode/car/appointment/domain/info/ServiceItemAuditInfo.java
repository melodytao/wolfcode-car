package com.wolfcode.car.appointment.domain.info;

import com.wolfcode.car.appointment.domain.BusServiceItem;
import com.wolfcode.car.common.core.domain.entity.SysUser;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ServiceItemAuditInfo {
    private BusServiceItem serviceItem;
    private List<SysUser> shopOwners;
    private List<SysUser> finances;
    private BigDecimal discountPrice;
}

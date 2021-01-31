/*
 * Digital Lean System (DLS)
 * <p>
 * Copyright (c) 2012-2020 Lean Intelligence Inc., All Rights Reserved.
 * <p>
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for NON-COMMERCIAL or COMMERCIAL purposes and without fee is
 * hereby granted.
 * <p>
 * YOUR COMPANY MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. YOUR COMPANY SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED
 * BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR
 * ITS DERIVATIVES.
 * <p>
 * THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
 * CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE PERFORMANCE,
 * SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT NAVIGATION OR
 * COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE SUPPORT MACHINES, OR
 * WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE SOFTWARE COULD LEAD DIRECTLY TO
 * DEATH, PERSONAL INJURY, OR SEVERE PHYSICAL OR ENVIRONMENTAL DAMAGE
 * ("HIGH RISK ACTIVITIES"). YOUR COMPANY SPECIFICALLY DISCLAIMS ANY EXPRESS OR
 * IMPLIED WARRANTY OF FITNESS FOR HIGH RISK ACTIVITIES.
 */

package com.sophlean.aps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * APS计划输入 - 产品库存
 */
public class ApsScheduleInputProductInventory implements Serializable {

    protected static final Logger log = LogManager.getLogger();

    private static final long serialVersionUID = 615615615L;

    /**
     * 编号（品号）
     */
    private String id;
    /**
     * 类型
     */
    private ApsScheduleInputProductInventoryType type;
    /**
     * 数量
     */
    private BigDecimal quantityNumber;

    public String getId() {
        return id;
    }

    public ApsScheduleInputProductInventory setId(String id) {
        this.id = id;
        return this;
    }

    public ApsScheduleInputProductInventoryType getType() {
        return type;
    }

    public ApsScheduleInputProductInventory setType(ApsScheduleInputProductInventoryType type) {
        this.type = type;
        return this;
    }

    public BigDecimal getQuantityNumber() {
        return quantityNumber;
    }

    public ApsScheduleInputProductInventory setQuantityNumber(BigDecimal quantityNumber) {
        this.quantityNumber = quantityNumber;
        return this;
    }
}

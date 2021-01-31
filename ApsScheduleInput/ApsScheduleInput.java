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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * APS计划输入
 */
public class ApsScheduleInput implements Serializable {

    protected static final Logger log = LogManager.getLogger();

    private static final long serialVersionUID = 615615615L;

    /**
     * 计算时间
     */
    private Date transactionTime;
    /**
     * 计划开始时间
     */
    private Date startTime;
    /**
     * 计划结束时间
     */
    private Date endTime;
    /**
     * 产品
     */
    private List<ApsScheduleInputProduct> productList;
    /**
     * 关键工序
     */
    private List<ApsScheduleInputProductSegment> productSegmentList;
    /**
     * 库存
     */
    private List<ApsScheduleInputProductInventory> inventoryList;
    /**
     * 需求
     */
    private List<ApsScheduleInputDemand> demandList;

    /**
     * 计划天数
     */
    public long getDays() {
        if (this.startTime != null && this.endTime != null) {
            LocalDate start = date2LocalDate(this.startTime);
            LocalDate end = date2LocalDate(this.endTime);
            return end.toEpochDay() - start.toEpochDay();
        } else {
            return -1;
        }
    }

    private static LocalDate date2LocalDate(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public ApsScheduleInput setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public ApsScheduleInput setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public ApsScheduleInput setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public List<ApsScheduleInputProduct> getProductList() {
        return productList;
    }

    public ApsScheduleInput setProductList(List<ApsScheduleInputProduct> productList) {
        this.productList = productList;
        return this;
    }

    public List<ApsScheduleInputProductSegment> getProductSegmentList() {
        return productSegmentList;
    }

    public ApsScheduleInput setProductSegmentList(List<ApsScheduleInputProductSegment> productSegmentList) {
        this.productSegmentList = productSegmentList;
        return this;
    }

    public List<ApsScheduleInputProductInventory> getInventoryList() {
        return inventoryList;
    }

    public ApsScheduleInput setInventoryList(List<ApsScheduleInputProductInventory> inventoryList) {
        this.inventoryList = inventoryList;
        return this;
    }

    public List<ApsScheduleInputDemand> getDemandList() {
        return demandList;
    }

    public ApsScheduleInput setDemandList(List<ApsScheduleInputDemand> demandList) {
        this.demandList = demandList;
        return this;
    }
}

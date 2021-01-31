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
import java.util.Date;

/**
 * APS计划输入 - 产品需求
 */
public class ApsScheduleInputDemand implements Serializable {

    protected static final Logger log = LogManager.getLogger();

    private static final long serialVersionUID = 615615615L;

    /**
     * 编号（订单编号）
     */
    private String id;
    /**
     * 检修任务编号
     */
    private String productDefinitionId;
    /**
     * 产品类（车型）
     */
    private String productClass;
    /**
     * 车组号
     */
    private String set;
    /**
     * 车厢号
     */
    private String setItem;
    /**
     * 修程
     */
    private String program;
    /**
     * 轮对号
     */
    private String uuid;
    /**
     * 类型（动拖）
     */
    private String productType;
    /**
     * 是否到限
     */
    private boolean toTheLimit;
    /**
     * 数量
     */
    private BigDecimal quantityNumber;
    /**
     * 入检修车间日期
     */
    private Date toReceiveTime;
    /**
     * 计划交付日期
     */
    private Date toDeliverTime;
    /**
     * 订单类型
     */
    private String type;
    /**
     * 是否需要装轴承轴箱
     */
    private boolean axleBox;
    /**
     * 订单优先级
     */
    private Integer priority;

    public String getId() {
        return id;
    }

    public ApsScheduleInputDemand setId(String id) {
        this.id = id;
        return this;
    }

    public String getProductDefinitionId() {
        return productDefinitionId;
    }

    public ApsScheduleInputDemand setProductDefinitionId(String productDefinitionId) {
        this.productDefinitionId = productDefinitionId;
        return this;
    }

    public String getProductClass() {
        return productClass;
    }

    public ApsScheduleInputDemand setProductClass(String productClass) {
        this.productClass = productClass;
        return this;
    }

    public String getSet() {
        return set;
    }

    public ApsScheduleInputDemand setSet(String set) {
        this.set = set;
        return this;
    }

    public String getSetItem() {
        return setItem;
    }

    public ApsScheduleInputDemand setSetItem(String setItem) {
        this.setItem = setItem;
        return this;
    }

    public String getProgram() {
        return program;
    }

    public ApsScheduleInputDemand setProgram(String program) {
        this.program = program;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public ApsScheduleInputDemand setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getProductType() {
        return productType;
    }

    public ApsScheduleInputDemand setProductType(String productType) {
        this.productType = productType;
        return this;
    }

    public boolean isToTheLimit() {
        return toTheLimit;
    }

    public ApsScheduleInputDemand setToTheLimit(boolean toTheLimit) {
        this.toTheLimit = toTheLimit;
        return this;
    }

    public BigDecimal getQuantityNumber() {
        return quantityNumber;
    }

    public ApsScheduleInputDemand setQuantityNumber(BigDecimal quantityNumber) {
        this.quantityNumber = quantityNumber;
        return this;
    }

    public Date getToReceiveTime() {
        return toReceiveTime;
    }

    public ApsScheduleInputDemand setToReceiveTime(Date toReceiveTime) {
        this.toReceiveTime = toReceiveTime;
        return this;
    }

    public Date getToDeliverTime() {
        return toDeliverTime;
    }

    public ApsScheduleInputDemand setToDeliverTime(Date toDeliverTime) {
        this.toDeliverTime = toDeliverTime;
        return this;
    }

    public String getType() {
        return type;
    }

    public ApsScheduleInputDemand setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isAxleBox() {
        return axleBox;
    }

    public ApsScheduleInputDemand setAxleBox(boolean axleBox) {
        this.axleBox = axleBox;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public ApsScheduleInputDemand setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }
}

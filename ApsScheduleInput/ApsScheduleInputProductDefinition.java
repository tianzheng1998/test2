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
import java.util.List;

/**
 * APS计划输入 - 工艺路线
 */
public class ApsScheduleInputProductDefinition implements Serializable {

    protected static final Logger log = LogManager.getLogger();

    private static final long serialVersionUID = 615615615L;

    /**
     * 编号（检修任务编号）
     */
    private String id;
    /**
     * 产品类（车型）
     */
    private String productClass;
    /**
     * 修程
     */
    private String program;
    /**
     * 类型（动拖）
     */
    private String productType;
    /**
     * 是否到限
     */
    private boolean toTheLimit;
    /**
     * 工艺路线
     */
    private List<ApsScheduleInputProductSegment> productSegmentList;

    public String getId() {
        return id;
    }

    public ApsScheduleInputProductDefinition setId(String id) {
        this.id = id;
        return this;
    }

    public String getProductClass() {
        return productClass;
    }

    public ApsScheduleInputProductDefinition setProductClass(String productClass) {
        this.productClass = productClass;
        return this;
    }

    public String getProgram() {
        return program;
    }

    public ApsScheduleInputProductDefinition setProgram(String program) {
        this.program = program;
        return this;
    }

    public String getProductType() {
        return productType;
    }

    public ApsScheduleInputProductDefinition setProductType(String productType) {
        this.productType = productType;
        return this;
    }

    public boolean isToTheLimit() {
        return toTheLimit;
    }

    public ApsScheduleInputProductDefinition setToTheLimit(boolean toTheLimit) {
        this.toTheLimit = toTheLimit;
        return this;
    }

    public List<ApsScheduleInputProductSegment> getProductSegmentList() {
        return productSegmentList;
    }

    public ApsScheduleInputProductDefinition setProductSegmentList(List<ApsScheduleInputProductSegment> productSegmentList) {
        this.productSegmentList = productSegmentList;
        return this;
    }
}

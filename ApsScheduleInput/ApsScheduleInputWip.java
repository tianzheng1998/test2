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
import java.util.Date;

/**
 * APS计划输入 - 在制任务
 */
public class ApsScheduleInputWip implements Serializable {

    protected static final Logger log = LogManager.getLogger();

    private static final long serialVersionUID = 615615615L;

    /**
     * 编号（轮对号）
     */
    private String id;
    /**
     * 检修任务编号
     */
    private String productDefinitionId;
    /**
     * 当前所处工序编号
     */
    private String productSegmentId;
    /**
     * 外协
     */
    private boolean outsourcing;
    /**
     * 预计返回时间
     */
    private Date toReceiveTime;

    public String getId() {
        return id;
    }

    public ApsScheduleInputWip setId(String id) {
        this.id = id;
        return this;
    }

    public String getProductDefinitionId() {
        return productDefinitionId;
    }

    public ApsScheduleInputWip setProductDefinitionId(String productDefinitionId) {
        this.productDefinitionId = productDefinitionId;
        return this;
    }

    public String getProductSegmentId() {
        return productSegmentId;
    }

    public ApsScheduleInputWip setProductSegmentId(String productSegmentId) {
        this.productSegmentId = productSegmentId;
        return this;
    }

    public boolean isOutsourcing() {
        return outsourcing;
    }

    public ApsScheduleInputWip setOutsourcing(boolean outsourcing) {
        this.outsourcing = outsourcing;
        return this;
    }

    public Date getToReceiveTime() {
        return toReceiveTime;
    }

    public ApsScheduleInputWip setToReceiveTime(Date toReceiveTime) {
        this.toReceiveTime = toReceiveTime;
        return this;
    }
}

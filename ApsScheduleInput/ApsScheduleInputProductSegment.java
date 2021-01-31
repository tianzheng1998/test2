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

import com.sophlean.core.lang.Period;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * APS计划输入 - 关键工序
 */
public class ApsScheduleInputProductSegment implements Serializable {

    protected static final Logger log = LogManager.getLogger();

    private static final long serialVersionUID = 615615615L;

    /**
     * 编号
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 加工时间
     */
    private Period cycleTime;
    /**
     * 日历（K-日期, V-工作时间/分），如同一工序有多台设备，则为累加值
     */
    private Map<Date, Long> calendarMap;
    /**
     * 提前时间（K-前道工序, V-提前时间/分）
     */
    private Map<ApsScheduleInputProductSegment, Long> leadTimeMap;

    public String getId() {
        return id;
    }

    public ApsScheduleInputProductSegment setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ApsScheduleInputProductSegment setName(String name) {
        this.name = name;
        return this;
    }

    public Period getCycleTime() {
        return cycleTime;
    }

    public ApsScheduleInputProductSegment setCycleTime(Period cycleTime) {
        this.cycleTime = cycleTime;
        return this;
    }

    public Map<Date, Long> getCalendarMap() {
        return calendarMap;
    }

    public ApsScheduleInputProductSegment setCalendarMap(Map<Date, Long> calendarMap) {
        this.calendarMap = calendarMap;
        return this;
    }

    public Map<ApsScheduleInputProductSegment, Long> getLeadTimeMap() {
        return leadTimeMap;
    }

    public ApsScheduleInputProductSegment setLeadTimeMap(Map<ApsScheduleInputProductSegment, Long> leadTimeMap) {
        this.leadTimeMap = leadTimeMap;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApsScheduleInputProductSegment that = (ApsScheduleInputProductSegment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

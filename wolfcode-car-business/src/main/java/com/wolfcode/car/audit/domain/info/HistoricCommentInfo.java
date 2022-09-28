package com.wolfcode.car.audit.domain.info;

import lombok.Data;

@Data
public class HistoricCommentInfo {

    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 评论
     */
    private String comment;
    /**
     * 任务开始时间
     */
    private String startTime;
    /**
     * 任务结束时间
     */
    private String endTime;
    /**
     * 任务持续时间
     */
    private String durationInMillis;
}

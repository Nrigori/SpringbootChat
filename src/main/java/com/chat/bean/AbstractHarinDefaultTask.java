package com.chat.bean;

import lombok.Data;

/**
 * @description: 子任务基类
 * 
 * @createDate: 2023-03-03
 * @version: 1.0
 */
@Data
public abstract class AbstractHarinDefaultTask {
    /**
     *  任务名
     */
    private String taskName;
    /**
     *  序号
     */
    private int order;

    /**
     *  任务执行完后的结果
     */
    private boolean resultFlag;

    /**
     *  任务执行后返回的消息
     */
    private String message;

    /**
     *  任务方法
     */
    public abstract boolean run();
}

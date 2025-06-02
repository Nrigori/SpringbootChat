package com.chat.config.autoRunner.task.executer;

import com.chat.bean.AbstractHarinDefaultTask;
import com.chat.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author laijieguan@sunwodao.com
 * @version 1.0
 * @date 2022-03-03 08:52:11
 */
@Component
@Order(value = 1)
@Slf4j
public class TaskRunner {
    private List<AbstractHarinDefaultTask> tasks;

    public void run() throws Exception{
        if(tasks == null || tasks.isEmpty()){
            throw new CommonException("tasks不能为空！！可通过构造传入或通过setTasks设置。");
        }
        //先根据顺序排序
        sort(this.tasks);
        for(AbstractHarinDefaultTask task: this.tasks) {
            log.info("["+task.getTaskName()+"]:启动....");
            task.run();
            log.info("["+task.getTaskName()+"] 任务执行消息:"+task.getMessage());
            log.info("["+task.getTaskName()+"]:启动结束。");
        }
    }
    public TaskRunner(){
        super();
    }
    public TaskRunner(List<AbstractHarinDefaultTask> tasks){
        this.tasks = tasks;
    }

    public static void sort(List<AbstractHarinDefaultTask> tasks){
        Collections.sort(tasks, new Comparator<AbstractHarinDefaultTask>() {
            @Override
            public int compare(AbstractHarinDefaultTask o1, AbstractHarinDefaultTask o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
    }
}

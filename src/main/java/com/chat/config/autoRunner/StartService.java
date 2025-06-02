package com.chat.config.autoRunner;

import com.chat.bean.AbstractHarinDefaultTask;
import com.chat.config.autoRunner.task.SelfAutoStartTaskManagementTask;
import com.chat.config.autoRunner.task.executer.TaskRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * springboot项目启动后的第一个执行任务，主要用于自检系统缺失信息
 * 继承Application接口后项目启动时会按照执行顺序执行run方法
 * 通过设置Order的value来指定执行的顺序
 */
@Component
@Order(value = 1)
@Slf4j
public class StartService implements ApplicationRunner {
    @Autowired
    SelfAutoStartTaskManagementTask selfAutoStartTaskManagementTask;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("harinChat项目启动完成，当前时间是:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //任务集合
        ArrayList<AbstractHarinDefaultTask> tasks = new ArrayList<>();
        //添加任务
        tasks.add(selfAutoStartTaskManagementTask);
        TaskRunner taskRunner = new TaskRunner(tasks);
        try {
            taskRunner.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
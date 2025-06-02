package com.chat.config.autoRunner.task;

import com.chat.bean.AbstractHarinDefaultTask;
import com.chat.bean.chat.ChatGroupInfo;
import com.chat.bean.chat.ChatGroupUserMap;
import com.chat.controller.ChatWebSocket;
import com.chat.service.ChatGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @description: 初始化系统参数
 * @createDate: 2022-12-22
 * @version: 1.0
 */
@Component
@Scope("prototype")     //如果注入spring容器，建议使用单例
@Slf4j
public class SelfAutoStartTaskManagementTask extends AbstractHarinDefaultTask {
    @Autowired
    ChatGroupService chatGroupService;

    /**
     * 空参构造
     */
    public SelfAutoStartTaskManagementTask(){
        setTaskName("系统配置初始化");
        setOrder(1);
        setResultFlag(true);
        setMessage("加载群信息:...");
    }

    /**
     * 要干什么~
     * @return
     */
    @Override
    public boolean run() {
        List<ChatGroupInfo> allGroupInfo = chatGroupService.getAllGroupInfo();
        ConcurrentHashMap<String, Set<String>> groupWebSocket = ChatWebSocket.getGroupWebSocket();
        for (ChatGroupInfo chatGroupInfo : allGroupInfo) {
            Integer gid = chatGroupInfo.getId();
            List<ChatGroupUserMap> chatGroupUserMapList = chatGroupInfo.getChatGroupUserMapList();
            Set<String> uIds = chatGroupUserMapList.stream().map(e -> e.getUId()).collect(Collectors.toSet());
            //不管以前的是什么，都直接处理成最新的数据库的
            groupWebSocket.put(gid.toString(),uIds);

        }
        return true;
    }
}

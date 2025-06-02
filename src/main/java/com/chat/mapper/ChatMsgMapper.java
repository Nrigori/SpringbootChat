package com.chat.mapper;

import com.chat.bean.chat.ChatMsg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMsgMapper {
    //插入聊天记录
    void InsertChatMsg(ChatMsg chatMsg);
    //查询好友聊天记录
    List<ChatMsg>  LookTwoUserMsg(@Param("chatMsg") ChatMsg chatMsg);
    //群消息
    List<ChatMsg>  LookGroupMsg(@Param("chatMsg") ChatMsg chatMsg);
}
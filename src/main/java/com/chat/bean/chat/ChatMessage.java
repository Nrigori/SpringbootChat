package com.chat.bean.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 消息实体类，之前作者封装的ChatMsg不是很灵活。
 * @author harin-黑医
 * @version 1.0
 * @date 2022-03-02 15:09:30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ChatMessage {
    //消息类型 系统消息、好友消息、群消息...
    private Integer msgType;
    //消息内容
    private String content;
    //其他集合内容消息
    private List<String> otherMsg;
    //发送者
    private String formName;
    //接收者
    private String toName;
    //接收时间
    private Date receiveDate;
}

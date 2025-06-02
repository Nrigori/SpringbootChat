package com.chat.bean.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ChatMsg {
    private String senduserid;

    private String reciveuserid;

    private String reciveGroupId;

    private Date sendtime;

    private String msgtype;

    private String sendtext;

    private String reciveObject;        //来源于ReciveObjectEnum中的typeCode
}
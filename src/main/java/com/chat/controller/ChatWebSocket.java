package com.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.chat.ReciveObjectEnum;
import com.chat.bean.chat.ChatMsg;
import com.chat.service.ChatMsgService;
import com.chat.util.EmojiFilter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 好友聊天室中的websocket
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 * @ServerEndpoint 可以把当前类变成websocket服务类
 */
@Controller
@ServerEndpoint(value = "/websocket/{userno}")
public class ChatWebSocket {

    // 这里使用静态，让 service 属于类
    private static ChatMsgService chatMsgService;
    // 注入的时候，给类的 service 注入
    @Autowired
    public void setChatService(ChatMsgService chatService) {
        ChatWebSocket.chatMsgService = chatService;
    }

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static ConcurrentHashMap<String, ChatWebSocket> webSocketSet = new ConcurrentHashMap<String, ChatWebSocket>();

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    @Getter
    @Setter
    private static ConcurrentHashMap<String, Set<String>> groupWebSocket =  new ConcurrentHashMap<>();


    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session WebSocketsession;
    //当前发消息的人员编号
    private String userno = "";


    /**
     * 连接建立成功调用的方法
     *
     * session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userno") String param, Session WebSocketsession) {
        userno = param;//接收到发送消息的人员编号
        this.WebSocketsession = WebSocketsession;
        webSocketSet.put(param, this);//加入map中
        addOnlineCount();     //在线数加1
        //System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (!userno.equals("")) {
            webSocketSet.remove(userno); //从set中删除
            subOnlineCount();     //在线数减1
            //System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        }
    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param chatmsg 客户端发送过来的消息
     * @param session 可选的参数
     */
    @SuppressWarnings("unused")
	@OnMessage
    public void onMessage(String chatmsg, Session session) {
        if(!StringUtils.isEmpty(chatmsg)) {
            JSONObject jsonObject = JSONObject.parseObject(chatmsg);
            //给指定的人发消息
            ChatMsg chatMsg = jsonObject.toJavaObject(ChatMsg.class);
//            sendToUser(jsonObject.toJavaObject(ChatMsg.class));
            sendToUser2(jsonObject.toJavaObject(ChatMsg.class));
        }else{
            try {
                webSocketSet.get(userno).sendMessage("0"+"|"+"数据异常");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //sendAll(message);
    }


    /**
     * 给指定的人发送消息
     *
     * @param chatMsg 消息对象
     */
    public void sendToUser(ChatMsg chatMsg) {
        String reviceUserid = chatMsg.getReciveuserid();
        String sendMessage = chatMsg.getSendtext();
        sendMessage= EmojiFilter.filterEmoji(sendMessage);//过滤输入法输入的表情
        chatMsgService.InsertChatMsg(new ChatMsg().setMsgtype(chatMsg.getMsgtype()).setReciveuserid(reviceUserid).setSenduserid(userno).setSendtext(sendMessage));
        try {
            if (webSocketSet.get(reviceUserid) != null) {
                webSocketSet.get(reviceUserid).sendMessage(userno+"|"+sendMessage);
            }else{
                webSocketSet.get(userno).sendMessage("0"+"|"+"当前用户不在线");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给指定人或者指定房间发送消息
     * @param chatMsg 消息对象
     */
    public void sendToUser2(ChatMsg chatMsg) {
        String reciveObject = chatMsg.getReciveObject();
        String reviceUserid = chatMsg.getReciveuserid();
        String sendMessage = chatMsg.getSendtext();
        sendMessage= EmojiFilter.filterEmoji(sendMessage);//过滤输入法输入的表情
        try {
            //给指定人发送消息
            if(ReciveObjectEnum.NORMAL.getTypeCode().equals(reciveObject)){
                chatMsgService.InsertChatMsg(new ChatMsg().setMsgtype(chatMsg.getMsgtype()).setReciveuserid(reviceUserid).setSenduserid(userno).setSendtext(sendMessage));
                if (webSocketSet.get(reviceUserid) != null) {
                    webSocketSet.get(reviceUserid).sendMessage(userno+"|"+sendMessage);
                }else{
                    webSocketSet.get(userno).sendMessage("0"+"|"+"当前用户不在线");
                }
            }else if(ReciveObjectEnum.GROUP.getTypeCode().equals(reciveObject)){ //给群里发消息
                //获取群id
                String reciveGroupId = chatMsg.getReciveGroupId();
                //记录聊天记录
                chatMsgService.InsertChatMsg(new ChatMsg().setMsgtype(chatMsg.getMsgtype()).setReciveuserid(reciveGroupId).setSenduserid(userno).setSendtext(sendMessage));
                //如果当前的群id存在，把其中的用户socket找出来。
                boolean contains = groupWebSocket.containsKey(reciveGroupId);
                if(contains){
                    Set<String> userIds = groupWebSocket.get(reciveGroupId);
                    for (String userId : userIds) {
                        if(!userId.equals(userno) && webSocketSet.containsKey(userId)){  //给客户端返回消息
                            webSocketSet.get(userId).sendMessage(ReciveObjectEnum.GROUP.getTypeCode()+"|"+reciveGroupId+"|"+sendMessage);
                        }
                    }
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 给所有人发消息
     * @param message
     */
    private void sendAll(String message) {
        String sendMessage = message.split("[|]")[1];
        //遍历HashMap
        for (String key : webSocketSet.keySet()) {
            try {
                //判断接收用户是否是当前发消息的用户
                if (!userno.equals(key)) {
                    webSocketSet.get(key).sendMessage(sendMessage);
                    System.out.println("key = " + key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.WebSocketsession.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }


    public static synchronized void addOnlineCount() {
        ChatWebSocket.onlineCount++;
    }


    public static synchronized void subOnlineCount() {
        ChatWebSocket.onlineCount--;
    }

}


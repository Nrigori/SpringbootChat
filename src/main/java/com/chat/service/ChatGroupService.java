package com.chat.service;

import com.chat.bean.chat.ChatGroupInfo;
import com.chat.bean.chat.ChatGroupUserMap;
import com.chat.mapper.ChatGroupInfoMapper;
import com.chat.mapper.ChatGroupUserMapMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author harin-黑医
 * @version 1.0
 * @date 2022-03-03 09:21:29
 */
@Service
public class ChatGroupService {
    @Autowired
    ChatGroupInfoMapper chatGroupInfoMapper;

    @Autowired
    ChatGroupUserMapMapper chatGroupUserMapMapper;


    public List<ChatGroupInfo> getAllGroupInfo(){
        List<ChatGroupInfo> chatGroupInfos = chatGroupInfoMapper.selectAll();
        if(!chatGroupInfos.isEmpty()){
            for (ChatGroupInfo chatGroupInfo : chatGroupInfos) {
                List<ChatGroupUserMap> chatGroupUserMaps = chatGroupUserMapMapper.selectGroupMember(chatGroupInfo.getId());
                chatGroupInfo.setChatGroupUserMapList(chatGroupUserMaps);
            }
        }
        return chatGroupInfos;
    }


    public Integer addGroupUserMap(ChatGroupUserMap chatGroupUserMap){
        return chatGroupUserMapMapper.insert(chatGroupUserMap);
    }


    public List<ChatGroupInfo> selectMyGroup(String userid) {
        return chatGroupInfoMapper.selectByUid(userid);
    }

    public ChatGroupInfo selectByGroupNumber(String groupNumber) {
        return chatGroupInfoMapper.selectByGroupNumber(groupNumber);
    }

    public ChatGroupInfo selectByPrimaryKey(Integer groupId) {
        return chatGroupInfoMapper.selectByPrimaryKey(groupId);
    }

    public ChatGroupUserMap selectGroupUserMap(ChatGroupUserMap chatGroupUserMap) {
        return chatGroupUserMapMapper.selectGroupUserMap(chatGroupUserMap);
    }
}

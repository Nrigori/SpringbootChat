package com.chat.mapper;

import com.chat.bean.chat.ChatGroupUserMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatGroupUserMapMapper {

    int insert(ChatGroupUserMap record);

    int insertSelective(ChatGroupUserMap record);

    List<ChatGroupUserMap> selectGroupMember(@Param("groupId") Integer groupId);

    ChatGroupUserMap selectGroupUserMap(ChatGroupUserMap chatGroupUserMap);
}
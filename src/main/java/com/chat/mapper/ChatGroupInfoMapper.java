package com.chat.mapper;

import com.chat.bean.chat.ChatGroupInfo;
import com.chat.bean.chat.ChatGroupUserMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatGroupInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ChatGroupInfo record);

    int insertSelective(ChatGroupInfo record);

    List<ChatGroupInfo> selectAll();

    ChatGroupInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChatGroupInfo record);

    int updateByPrimaryKey(ChatGroupInfo record);

    List<ChatGroupInfo> selectByUid(@Param("userId")String userid);

    ChatGroupInfo selectByGroupNumber(@Param("groupNumber")String groupNumber);

    ChatGroupUserMap selectGroupUserMap(ChatGroupUserMap chatGroupUserMap);
}
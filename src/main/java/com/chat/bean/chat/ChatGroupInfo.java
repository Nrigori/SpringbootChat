package com.chat.bean.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author harin-黑医
 * @version 1.0
 * @date 2022-03-01 17:12:13
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ChatGroupInfo {
    private Integer id;

    private String groupName;

    private String groupNumber;

    private String introduce;

    private String uImg;

    private Date createDate;

    private String createBy;

    private List<ChatGroupUserMap> chatGroupUserMapList;

    private String status;

}

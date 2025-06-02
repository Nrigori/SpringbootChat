package com.chat.bean.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author harin-黑医
 * @version 1.0
 * @date 2022-03-01 17:17:44
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ChatGroupUserMap {
    private Integer gId;

    private String uId;

    private String status;
}

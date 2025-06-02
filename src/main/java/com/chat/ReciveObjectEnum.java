package com.chat;

/**
 * @author Harin-黑医
 * @version 1.0
 * @date 2022-03-01 16:42:11
 */
public enum ReciveObjectEnum {

    NORMAL("N","好友聊天"),
    GROUP("G","群聊天");


    private String typeCode;
    private String typeName;

    ReciveObjectEnum(String typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

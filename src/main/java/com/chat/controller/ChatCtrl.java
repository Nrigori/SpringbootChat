package com.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.chat.bean.Login;
import com.chat.bean.chat.ChatFriends;
import com.chat.bean.chat.ChatGroupInfo;
import com.chat.bean.chat.ChatGroupUserMap;
import com.chat.bean.chat.ChatMsg;
import com.chat.service.ChatFriendsService;
import com.chat.service.ChatGroupService;
import com.chat.service.ChatMsgService;
import com.chat.service.LoginService;
import com.chat.util.EmojiFilter;
import com.chat.util.exception.R;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatCtrl {
    @Value("${file.path}")
    private String pathStr;

    @Autowired
    ChatFriendsService chatFriendsService;
    @Autowired
    ChatMsgService chatMsgService;
    @Autowired
    LoginService loginService;

    @Autowired
    ChatGroupService chatGroupService;

    /**
     * 上传聊天图片
     * **/
    @PostMapping(value = "/chat/upimg")
    @ResponseBody
    public JSONObject upauz(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        LocalDate today = LocalDate.now();
        Instant timestamp = Instant.now();
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String filenames = today + String.valueOf(timestamp.toEpochMilli()) + "."+ext;
        File path = new File(pathStr);
        if(!path.exists()){
            path.mkdir();
        }
        file.transferTo(new File(pathStr+ File.separator + filenames));
        resUrl.put("src", "/pic/" + filenames);
        res.put("msg", "");
        res.put("code", 0);
        res.put("data", resUrl);
        return res;
    }
    /**
     * 添加好友：查询用户
     * */
    @PostMapping("/chat/lkuser/{username}")
    @ResponseBody public R lkuser(@PathVariable("username")String username){
        username= EmojiFilter.filterEmoji(username);
        String uid = loginService.lkUseridByUsername(username);
        if(uid==null){
            return R.error().message("未查询到此用户");
        }
        return R.ok().data("userinfo",chatFriendsService.LkUserinfoByUserid(uid)).message("用户信息");
    }
    /**
     * 添加好友
     * */
    @PostMapping("/chat/adduser/{fuserid}")
    @ResponseBody public R tofuseridchat(@PathVariable("fuserid")String fuserid,HttpSession session){
        String userid=(String)session.getAttribute("userid");
        if(userid.equals(fuserid)){
            return R.error().message("不能添加自己为好友");
        }
        ChatFriends chatFriends=new ChatFriends();
        chatFriends.setUserid(userid).setFuserid(fuserid);
        Integer integer = chatFriendsService.JustTwoUserIsFriend(chatFriends);
        if(integer==null){
            //如果不存在好友关系插入好友关系
            chatFriendsService.InsertUserFriend(chatFriends);
            chatFriendsService.InsertUserFriend(new ChatFriends().setFuserid(userid).setUserid(fuserid));
        }
        return R.ok().message("添加成功");
    }

    /**
     * 添加群：查询群
     * */
    @PostMapping("/chat/lkGroupInfo/{groupNumber}")
    @ResponseBody public R lkGroupInfo(@PathVariable("groupNumber")String groupNumber){
        ChatGroupInfo groupInfo = chatGroupService.selectByGroupNumber(groupNumber);
        if(groupInfo == null){
            return R.error().message("未查询到群信息");
        }
        return R.ok().data("groupInfo",groupInfo).message("群组信息");
    }

    /**
     * 加入群
     */
    @PostMapping("/chat/joinGroup/{groupId}")
    @ResponseBody public R joinGroup(@PathVariable("groupId")Integer groupId,HttpSession session){
        //ChatGroupInfo groupInfo = chatGroupService.selectByGroupNumber(groupNumber);
        String userId = (String)session.getAttribute("userid");
        ChatGroupInfo groupInfo  = chatGroupService.selectByPrimaryKey(groupId);
        Login login = loginService.selectByUserId(userId);
        if(groupInfo == null){
            return R.error().message("未查询到群信息");
        }
        if(login == null){
            return R.error().message("未查询到当前登录用户的信息");
        }
        ChatGroupUserMap chatGroupUserMap = new ChatGroupUserMap();
        chatGroupUserMap.setGId(groupInfo.getId());
        chatGroupUserMap.setUId(login.getUserid());

        ChatGroupUserMap oldMap = chatGroupService.selectGroupUserMap(chatGroupUserMap);
        if(oldMap!= null){
            return R.error().message("你已加入该群,不用再加入啦!");
        }

        Integer result = chatGroupService.addGroupUserMap(chatGroupUserMap);

        return result > 0 ? R.ok().data("groupInfo",groupInfo).message("加入群成功!!") : R.error().message("加入群失败!");
    }


    /**
     * 跳转到聊天
     * */
    @GetMapping("/chat/ct")
    public String tochat(){
        return "/chat/chats";
    }

    /***
     * 查询用户的好友
     * */
    @PostMapping("/chat/lkfriends")
    @ResponseBody
    public List<ChatFriends> lkfriends(HttpSession session){
        String userid=(String)session.getAttribute("userid");
        return chatFriendsService.LookUserAllFriends(userid);
    }

    /**
     * 查询用户加入的群
     */
    @PostMapping("/chat/getMyGroup")
    @ResponseBody
    public List<ChatGroupInfo> getMyGroup(HttpSession session){
        String userid=(String)session.getAttribute("userid");
        List<ChatGroupInfo> groupIdList = chatGroupService.selectMyGroup(userid);
        return groupIdList;
    }

    /***
     * 查询两个用户之间的聊天记录
     * */
    @PostMapping("/chat/lkuschatmsg/{reviceuserid}")
    @ResponseBody public List<ChatMsg> lkfriends(HttpSession session, @PathVariable("reviceuserid")String reviceuserid){
        String userid=(String)session.getAttribute("userid");
        return chatMsgService.LookTwoUserMsg(new ChatMsg().setSenduserid(userid).setReciveuserid(reviceuserid));
    }

    /***
     * 查询一个群内的聊天记录
     * */
    @PostMapping("/chat/getGroupMessage/{groupId}")
    @ResponseBody public List<ChatMsg> getGroupMessage(HttpSession session, @PathVariable("groupId")String reviceuserid){
        String userid=(String)session.getAttribute("userid");
        return chatMsgService.LookGroupMsg(new ChatMsg().setSenduserid(userid).setReciveuserid(reviceuserid));
    }




    /***
     * Ajax上传web界面js录制的音频数据
     * */
    @PostMapping("/chat/audio")
    @ResponseBody
    public JSONObject upaudio(@RequestParam(value = "file") MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        LocalDate today = LocalDate.now();
        Instant timestamp = Instant.now();
        String filenames = today  + String.valueOf(timestamp.toEpochMilli()) + ".mp3";
        String pathname = "D:\\chat\\" + filenames;
        file.transferTo(new File(pathname));
        resUrl.put("src", "/pic/"+filenames);
        res.put("msg", "");
        res.put("data", resUrl);
        return res;
    }
}

package com.driver;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class WhatsappService {
    @Autowired
    WhatsappRepository wr;
    List<User> usersList;
    static int GroupCount=1;
    static int msgCount=1;
    public String createUser(String name, String mobile) {
        if(!wr.getUserMobile().contains(mobile)) {
            User user = new User(name, mobile);
            wr.getUserMobile().add(mobile);
            usersList.add(user);
            return "SUCCESS";
        }
        throw new IllegalArgumentException("Mobile number already exists in the database");
    }

    public Group createGroup(List<User> users) {
        String groupName = "";
        if (users.size() < 2) {
            throw new IllegalArgumentException("Creation of group not Possible");
        } else if (users.size() > 2) {
            String GroupName = "Group " + wr.getCustomGroupCount()+1;
            if (!wr.getGroupUserMap().containsKey(GroupName)) {
                Group group = new Group(GroupName, users.size());
                wr.getGroupUserMap().put(group,users);
                wr.setCustomGroupCount(wr.getCustomGroupCount()+1);
                wr.getAdminMap().put(group,users.get(0));

            }
            else {
                throw new IllegalArgumentException("Invalid group");
            }
        }
        else if (users.size() == 2) {
            String GroupName = users.get(1).getName();
            if (!wr.getGroupUserMap().containsKey(GroupName)) {
                Group group = new Group(GroupName, users.size());
                wr.getGroupUserMap().put(group,users);
                wr.getAdminMap().put(group,users.get(0));
            }
            else {
                throw new IllegalArgumentException("Invalid group");
            }
        }
        return null;

    }
    public int createMessage(String content) {
        Message message=new Message(wr.getMessageId()+1,content,new java.util.Date());
        wr.getMessageHashMap().put(wr.getMessageId()+1,message);
        wr.setMessageId(wr.getMessageId()+1);
        return wr.getMessageId();
    }

    public int sendMessage(Message message, User sender, Group group) {
        if(!wr.getUserMobile().contains(sender.getMobile())){
            throw new IllegalArgumentException("user does not exist");
        }
        if(!wr.getGroupUserMap().containsKey(group)){
            throw new IllegalArgumentException("Group does not exist");
        }
        else{
            boolean flag=false;
            List<User> list=wr.getGroupUserMap().get(group);
            for(User u:list){
                if(u.equals(sender)){
                    flag=true;
                }
            }
            if(!flag){
                throw new IllegalArgumentException("You are not allowed to send message");
            }
        }
        List<Message> l=new LinkedList<Message>();
        if(wr.getGroupMessageMap().get(group)!=null){
            l=wr.getGroupMessageMap().get(group);
            l.add(message);
        }
        else {
            l.add(message);
            wr.getGroupMessageMap().put(group,l);
        }
        return message.getId();
    }

    public String changeAdmin(User approver, User user, Group group) {
        if(!wr.getAdminMap().containsKey(group)){
            throw new IllegalArgumentException("Group does not exist");
        }
        if(!wr.getAdminMap().get(group).equals(approver)){
            throw new  IllegalArgumentException("Approver does not have rights");
        }
        if (wr.getAdminMap().containsKey(group)){
            boolean flag=false;
            for(User u:wr.getGroupUserMap().get(group)){
                if(u.equals(user)){
                    flag=true;
                }
                if(!flag){
                    throw new IllegalArgumentException("User is not a participant");
                }
                else{
                    wr.getAdminMap().put(group,user);
                    return "SUCCESS";
                }
            }
        }
        return "fail";
    }

    public int removeUser(User user) {
        return 0;
    }

    public String findMessage(Date start, Date end, int k) {
        return null;
    }
}

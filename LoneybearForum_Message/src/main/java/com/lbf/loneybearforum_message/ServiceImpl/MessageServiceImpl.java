package com.lbf.loneybearforum_message.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbf.loneybearforum_message.Beans.MessageBean;
import com.lbf.loneybearforum_message.Controller.Message;
import com.lbf.loneybearforum_message.Mapper.MessageMapper;
import com.lbf.loneybearforum_message.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageBean> implements MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Override
    public List<Map<String, Object>> getMessageByUidAndType(Long uid, int type) {
        return null;
    }

    @Override
    public List getAllMessagesByUid(Long uid) {
        return messageMapper.selectList(new QueryWrapper<MessageBean>().eq("uid",uid).orderByAsc("time"));
    }

}

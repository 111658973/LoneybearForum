package com.lbf.loneybearforum_message.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbf.loneybearforum_message.Beans.MessageBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<MessageBean> {
}

package com.lbf.pack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbf.pack.beans.UserFullDataBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDataMapper extends BaseMapper<UserFullDataBean> {}

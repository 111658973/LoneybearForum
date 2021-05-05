package com.lbf.pack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.lbf.pack.beans.UserJsonStoreBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserJsonMapper extends BaseMapper<UserJsonStoreBean> {
}

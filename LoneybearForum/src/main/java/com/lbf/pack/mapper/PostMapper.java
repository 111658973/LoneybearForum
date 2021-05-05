package com.lbf.pack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbf.pack.beans.PostBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<PostBean> {
}

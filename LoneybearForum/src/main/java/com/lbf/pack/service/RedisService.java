package com.lbf.pack.service;


import com.lbf.pack.beans.ResponseBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface RedisService {
    /**
     * 插入一个新的hashmap
     * @param map map要存的map
     * @param expire 失效时间
     * @return
     */
    public Map<String,Object> insertMap(int tableId,String name,Map<String,Object> map,long expire);

    public Map<Object,Object> getMapByName(String name);

    public List<String> getKeys(String param);

    public List<Map<String,Object>> getListByKeys(List<String> keys);

    public List<String> scan(String matchKey);

    public ResponseBean deleteByName(String name);

    public ResponseBean insertMap(String name,Map<String,String> map,int expire);

}

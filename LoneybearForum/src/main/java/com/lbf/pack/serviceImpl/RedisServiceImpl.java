package com.lbf.pack.serviceImpl;

import com.lbf.pack.Util.TimeUtil;
import com.lbf.pack.beans.ResponseBean;
import com.lbf.pack.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    RedisTemplate<String,String> redisTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Map<String, Object> insertMap(int tableId,String name,Map<String, Object> map,long expire) {

//        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
//        DefaultStringRedisConnection stringRedisConnection = new DefaultStringRedisConnection(redisConnection);
//        stringRedisConnection.select(tableId);
        Map<String,Object> a = new HashMap<>();

        Map<String,Object> returnJSon = new HashMap<>();
        redisTemplate.opsForHash().putAll(name,map);
        redisTemplate.expire(name,expire, TimeUnit.MINUTES);
        returnJSon.put("code",200);
        returnJSon.put("msg","储存成功");
        return returnJSon;
    }

    @Override
    public Map<Object, Object> getMapByName(String name) {
        Map<Object, Object> values = redisTemplate.opsForHash().entries(name);
        return values;
    }

    @Override
    public List<String> getKeys(String param) {
        Set<String> keys = stringRedisTemplate.keys(param + "*");
        return new ArrayList<>(keys);
    }

    public List<String> scan(String matchKey) {
        Set<String> keys = redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match("*" + matchKey + "*").count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });

        return new ArrayList<>(keys);
    }




    @Override
    public List<Map<String, Object>> getListByKeys(List<String> keys) {
        List list = new ArrayList<>();
        for(String s:keys){
            Map<Object, Object> values = redisTemplate.opsForHash().entries(s);
            Long expire = redisTemplate.getExpire(s);
            values.put("expire_days",new TimeUtil().secondToDay(expire));
//            values.put("expire_minute",expire/60);
            values.put("expire_second",expire);
            values.put("until",new TimeUtil().getExpireTime(Integer.parseInt(expire.toString())));
            list.add(values);
        }

        return list;
    }

    @Override
    public ResponseBean insertMap(String name,Map<String,String> map,int expire) {
        redisTemplate.opsForHash().putAll(name,map);
        redisTemplate.expire(name,expire, TimeUnit.MINUTES);
        return new ResponseBean(200,"操作成功",null);
    }

    @Override
    public ResponseBean deleteByName(String name) {
        redisTemplate.delete(name);
        return new ResponseBean(200,"操作成功","删除redis元素成功");
    }
}

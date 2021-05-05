package com.lbf.pack.beans;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HtmlBean {
    private String tag;
    private List<Map<String,String>> attrs;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<JSONObject> childern;
}

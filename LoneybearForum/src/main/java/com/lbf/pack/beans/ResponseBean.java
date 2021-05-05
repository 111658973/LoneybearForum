package com.lbf.pack.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseBean {
    private int code;
    private String msg;
    private String detail;
}

package com.yueyue.netty.servicerfactory;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gq.wang
 * @version 1.0
 * @description: ${description}
 * @create: 2019-11-06 21:52
 */
@Component
public interface ServiverFactory {

    public Map<String,Object> doBiz();

}

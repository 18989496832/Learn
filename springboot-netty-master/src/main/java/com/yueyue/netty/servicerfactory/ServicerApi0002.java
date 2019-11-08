package com.yueyue.netty.servicerfactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gq.wang
 * @version 1.0
 * @description: ${description}
 * @create: 2019-11-06 21:57
 */
@Service
@Slf4j
public class ServicerApi0002 implements ServiverFactory {

    @Override
    public Map<String, Object> doBiz() {
        System.out.println("=========0002子服务执行了==========");
        Map retMap = new HashMap();
        retMap.put("resultId","0000");
        retMap.put("resultDesc","交易成功");
        return retMap;
    }
}

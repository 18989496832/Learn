package com.yueyue.netty.bizfactory;

import com.yueyue.netty.servicerfactory.ServiverFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gq.wang
 * @version 1.0
 * @description: ${description}
 * @create: 2019-11-06 21:31
 */
@Component
public class BizFactory implements ApplicationContextAware {
    private static ApplicationContext context;
    private Map retMap;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context =applicationContext;
    }
    public static Object getBean(String name){
        return context.getBean(name);
    }
    /***
     * 1.解包，确认业务种类
     *
     * 2.动态拼接需要处理的业务
     *
     * 3.返回业务处理结果
     *
     * 4.组包返回
     */
    public Map doBizModel(String tranId){
        String servicerBeanName = "servicerApi"+tranId;
        System.out.println("servicerBeanName:"+servicerBeanName);
        ServiverFactory servicerObj= (ServiverFactory) getBean(servicerBeanName);
        retMap = servicerObj.doBiz();
        return retMap;
    }

}

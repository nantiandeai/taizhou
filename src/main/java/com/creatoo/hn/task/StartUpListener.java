package com.creatoo.hn.task;

import com.creatoo.hn.services.home.userCenter.BlackListService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**启动项监听器
 * Created by caiyong on 2017/6/9.
 */
@Component("StartUpListener")
public class StartUpListener implements ApplicationContextAware {

    private static Logger logger = Logger.getLogger(StartUpListener.class);

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private BlackListService blackListService;

    public void setBlackListService(BlackListService blackListService) {
        this.blackListService = blackListService;
    }

    public BlackListService getBlackListService() {
        return blackListService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        scheduledExecutorService.scheduleAtFixedRate(myRunner, 3600, 3600, TimeUnit.SECONDS);
    }

    private Runnable myRunner = new Runnable() {
        @Override
        public void run() {
            try {
                if(null != blackListService){
                    blackListService.doTask();
                }
            }catch (Exception e){
                logger.error(e.toString());
                //如果发生异常，则停止线程池，以免内存泄露
                scheduledExecutorService.shutdown();
            }
        }
    };
}

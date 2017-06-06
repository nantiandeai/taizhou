package com.creatoo.hn.task.quartz;

import java.time.LocalDateTime;

/**
 * Created by caiyong on 2017/6/6.
 */
public class BlackListTask {
    public void doJob(){
        LocalDateTime now = LocalDateTime.now();
        System.out.println("hello world + " + now.toString());
    }
}

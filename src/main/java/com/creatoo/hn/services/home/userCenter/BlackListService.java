package com.creatoo.hn.services.home.userCenter;

import com.creatoo.hn.mapper.WhScanCollectionMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**黑名单服务
 * Created by caiyong on 2017/6/9.
 */
public class BlackListService {

    private static Logger logger = Logger.getLogger(BlackListService.class);

    @Autowired
    private WhScanCollectionMapper whScanCollectionMapper;

    /**
     * 执行工作
     */
    public void doTask(){
        /**
         * 执行步骤：
         * 1、扫描违规
         * 2、判定违规
         * 3、违规判定处理
         */
    }

    private void doScan(){

    }

    private void doJudge(){

    }

    private void doJudgeHandle(){

    }

}

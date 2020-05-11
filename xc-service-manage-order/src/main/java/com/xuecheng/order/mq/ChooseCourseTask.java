package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.service.XcTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @Auther : shindou
 * @Date : 2019/7/19
 * @Description : 功能描述
 * @Version : 1.0
 */

@Component
public class ChooseCourseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    XcTaskService xcTaskService;

    //每隔1分钟定时扫描消息表，向MQ发送消息
    @Scheduled(fixedDelay = 60000)
    public void sendChooseCourseTask(){

        //取出当前时间前一分钟的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE,-1);
        Date time = calendar.getTime();
        List<XcTask> taskList = xcTaskService.findTaskList(time, 1000);

        //遍历任务列表
        for(XcTask xcTask:taskList){
            //发送选课消息
            xcTaskService.publish(xcTask,xcTask.getMqExchange(),xcTask.getMqRoutingkey());
            LOGGER.info("send choose course task id:{}",xcTask.getId());
        }

    }
}

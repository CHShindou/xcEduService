package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Auther : shindou
 * @Date : 2019/7/19
 * @Description : 功能描述
 * @Version : 1.0
 */

@Service
public class XcTaskService {

    @Autowired
    XcTaskRepository xcTaskRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    public List<XcTask> findTaskList(Date updateTime,int n){

        //设置分页参数
        Pageable pageable = new PageRequest(0,n);
        Page<XcTask> xcTaskPage = xcTaskRepository.findByUpdateTimeBefore(pageable, updateTime);
        return xcTaskPage.getContent();

    }


    //发送消息

    /**
     * @param xcTask  任务对象
     * @param ex      交换机
     * @param routingKey   路由KEY
     */
    @Transactional
    public void publish(XcTask xcTask,String ex,String routingKey){
        Optional<XcTask> optional = xcTaskRepository.findById(xcTask.getId());
        if(optional.isPresent()){
            xcTask = optional.get();
            rabbitTemplate.convertAndSend(ex,routingKey,xcTask);
            //更新任务时间为当前时间
            xcTask.setUpdateTime(new Date());
            xcTaskRepository.save(xcTask);
        }

    }


    /**
     * @Description :使用乐观锁校验任务
     * @param  :
     * @return :
     * @auther : shindou
     * @date   : 2019/7/28 23:56
     */
    @Transactional
    public int getTask(String id,int version){
        return xcTaskRepository.updateTaskVersion(id,version);
    }
}

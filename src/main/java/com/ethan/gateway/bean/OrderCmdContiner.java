package com.ethan.gateway.bean;

import com.google.common.collect.Lists;
import thirdpart.order.OrderCmd;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class OrderCmdContiner {
    private static OrderCmdContiner ourInstance = new OrderCmdContiner();

    private OrderCmdContiner(){}

    public static OrderCmdContiner getInstance(){
        return ourInstance;
    }

    ///////////////////////////

    private final BlockingDeque<OrderCmd> queue = new LinkedBlockingDeque<>();

    public boolean cache(OrderCmd cmd){
        return queue.offer(cmd);
    }

    public int size(){
        return queue.size();
    }

    public List<OrderCmd> getAll() {
        List<OrderCmd> msgList = Lists.newArrayList();
        int count = queue.drainTo(msgList);
        if(count == 0){
            return null;
        }else{
            return msgList;
        }
    }
}

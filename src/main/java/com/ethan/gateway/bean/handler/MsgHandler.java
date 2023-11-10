package com.ethan.gateway.bean.handler;

import com.ethan.gateway.bean.OrderCmdContiner;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import thirdpart.bean.CommonMsg;
import thirdpart.codec.IByteCodec;
import thirdpart.order.OrderCmd;


@Log4j2
@AllArgsConstructor
public class MsgHandler implements IMsgHandler {

    private IByteCodec bodyCodec;
    @Override
    public void onCounterData(CommonMsg msg) {
        OrderCmd orderCmd;

        try{
            orderCmd = bodyCodec.deseriallize(msg.getBody(),OrderCmd.class);
            log.info("recv cmd: {}",orderCmd);

            if(!OrderCmdContiner.getInstance().cache(orderCmd)){
                log.error("gateway queue insert fail,queue length:{},order:{}",OrderCmdContiner.getInstance().size(),orderCmd);
            }

        }catch (Exception e){
            log.error("decode order cmd error",e);
        }

    }
}

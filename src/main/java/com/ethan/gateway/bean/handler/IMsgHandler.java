package com.ethan.gateway.bean.handler;

import io.vertx.core.net.NetSocket;
import thirdpart.bean.CommonMsg;

public interface IMsgHandler {

    default void onConnect(NetSocket socket){};

    default void onDisConnect(NetSocket socket){};

    default void onException(NetSocket socket, Throwable e){};

    void onCounterData(CommonMsg msg);

}

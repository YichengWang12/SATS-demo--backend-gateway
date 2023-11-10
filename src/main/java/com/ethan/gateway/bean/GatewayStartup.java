package com.ethan.gateway.bean;

import lombok.extern.log4j.Log4j2;
import thirdpart.checksum.ByteCheckSum;
import thirdpart.codec.IByteCodecImpl;

@Log4j2
public class GatewayStartup {
    public static void main(String[] args) throws Exception{
        String configFileName = "gateway.xml";

        GatewayConfig config = new GatewayConfig();
        config.initConfig(GatewayStartup.class.getResource("/").getPath() + configFileName);

        config.setCheckSum(new ByteCheckSum());
        config.setByteCodec(new IByteCodecImpl());
        config.startup();
    }
}

package com.ethan.gateway.bean;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.ethan.gateway.bean.handler.ConnHandler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import thirdpart.checksum.ByteCheckSum;
import thirdpart.checksum.ICheckSum;
import thirdpart.codec.IByteCodec;
import thirdpart.fetchserv.IFetchService;

import java.io.File;


@Log4j2
@Getter
public class GatewayConfig {
    private short id; // gate way id(I guess)
    private int recvPort;

    //Provider to raft
    private int fetchServPort;

    //todo counter-connection   database-connection

    @Setter
    private IByteCodec byteCodec;

    @Setter
    private ICheckSum checkSum;


    private Vertx vertx = Vertx.vertx();

    public void initConfig(String fileName) throws Exception{
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(fileName));
        Element root = document.getRootElement();

        //parse
        id = Short.parseShort(root.element("id").getText());

        recvPort = Integer.parseInt(root.element("recvport").getText());


        log.info("GateWay ID:{}, Port:{}",id,recvPort);

        fetchServPort = Integer.parseInt(root.element("fetchservport").getText());
        log.info("Gateway ID:{}, Port: {},FetchServPort:{}",id,recvPort,fetchServPort);
        //todo counter-parse  database-parse
    }

    public void startup() throws Exception{
        //1. start tcp listening
        initRecv();

        //2. queue machine interaction
        initFetchRecv();
    }

    private void initFetchRecv() {
        //start rpc server
        ServerConfig rpcConfig = new ServerConfig()
                .setPort(fetchServPort)
                .setProtocol("bolt");

        ProviderConfig<IFetchService> providerConfig = new ProviderConfig<IFetchService>()
                .setInterfaceId(IFetchService.class.getName())
                .setRef(() -> OrderCmdContiner.getInstance().getAll())
                .setServer(rpcConfig);
        providerConfig.export();
        log.info("gateway startup fetchServ success at port:{}",fetchServPort);
    }

    public void initRecv(){
        NetServer server = vertx.createNetServer();
        server.connectHandler(new ConnHandler(this));
        server.listen(recvPort,res->{
            if(res.succeeded()){
                log.info("Gateway startup success at port:{}",recvPort);
            }else{
                log.error("Gateway startup failed!");
            }
        });
    }


}

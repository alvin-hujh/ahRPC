package cn.hjh.ahrpc.demo.provider;

import cn.hjh.ahrpc.core.api.RpcResponse;
import cn.hjh.ahrpc.core.api.RpcRequest;
import cn.hjh.ahrpc.core.provider.ProviderBootstrap;
import cn.hjh.ahrpc.core.provider.ProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Import({ProviderConfig.class})
public class AhrpcDemoProviderApplication {
    @Autowired
    ProviderBootstrap providerBootstrap;

    public static void main(String[] args) {
        SpringApplication.run(AhrpcDemoProviderApplication.class, args);
    }

//    使用 HTTP 协议 + JSON 实现序列化和通信

    @RequestMapping("/")
    public RpcResponse invoke(@RequestBody RpcRequest request) {
        return providerBootstrap.invoke(request);
    }

    @Bean
    ApplicationRunner providerRun() {
        return x -> {
//            RpcRequest request = new RpcRequest();
//            request.setService("cn.hjh.ahrpc.demo.api.UserService");
//            request.setMethod("findById");
//            request.setArgs(new Object[]{100});
//
//            RpcResponse rpcResponse = providerBootstrap.invoke(request);
//            System.out.println("return:" + rpcResponse.getData());
        };
    }

}

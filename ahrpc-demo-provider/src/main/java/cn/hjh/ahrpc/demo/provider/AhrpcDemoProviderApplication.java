package cn.hjh.ahrpc.demo.provider;

import cn.hjh.ahrpc.core.api.RpcRequest;
import cn.hjh.ahrpc.core.api.RpcResponse;
import cn.hjh.ahrpc.core.provider.ProviderConfig;
import cn.hjh.ahrpc.core.provider.ProvierInvoker;
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
    private ProvierInvoker provierInvoker;

    public static void main(String[] args) {
        SpringApplication.run(AhrpcDemoProviderApplication.class, args);
    }

//    使用 HTTP 协议 + JSON 实现序列化和通信

    @RequestMapping("/")
    public RpcResponse invoke(@RequestBody RpcRequest request) {
        return provierInvoker.invoke(request);
    }

    @Bean
    ApplicationRunner providerRun() {
        return x -> {
            RpcRequest request = new RpcRequest();
            request.setService("cn.hjh.ahrpc.demo.api.UserService");
            request.setMethodSign("findById@1_class java.lang.Integer");
            request.setArgs(new Object[]{100});

            RpcResponse rpcResponse = provierInvoker.invoke(request);
            System.out.println("return:" + rpcResponse.getData());
//            test 2 parameters
            RpcRequest request2 = new RpcRequest();
            request2.setService("cn.hjh.ahrpc.demo.api.UserService");
            request2.setMethodSign("findById@2_class java.lang.Integer_class java.lang.String");
            request2.setArgs(new Object[]{198,"yj"});

            RpcResponse rpcResponse2 = provierInvoker.invoke(request2);
            System.out.println("return:" + rpcResponse2.getData());
        };
    }

}

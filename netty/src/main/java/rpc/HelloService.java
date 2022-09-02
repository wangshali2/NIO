package rpc;

//这个是接口，是服务提供方和 服务消费方都需要
public interface HelloService {

    String hello(String mes);

    default String hello1(String mes) {
        return "";
    }
}


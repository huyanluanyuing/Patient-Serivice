package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

// @Component 让 Spring 能够扫描到这个类，并把它注册到容器中
@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    // 这是一个 HTTP 客户端，专门用来发起网络请求（类似于 Postman 的代码版）
    private final WebClient webClient;

    // 构造函数：Spring 启动时会执行这里
    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${auth.service.url}") String authServiceUrl) {
        // webClientBuilder 是 Spring 自动提供的工具
        // baseUrl(authServiceUrl): 设置基础地址（例如 http://auth-service:4005）
        // build(): 创建好这个客户端实例
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    // apply 方法：这是过滤器的核心逻辑，每次有请求经过时都会调用
    @Override
    public GatewayFilter apply(Object config) {
        // Lambda 表达式：返回一个 GatewayFilter 对象
        // exchange: 当前的请求上下文（包含 Request 和 Response）
        // chain: 过滤器链（用来把请求传递给下一个过滤器）
        return (exchange, chain) -> {

            // 1. 从请求头中提取 Authorization 字段
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // 2. 简单的非空校验
            if (token == null || !token.startsWith("Bearer ")) {
                // 如果没有 Token，直接设置状态码为 401 Unauthorized
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                // setComplete() 表示请求到此结束，不再往后传了
                return exchange.getResponse().setComplete();
            }

            // 3. 使用 WebClient 发起异步请求去验证 Token
            return webClient.get()                  // 发起 GET 请求
                    .uri("/validate")               // 请求路径，拼接在 baseUrl 后面
                    .header(HttpHeaders.AUTHORIZATION, token) // 把 Token 原样传给 Auth Service
                    .retrieve()                     // 发送请求并开始接收响应
                    .toBodilessEntity()             // 我们只关心状态码（200或401），不关心返回值的内容

                    // 4. 处理响应结果
                    // .then(...) 意味着：只有当上面的 webClient 请求成功（返回 200 OK）时，才执行这一步
                    .then(chain.filter(exchange));

            // 注意：如果 webClient 收到 401/403 错误，它会自动抛出异常，
            // 这里的 .then() 就不会执行，请求链中断，用户会收到错误提示。
        };
    }
}

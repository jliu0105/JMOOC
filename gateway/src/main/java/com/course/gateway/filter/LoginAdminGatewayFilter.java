package com.course.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAdminGatewayFilter implements GatewayFilter, Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(LoginAdminGatewayFilter.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // If the request address does not contain /admin/, it is not a console request and does not need to be intercepted
        if (!path.contains("/admin/")) {
            return chain.filter(exchange);
        }
        if (path.contains("/system/admin/user/login")
                || path.contains("/system/admin/user/logout")
                || path.contains("/system/admin/kaptcha")) {
            LOG.info("Console login verification is not required:{}", path);
            return chain.filter(exchange);
        }
        // Get the token parameter of the header
        String token = exchange.getRequest().getHeaders().getFirst("token");
        LOG.info("Console login verification starts, token:{}", token);
        if (token == null || token.isEmpty()) {
            LOG.info( "token is empty, the request is intercepted" );
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        Object object = redisTemplate.opsForValue().get(token);
        if (object == null) {
            LOG.warn( "token is invalid, the request is blocked" );
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } else {
            LOG.info("logged in：{}", object);

            //Add permission verification, there is no LoginUserDto in the gateway, so all operations are done with JSON 
            LOG.info("Interface permission verification, request address:{}", path);
            boolean exist = false;
            JSONObject loginUserDto = JSON.parseObject(String.valueOf(object));
            redisTemplate.opsForValue().set(token, JSON.toJSONString(loginUserDto), 3600, TimeUnit.SECONDS);
            JSONArray requests = loginUserDto.getJSONArray("requests");
            //  Traverse all [Permission Requests] to determine whether the address currently requested is in [Permission Requests]
            for (int i = 0, l = requests.size(); i < l; i++) {
                String request = (String) requests.get(i);
                if (path.contains(request)) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                LOG.info("Permission verification passed");

                if (path.contains("/save")
                        || path.contains("/delete")) {
                    LOG.info("The example website is temporarily not open for adding, deleting and modifying operations:{}", path);
                    exchange.getResponse().setStatusCode(HttpStatus.EXPECTATION_FAILED);
                    return exchange.getResponse().setComplete();
                }

            } else {
                LOG.warn("Permission verification failed");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder()
    {
        return 1;
    }
}

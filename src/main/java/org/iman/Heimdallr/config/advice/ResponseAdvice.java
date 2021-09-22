/**
 * 
 */
package org.iman.Heimdallr.config.advice;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.UUID;

import org.iman.Heimdallr.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author ey
 * @param <T>
 *
 */
@ControllerAdvice
public class ResponseAdvice<T> implements ResponseBodyAdvice<Object> {

    @Autowired
    private Environment env;

    @Override
    public boolean supports(MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        // Start
        String uri = request.getURI().toString();
        if (uri.indexOf("/login/account") >= 0 || uri.indexOf("/currentUser") >= 0
                || uri.indexOf("/notices") >= 0) {
            return body;
        }
        Response<T> resp = (Response<T>) body;
        // 这段代码因为有ant-pro自己的接口，所以要特殊处理下，等把ant-pro的接口彻底剔除干净了，
//        或者能把特殊处理的这几个接口能给转成现有的返回标准，这段代码就可以删掉了
        // End
        resp.mkTime();

        String port = env.getProperty("local.server.port");
        String host = "";
        try {
            host = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e1) {
            host = "unknown";
        }
        host = host + ":" + port;
        resp.setHost(host);
        resp.setTraceId(UUID.randomUUID().toString());
        return resp;
    }

}

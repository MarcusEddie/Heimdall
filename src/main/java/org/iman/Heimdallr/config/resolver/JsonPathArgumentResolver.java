package org.iman.Heimdallr.config.resolver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.iman.Heimdallr.config.annotations.JsonParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonPathArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String ATTRIBUTE = "JSON_REQUEST_BODY";

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String jsonBody = getRequestBody(webRequest);

        JsonNode rootNode = mapper.readTree(jsonBody);
        JsonNode node = rootNode.path(parameter.getParameterName());
        return mapper.readValue(node.toString(), parameter.getParameterType());
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        String body = (String) webRequest.getAttribute(ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        if (null == body) {
            try {
                body = IOUtils.toString(servletRequest.getInputStream());
                webRequest.setAttribute(ATTRIBUTE, body, NativeWebRequest.SCOPE_REQUEST);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        return body;
    }

}

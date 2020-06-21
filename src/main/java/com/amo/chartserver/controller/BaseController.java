package com.amo.chartserver.controller;

import com.alibaba.fastjson.JSON;
import com.amo.chartserver.util.SpringUtil;
import com.amo.chartserver.util.exception.BusinessException;
import com.amo.chartserver.view.RequestView;
import com.amo.chartserver.view.ResponseView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("amo")
public class BaseController {

    @Value("${server.port:8080}")
    private int port;


    @Autowired
    private SpringUtil springUtil;

    private static Map<String, HandlerMethod> urlMap = new HashMap<String, HandlerMethod>();

    @PostConstruct
    public void getAllRequestMap() {

        RequestMappingHandlerMapping mapping = springUtil.getBean(RequestMappingHandlerMapping.class);

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            PatternsRequestCondition condition = entry.getKey().getPatternsCondition();
            for (String pattern : condition.getPatterns()) {
                urlMap.put(pattern, entry.getValue());
            }
        }
    }

    @RequestMapping("doAction")
    public Map<String, ResponseView> doAction(@RequestBody List<RequestView> requests) {

        Map<String, ResponseView> responseMap = new HashMap<>();

        ExecutorService executor = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors());

        for (RequestView request : requests) {
            try {

                Future<ResponseView> future = executor.submit(() -> {
                    String url = request.getUrl();
                    if (!url.startsWith("/")) url = "/" + url;
                    if (url.endsWith("/")) url = url.substring(0, url.length() - 2);

                    HandlerMethod method = urlMap.get(url);

                    if (method == null) {
                        return ResponseView.getFailedView("请检查请求路径 !!");
                    } else {
                        try {

                            Class<?>[] types = method.getMethod().getParameterTypes();
                            Object[] params = request.getParams();
                            Object[] objects = new Object[types.length];
                            for (int i = 0; i < types.length; i++) {
                                if (i < params.length) {
                                    Class<?> type = types[i];
                                    objects[i] = JSON.parseObject(JSON.toJSONString(params[i]), type);
                                } else {
                                    objects[i] = null;
                                }
                            }

                            return (ResponseView) method.getMethod().invoke(SpringUtil.getBean(method.getBeanType()), objects);
                        } catch (InvocationTargetException e) {
                            if (e.getTargetException() instanceof BusinessException) {
                                return ResponseView.getFailedView(e.getTargetException().getMessage());
                            } else {
                                e.getTargetException().printStackTrace();
                                return ResponseView.getFailedView("服务端调用出错 !!");
                            }
                        }
                    }
                });

                responseMap.put(request.getKey(), future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return responseMap;
    }

}

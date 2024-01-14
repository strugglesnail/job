package com.frontier.job.core.handler;

import java.lang.reflect.Method;

public class MethodJobHandler implements JobHandler {


    private final Object target;
    private final Method method;

    public MethodJobHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public void execute() throws Exception {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length > 0) {
            method.invoke(target, new Object[paramTypes.length]);       // method-param can not be primitive-types
        } else {
            method.invoke(target);
        }
    }
}

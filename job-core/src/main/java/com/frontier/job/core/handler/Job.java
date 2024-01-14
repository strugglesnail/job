package com.frontier.job.core.handler;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Job {

    // jobHandler name
    String value();
}

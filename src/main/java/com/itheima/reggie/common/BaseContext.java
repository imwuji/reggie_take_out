package com.itheima.reggie.common;

public class BaseContext {
    //线程隔离的   每次请求  对应一个线程
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setThreadLocal(Long id) {
        threadLocal.set(id);
    }

    public static Long getThreadLocal() {
        return threadLocal.get();
    }
}

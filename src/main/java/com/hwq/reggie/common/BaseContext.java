package com.hwq.reggie.common;

public class BaseContext {
    public static  ThreadLocal<Long> objectThreadLocal = new ThreadLocal<>();

    public static void setId(Long empId)
    {
        objectThreadLocal.set(empId);
    }
    public static Long getId()
    {
        return objectThreadLocal.get();
    }
}

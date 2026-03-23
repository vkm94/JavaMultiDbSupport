package com.multidb.demo.Config;

public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setRead() { contextHolder.set("READ_ONLY"); }
    public static void setWrite() { contextHolder.set("WRITE"); }
    public static String get() { return contextHolder.get(); }
    public static void clear() { contextHolder.remove(); }
}
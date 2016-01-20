package com.zhuanglide.proxyspider;

import com.zhuanglide.proxyspider.task.impl.XiciTask;
import com.zhuanglide.proxyspider.utils.ProxyCheck;

/**
 * Created by wwj on 16.1.13.
 */
public class Main {
    public static void main(String[] args) {
        new XiciTask().run();
        ProxyCheck.check();
    }
}

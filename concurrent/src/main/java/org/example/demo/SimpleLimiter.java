package org.example.demo;

import java.util.concurrent.TimeUnit;

/**
 * 简单限流器的实现：Guava实现令牌桶算法，就是【记录并动态计算下一令牌发放的时间】。
 * 	    1.只需要记录一个下一令牌产生的时间，并动态更新它，就能够轻松完成限流功能
 * 	    2.新产生的令牌的计算公式是：(now-next)/interval，
 *
 * 38 | 案例分析（一）：高性能限流器Guava RateLimiter
 * 令牌桶算法的详细描述如下：
 * 	1.令牌以固定的速率添加到令牌桶中，假设限流的速率是 [r/秒]，则令牌每 [1/r秒]会添加一个(本案例每秒增加一个令牌)；
 * 	2.假设令牌桶的容量是[b]，如果令牌桶已满，则新的令牌会被丢弃；(本案例令牌桶容量是3)
 * 	        b 其实是 burst 的简写，意义是限流器允许的最大突发流量
 * 	3.请求能够通过限流器的前提是 【令牌桶中有令牌】。
 *
 * @author: jiaolong
 * @date: 2024/07/04 11:22
 **/
class SimpleLimiter {
    // 当前令牌桶中的令牌数量
    long storedPermits = 0;
    // 令牌桶的容量
    long maxPermits = 3;
    // 下一令牌产生时间
    long next = System.nanoTime();
    // 发放令牌间隔：纳秒
    long interval = 1000_000_000;

    // 请求时间在下一令牌产生时间之后, 则
    // 1. 重新计算令牌桶中的令牌数
    // 2. 将下一个令牌发放时间重置为当前时间
    void resync(long now) {
        if (now > next) {
            // 新产生的令牌数
            long newPermits=(now-next)/interval;
            // 新令牌 增加到 令牌桶【计算当前令牌的数量】
            storedPermits=Math.min(maxPermits,
                    storedPermits + newPermits);
            // 将下一个令牌发放时间重置为当前时间
            next = now;
        }
    }
    // 预占令牌，返回能够获取令牌的时间
    synchronized long reserve(long now){
        resync(now);
        // 能够获取令牌的时间
        long at = next;

//-S如果有可用令牌，将无需等待直接使用，否则将等待一个间隔时间
        // 令牌桶中能提供的令牌  （storedPermits：当前令牌桶中的令牌数量）
        long fb = Math.min(1, storedPermits);
        // 令牌净需求：首先减掉令牌桶中的令牌
        long nr = 1 - fb;
        // 重新计算下一令牌产生时间
        next = next + nr*interval;
//-E如果有可用令牌，将无需等待直接使用，否则将等待一个间隔时间

        // 重新计算令牌桶中的令牌
        this.storedPermits -= fb;
        return at;
    }
    // 申请令牌-入口
    void acquire() {
        // 申请令牌时的时间
        long now = System.nanoTime();
        // 预占令牌
        long at = reserve(now);
        long waitTime = Math.max(at-now, 0);
        // 按照条件等待
        if(waitTime > 0) {
            try {
                TimeUnit.NANOSECONDS
                        .sleep(waitTime);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
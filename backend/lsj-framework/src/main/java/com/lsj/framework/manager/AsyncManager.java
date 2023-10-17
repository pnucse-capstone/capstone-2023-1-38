package com.lsj.framework.manager;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.lsj.common.utils.Threads;
import com.lsj.common.utils.spring.SpringUtils;

/**
 * 비동기 작업 관리자
 */
public class AsyncManager {
    /**
     * 작동 시 10밀리초 지연
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * 비동기 작업 스케줄링 스레드 풀
     */
    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    private AsyncManager() {}

    private static AsyncManager me = new AsyncManager();

    public static AsyncManager me() {
        return me;
    }

    /**
     * 업무 수행
     * @param task
     */
    public void execute(TimerTask task) {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 작업 스레드 풀 중지
     */
    public void shutdown() {
        Threads.shutdownAndAwaitTermination(executor);
    }
}

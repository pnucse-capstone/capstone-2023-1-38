package com.lsj.framework.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 응용 프로그램이 종료될 때 백그라운드 스레드를 닫을 수 있는지 확인
 */
@Component
public class ShutdownManager {
    private static final Logger logger = LoggerFactory.getLogger("sys-user");

    @PreDestroy
    public void destroy() {
        shutdownAsyncManager();
    }

    /**
     * 작업을 비동기적 실행 중지
     */
    private void shutdownAsyncManager() {
        try {
            logger.info("====백그라운드 작업 스레드 풀 닫기====");
            AsyncManager.me().shutdown();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}

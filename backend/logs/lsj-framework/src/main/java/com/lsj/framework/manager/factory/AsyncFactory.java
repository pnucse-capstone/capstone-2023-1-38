package com.lsj.framework.manager.factory;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lsj.common.utils.LogUtils;


public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 로그인 정보 기록
     * 메소드는 로그인 정보를 기록하는 TimerTask 객체를 생성하고 반납
     * @param username 사용자 이름
     * @param status   로그인 상태
     * @param message  로그인 메시지
     * @param args     리스트
     * @return 업무 task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
                                             final Object... args) {
        return new TimerTask() {
            @Override
            public void run() {
                StringBuilder s = new StringBuilder();//StringBuilder 객체 s를 생성하기, 创建StringBuilder对象s, 로그 정보 구축에 사용

                //사용자 이름, 로그인 상태 및 로그인 메시지와 같은 정보를 s에 문자열로 추가
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                //정보 로그에 기록하기
                sys_user_logger.info(s.toString(), args);
            }
        };
    }


}


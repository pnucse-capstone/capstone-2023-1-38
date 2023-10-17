package com.lsj.system.mapper;

import java.util.List;

import com.lsj.system.domain.SysUserPost;
import org.springframework.stereotype.Repository;

/**
 * 사용자 및 직위 관련표  데이터 층
 */
@Repository
public interface SysUserPostMapper {
    /**
     * 사용자 ID로 사용자 및 직위 관련을 삭제
     *
     * @param userId 사용자 ID
     * @return 결과
     */
    public int deleteUserPostByUserId(Long userId);

    /**
     * 직위 ID로 직위 사용량을 조회
     *
     * @param postId 직위 ID
     * @return 결과
     */
    public int countUserPostById(Long postId);

    /**
     * 여러 사용자 및 직위 관련을 삭제
     *
     * @param ids 삭제할 데이터 ID
     * @return 결과
     */
    public int deleteUserPost(Long[] ids);

    /**
     * 여러 사용자 직위 정보를 추가
     *
     * @param userPostList 사용자 역할 목록
     * @return 결과
     */
    public int batchUserPost(List<SysUserPost> userPostList);
}

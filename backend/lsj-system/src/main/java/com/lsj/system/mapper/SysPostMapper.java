package com.lsj.system.mapper;

import java.util.List;

import com.lsj.system.domain.SysPost;
import org.springframework.stereotype.Repository;

/**
 * 직위 정보 data layer
 */
@Repository
public interface SysPostMapper {
    /**
     * 직위 정보 집합 조회
     *
     * @param post 직위 정보
     * @return 직위 정보 집합
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * 모든 직위 조회
     *
     * @return 직위 목록
     */
    public List<SysPost> selectPostAll();

    /**
     * 직위 ID로 직위 정보를 조회
     *
     * @param postId 직위 ID
     * @return 역할 객체 정보
     */
    public SysPost selectPostById(Long postId);

    /**
     * 사용자 ID로 직위 선택 옵션 목록을 얻기
     *
     * @param userId 사용자 ID
     * @return 직위 ID 목록
     */
    public List<Integer> selectPostListByUserId(Long userId);

    /**
     * 사용자 속한 직위 목록
     *
     * @param userName 사용자
     * @return 결과
     */
    public List<SysPost> selectPostsByUserName(String userName);

    /**
     * 직위 정보 삭제
     *
     * @param postId 직위 ID
     * @return 결과
     */
    public int deletePostById(Long postId);

    /**
     * 여러 직위 정보 삭제
     *
     * @param postIds 삭제할 직위 ID
     * @return 결과
     */
    public int deletePostByIds(Long[] postIds);

    /**
     * 직위 정보 수정
     *
     * @param post 직위 정보
     * @return 결과
     */
    public int updatePost(SysPost post);

    /**
     * 직위 정보 추가
     *
     * @param post 직위 정보
     * @return 결과
     */
    public int insertPost(SysPost post);

    /**
     * 직위 이름 체크
     *
     * @param postName 직위 이름
     * @return 결과
     */
    public SysPost checkPostNameUnique(String postName);

    /**
     * 직위 번호 체크
     *
     * @param postCode 직위 번호
     * @return 결과
     */
    public SysPost checkPostCodeUnique(String postCode);
}

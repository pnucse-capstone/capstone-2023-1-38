package com.lsj.system.service;

import java.util.List;

import com.lsj.system.domain.SysPost;

/**
 * 직위 정보 업무층
 */
public interface ISysPostService {
    /**
     * 직위 정보 집합 조회
     *
     * @param post 직위 정보
     * @return 직위 목록
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
     * 직위 이름 확인
     *
     * @param post 직위 정보
     * @return 결과
     */
    public String checkPostNameUnique(SysPost post);

    /**
     * 직위 번호 확인
     *
     * @param post 직위 정보
     * @return 결과
     */
    public String checkPostCodeUnique(SysPost post);

    /**
     * 직위 ID로 직위 사용 수량을 조회
     *
     * @param postId 직위 ID
     * @return 결과
     */
    public int countUserPostById(Long postId);

    /**
     * 직위 정보를 삭제
     *
     * @param postId 직위 ID
     * @return 결과
     */
    public int deletePostById(Long postId);

    /**
     * 여러 직위 정보를 삭제
     *
     * @param postIds 삭제할 직위 ID
     * @return 결과
     * @throws Exception 예외
     */
    public int deletePostByIds(Long[] postIds);

    /**
     * 직위 정보를 추가
     *
     * @param post 직위 정보
     * @return 결과
     */
    public int insertPost(SysPost post);

    /**
     * 직위 정보 수정
     *
     * @param post 직위 정보
     * @return 결과
     */
    public int updatePost(SysPost post);
}

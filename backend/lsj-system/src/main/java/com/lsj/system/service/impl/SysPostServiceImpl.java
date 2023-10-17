package com.lsj.system.service.impl;

import java.util.List;

import com.lsj.system.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lsj.common.constant.UserConstants;
import com.lsj.common.exception.CustomException;
import com.lsj.common.utils.StringUtils;
import com.lsj.system.domain.SysPost;
import com.lsj.system.mapper.SysPostMapper;
import com.lsj.system.mapper.SysUserPostMapper;

/**
 * 직위 정보 service layer 처리
 */
@Service
public class SysPostServiceImpl implements ISysPostService {
    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    /**
     * 직위 정보 집합 조회
     *
     * @param post 직위 정보
     * @return 직위 정보 집합
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return postMapper.selectPostList(post);
    }

    /**
     * 모든 직위 조회
     *
     * @return 직위 목록
     */
    @Override
    public List<SysPost> selectPostAll() {
        return postMapper.selectPostAll();
    }

    /**
     * 직위 ID로 직위 정보를 조회
     *
     * @param postId 직위 ID
     * @return 역할 객체 정보
     */
    @Override
    public SysPost selectPostById(Long postId) {
        return postMapper.selectPostById(postId);
    }

    /**
     * 사용자 ID로 직위 선택 옵션 목록을 얻기
     *
     * @param userId 사용자 ID
     * @return 직위 ID 목록
     */
    @Override
    public List<Integer> selectPostListByUserId(Long userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    /**
     * 직위 이름 유일한지 확인
     *
     * @param post 직위 정보
     * @return 결과
     */
    @Override
    public String checkPostNameUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();//직위 ID를 얻기, null이면 -1L
        //직위 이름 유일하지 체크
        SysPost info = postMapper.checkPostNameUnique(post.getPostName());
        //조회 결과 null아니고 직위 ID가 현제 직위 ID 다르면 해당 직위 이름은 존재함
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;//유일하지 않으로 반납
        }
        return UserConstants.UNIQUE;//유일함으로 반납
    }

    /**
     * 직위 번호 유일한지 확인
     *
     * @param post 직위 정보
     * @return 결과
     */
    @Override
    public String checkPostCodeUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();//직위 ID를 얻기, null이면 -1L
        SysPost info = postMapper.checkPostCodeUnique(post.getPostCode());//직위 코드번호 유일하지 체크
        //조회 결과 null아니고 직위 ID가 현재 직위 Id 다르면 해당 직위 이름은 존재함
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;//유일하지 않으로 반납
        }
        return UserConstants.UNIQUE;//유일함으로 반납
    }

    /**
     * 직위 ID로 직위 사용 수량을 조회
     *
     * @param postId 직위 ID
     * @return 결과
     */
    @Override
    public int countUserPostById(Long postId) {
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * 직위 정보를 삭제
     *
     * @param postId 직위 ID
     * @return 결과
     */
    @Override
    public int deletePostById(Long postId) {
        return postMapper.deletePostById(postId);
    }

    /**
     * 여러 직위 정보를 삭제
     *
     * @param postIds 삭제할 직위 ID
     * @return 결과
     * @throws Exception 예외
     */
    @Override
    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) { //각 직위 방문
            SysPost post = selectPostById(postId);//ID로 각 직위 객체를 얻기
            if (countUserPostById(postId) > 0) { //직위 사용수가 있으면
                throw new CustomException(String.format("%1$s 분배되었으니 삭제 불가합니다", post.getPostName()));//throw exception 메시지
            }
        }
        return postMapper.deletePostByIds(postIds);//ID로 직위 삭제
    }

    /**
     * 직위 정보를 추가
     *
     * @param post 직위 정보
     * @return 결과
     */
    @Override
    public int insertPost(SysPost post) {
        return postMapper.insertPost(post);
    }

    /**
     * 직위 정보 수정
     *
     * @param post 직위 정보
     * @return 결과
     */
    @Override
    public int updatePost(SysPost post) {
        return postMapper.updatePost(post);
    }
}

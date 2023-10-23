package com.lsj.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.lsj.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lsj.common.annotation.DataScope;
import com.lsj.common.constant.UserConstants;
import com.lsj.common.core.domain.entity.SysRole;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.common.exception.CustomException;
import com.lsj.common.utils.StringUtils;
import com.lsj.system.domain.SysPost;
import com.lsj.system.domain.SysUserPost;
import com.lsj.system.domain.SysUserRole;
import com.lsj.system.mapper.SysPostMapper;
import com.lsj.system.mapper.SysRoleMapper;
import com.lsj.system.mapper.SysUserMapper;
import com.lsj.system.mapper.SysUserPostMapper;
import com.lsj.system.mapper.SysUserRoleMapper;


/**
 * 사용자 업무층처리
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    /**
     * 조건에 따라 페이지별로 사용자 목록을 조회
     *
     * @param user 사용자 정보
     * @return 사용자 정보 집합
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }


    /**
     * 사용자 계정번호로 사용자를 조회
     *
     * @param userName 사용자 이름
     * @return 사용자 객체 정보
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 사용자 ID로 사용자 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 객체 정보
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 사용자 속한 역할 그룹을 조회
     *
     * @param userName 사용자
     * @return 결과
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 사용자 속한 직위 그룹을 조회
     *
     * @param userName 사용자
     * @return 결과
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 사용자 계정 유일한지 여부
     *
     * @param userName 사용자 이름
     * @return 결과
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 전화번호 유일한지 체크
     *
     * @param user 사용자 정보
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 이메일 유일한지 체크
     *
     * @param user 사용자 정보
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 사용자의 조작 허용 여부를 확인
     *
     * @param user 사용자 정보
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new CustomException("관리자를 조작할 수 없습니다");
        }
    }

    /**
     * 사용자 정보를 추가
     *
     * @param user 사용자 정보
     * @return 결과
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 사용자 정보 추가
        int rows = userMapper.insertUser(user);
        // 추가한 사용자 직위 관련
        insertUserPost(user);
        // 추가한 사용자 역할 관리
        insertUserRole(user);
        return rows;
    }

    /**
     * 사용자 정보 수정
     *
     * @param user 사용자 정보
     * @return 결과
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 사용자와 역할 관련을 삭제
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 사용자와 역할 관리를 추가
        insertUserRole(user);
        // 사용자와 직위 관련을 삭제
        userPostMapper.deleteUserPostByUserId(userId);
        // 사용자와 직위 관리를 추가
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 사용자 상태 수정
     *
     * @param user 사용자 정보
     * @return 결과
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 사용자 기본 정보를 수정
     *
     * @param user 사용자 정보
     * @return 결과
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 사용자 프로필사진을 수정
     *
     * @param userName 사용자 이름
     * @param avatar 프로필 사진 주소头像地址
     * @return 결과
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 사용자 비밀번호를 재설정
     *
     * @param user 사용자 정보
     * @return 결과
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 사용자 비밀번호를 재설정
     *
     * @param userName 사용자 이름
     * @param password 비밀번호
     * @return 결과
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * @param user
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 사용자 직위 정보를 추사
     *
     * @param user 사용자 객체
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 사용자 ID로 사용자를 삭제
     *
     * @param userId 사용자 ID
     * @return 결과
     */
    @Override
    public int deleteUserById(Long userId) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 여러 사용자 정보를 삭제
     *
     * @param userIds 사용자 ID
     * @return 결과
     */
    @Override
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));//사용자를 삭제할 수 있는지 체크(관리자를 삭제할 수 없다)
        }
        return userMapper.deleteUserByIds(userIds);//사용자를 삭제
    }
}

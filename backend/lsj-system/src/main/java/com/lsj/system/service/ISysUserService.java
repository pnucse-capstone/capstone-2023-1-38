package com.lsj.system.service;

import java.util.List;

import com.lsj.common.core.domain.entity.SysUser;

/**
 * 사용자 업무층
 */
public interface ISysUserService {
    /**
     * 조건에 따라 페이지별로 사용자 목록을 조회
     *
     * @param user 사용자 정보
     * @return 사용자 정보 집합
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * 사용자 계정번호로 사용자를 조회
     *
     * @param userName 사용자
     * @return 사용자 객체 정보
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 사용자 ID로 사용자 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 객체 정보
     */
    public SysUser selectUserById(Long userId);

    /**
     * 사용자 계정번호로 사용자 속한 역할 그룹을 조회
     *
     * @param userName 사용자
     * @return 결과
     */
    public String selectUserRoleGroup(String userName);

    /**
     * 사용자 계정번호로 사용자 속한 직위 그룹을 조회
     *
     * @param userName 사용자 이름
     * @return 결과
     */
    public String selectUserPostGroup(String userName);

    /**
     * 사용자 계정 유일하는지 체크
     *
     * @param userName 사용자 이름
     * @return 결과
     */
    public String checkUserNameUnique(String userName);

    /**
     * 전화번호 유일한지 체크
     *
     * @param user 사용자 정보
     * @return 결과
     */
    public String checkPhoneUnique(SysUser user);

    /**
     * 이메일 유일한지 체크
     *
     * @param user 사용자 정보
     * @return 결과
     */
    public String checkEmailUnique(SysUser user);

    /**
     * 사용자의 조작 허용 여부를 확인
     *
     * @param user 사용자 정보
     */
    public void checkUserAllowed(SysUser user);

    /**
     * 사용자 정보를 추가
     *
     * @param user 사용자 정보
     * @return 결과
     */
    public int insertUser(SysUser user);

    /**
     * 사용자 정보 수정
     *
     * @param user 사용자 정보
     * @return 결과
     */
    public int updateUser(SysUser user);

    /**
     * 사용자 상태 수정
     *
     * @param user 사용자 정보
     * @return 결과
     */
    public int updateUserStatus(SysUser user);

    /**
     * 사용자 기본 정보를 수정
     *
     * @param user 사용자 정보
     * @return 결과
     */
    public int updateUserProfile(SysUser user);

    /**
     * 사용자 프로필사진을 수정
     *
     * @param userName 사용자 이름
     * @param avatar   프로필 사진 주소
     * @return 결과
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * 사용자 비밀번호를 재설정
     *
     * @param user 사용자 이름
     * @return 결과
     */
    public int resetPwd(SysUser user);

    /**
     * 사용자 비밀번호를 재설정
     *
     * @param userName 사용자 이름
     * @param password 비밀번호
     * @return 결과
     */
    public int resetUserPwd(String userName, String password);

    /**
     * 사용자 ID로 사용자를 삭제
     *
     * @param userId 사용자 ID
     * @return 결과
     */
    public int deleteUserById(Long userId);

    /**
     * 여러 사용자 정보를 삭제
     *
     * @param userIds 사용자 ID
     * @return 결과
     */
    public int deleteUserByIds(Long[] userIds);
}

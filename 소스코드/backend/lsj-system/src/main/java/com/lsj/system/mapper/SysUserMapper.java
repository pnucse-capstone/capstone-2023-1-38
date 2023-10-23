package com.lsj.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.lsj.common.core.domain.entity.SysUser;
import org.springframework.stereotype.Repository;

/**
 * 用户表 数据层
 * 사용자 테이블 데이터 층
 */
@Repository
public interface SysUserMapper {
    /**
     * 조건에 따라 페이지별로 사용자 목록을 조회
     *
     * @param sysUser 사용자 정보
     * @return 사용자 정보 집합
     */
    public List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 사용자 계정번호로 사용자를 조회
     *
     * @param userName 사용자 이름
     * @return 사용자 객체 정보
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 사용자 ID로 사용자를 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 객체 정보
     */
    public SysUser selectUserById(Long userId);

    /**
     * 사용자 정보 추가
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
     * 사용자 프로필사진 수정
     *
     * @param userName 사용자이름
     * @param avatar   프로필사진 주소
     * @return 결과
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 사용자 비밀번호 재설정
     *
     * @param userName 사용자 이름
     * @param password 비밀번호
     * @return 결과
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

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

    /**
     * 사용자 계정 유일한지 여부
     *
     * @param userName 사용자 이름
     * @return 결과
     */
    public int checkUserNameUnique(String userName);

    /**
     * 전화번호 유일한지 체크
     *
     * @param phonenumber 전화번호
     * @return 결과
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 이메일 유일한지 체크
     *
     * @param email 사용자 이메일
     * @return 결과
     */
    public SysUser checkEmailUnique(String email);

    /**
     * 부서 Id로 기사 목록을 얻기
     * @param deptId
     * @return
     */
    List<SysUser> selectUserByDeptId(String deptId);
}

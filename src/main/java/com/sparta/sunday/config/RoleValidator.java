package com.sparta.sunday.config;

import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.exception.ReadOnlyRoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component  // 빈으로 등록
@RequiredArgsConstructor
public class RoleValidator {

    public void validateRole(AuthUser authUser) {
        if (authUser.getAuthorities().contains(new SimpleGrantedAuthority("MEMBER"))) {
            throw new ReadOnlyRoleException("읽기 전용 멤버는 작업을 수행할 수 없습니다.");
        }
    }
}

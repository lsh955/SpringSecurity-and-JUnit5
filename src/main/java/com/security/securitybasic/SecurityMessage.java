package com.security.securitybasic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

/**
 * @author 이승환
 * @since 2020-11-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityMessage {

    private String message;

    @JsonIgnore // 테스트 돌릴 때 문제가 되어, 테스트 돌릴때만 JsonIgnore...
    private Authentication auth;

}

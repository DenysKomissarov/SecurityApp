package com.security.app.security.authorize;

import com.security.app.dto.CreateUserDTO;
import org.springframework.security.core.Authentication;

public interface AuthorizationConditions {

    boolean mayEditByUser(CreateUserDTO editUserDTO, Authentication authentication);
}

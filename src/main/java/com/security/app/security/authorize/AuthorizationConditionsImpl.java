package com.security.app.security.authorize;

import com.security.app.dto.CreateUserDTO;
import com.security.app.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationConditionsImpl implements AuthorizationConditions {

    @Override
    public boolean mayEditByUser(CreateUserDTO editUserDTO, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

       return editUserDTO.getEmail().equals(userPrincipal.getUser().getEmail()) ? true : false;

//        throw  new PermisionException("You don't have permission!", "you can only edit your data");


    }


}

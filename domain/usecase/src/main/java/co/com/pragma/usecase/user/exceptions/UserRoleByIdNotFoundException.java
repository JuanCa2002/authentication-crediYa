package co.com.pragma.usecase.user.exceptions;

import co.com.pragma.usecase.exception.NotFoundException;
import co.com.pragma.usecase.user.constants.UserMessageConstants;

import java.text.MessageFormat;

public class UserRoleByIdNotFoundException extends NotFoundException {

    public UserRoleByIdNotFoundException(Integer id) {
        super(MessageFormat.format(UserMessageConstants.ROLE_BY_ID_NOT_FOUND, id));
    }
}

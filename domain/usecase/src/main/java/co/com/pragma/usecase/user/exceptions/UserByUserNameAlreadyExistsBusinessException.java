package co.com.pragma.usecase.user.exceptions;

import co.com.pragma.usecase.exception.BusinessException;
import co.com.pragma.usecase.user.constants.UserMessageConstants;

public class UserByUserNameAlreadyExistsBusinessException extends BusinessException {
    public UserByUserNameAlreadyExistsBusinessException() {
        super(UserMessageConstants.USER_BY_USER_NAME_ALREADY_EXISTS);
    }
}

package co.com.pragma.usecase.user.exceptions;

import co.com.pragma.usecase.exception.BusinessException;
import co.com.pragma.usecase.user.constants.UserMessageConstants;

import java.text.MessageFormat;

public class UserByEmailAlreadyExistsBusinessException extends BusinessException {

    public UserByEmailAlreadyExistsBusinessException(String email) {
        super(MessageFormat.format(UserMessageConstants.USER_BY_EMAIL_ALREADY_EXISTS, email));
    }
}

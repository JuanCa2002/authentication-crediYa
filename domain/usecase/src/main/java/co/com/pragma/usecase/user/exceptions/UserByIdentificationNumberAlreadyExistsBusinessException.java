package co.com.pragma.usecase.user.exceptions;

import co.com.pragma.usecase.exception.BusinessException;
import co.com.pragma.usecase.user.constants.UserMessageConstants;

import java.text.MessageFormat;

public class UserByIdentificationNumberAlreadyExistsBusinessException extends BusinessException {

    public UserByIdentificationNumberAlreadyExistsBusinessException(String identificationNumber) {
        super(MessageFormat.format(UserMessageConstants.USER_BY_IDN_ALREADY_EXISTS, identificationNumber));
    }
}

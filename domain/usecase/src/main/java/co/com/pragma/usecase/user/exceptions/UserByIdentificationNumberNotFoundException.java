package co.com.pragma.usecase.user.exceptions;

import co.com.pragma.usecase.exception.NotFoundException;
import co.com.pragma.usecase.user.constants.UserMessageConstants;

import java.text.MessageFormat;

public class UserByIdentificationNumberNotFoundException extends NotFoundException {

    public UserByIdentificationNumberNotFoundException(String identificationNumber) {
        super(MessageFormat.format(UserMessageConstants.USER_BY_IDN_NOT_FOUND,identificationNumber));
    }
}

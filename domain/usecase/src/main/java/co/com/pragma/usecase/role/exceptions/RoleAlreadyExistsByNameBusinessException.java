package co.com.pragma.usecase.role.exceptions;

import co.com.pragma.usecase.exception.BusinessException;
import co.com.pragma.usecase.role.constants.RoleMessageConstants;

import java.text.MessageFormat;

public class RoleAlreadyExistsByNameBusinessException extends BusinessException {

    public RoleAlreadyExistsByNameBusinessException(String name) {
        super(MessageFormat.format(RoleMessageConstants.ROLE_ALREADY_EXISTS_BY_NAME, name));
    }
}

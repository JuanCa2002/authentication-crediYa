package co.com.pragma.api.mapper;

import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordMapper {

    @Named("passwordEncode")
    public static String passwordEncode(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}

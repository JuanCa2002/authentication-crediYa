package co.com.pragma.usecase.user.exceptions;

public class UserByIdentificationNumberNotFoundException extends NotFoundException{

    public UserByIdentificationNumberNotFoundException(String identificationNumber) {
        super("No se encontro un usuario con el numero de identificación " + identificationNumber);
    }
}

package com.razboi.razboi.business.service.user;


import com.razboi.razboi.business.service.user.dto.UserDTO;


public interface AuthService {


    UserDTO login(String username, String password) throws ServiceException;

}

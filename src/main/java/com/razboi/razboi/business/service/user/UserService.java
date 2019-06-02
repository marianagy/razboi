package com.razboi.razboi.business.service.user;

import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.business.service.user.dto.UserDTOHelper;
import com.razboi.razboi.persistence.user.AuthDAO;
import com.razboi.razboi.persistence.user.dao.UserDAO;
import com.razboi.razboi.persistence.user.entity.User;

public class UserService extends CRUDService<User> implements AuthService {

    private AuthDAO<User> userAuthDAO;

    public UserService() {
        this.userAuthDAO = new UserDAO();
    }


    @Override
    public UserDTO login(String username, String password) throws ServiceException {
        User user = userAuthDAO.findByUsername(username);
        if (user == null) {
            throw new ServiceException(ExceptionCode.INVALID_LOGIN_EXCEPTION);
        }
        if (user.getPassword().equals(password)) {
            return UserDTOHelper.fromEntity(user);
        } else {
            throw new ServiceException(ExceptionCode.INVALID_LOGIN_EXCEPTION);
        }
    }


    public void create(UserDTO userDTO, String password) {
        User user = UserDTOHelper.toEntity(userDTO);
        user.setPassword(password);
        this.create(user);
    }

}

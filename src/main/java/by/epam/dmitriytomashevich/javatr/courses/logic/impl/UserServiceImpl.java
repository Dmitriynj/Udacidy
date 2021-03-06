package by.epam.dmitriytomashevich.javatr.courses.logic.impl;

import by.epam.dmitriytomashevich.javatr.courses.db.dao.UserDao;
import by.epam.dmitriytomashevich.javatr.courses.exceptions.DAOException;
import by.epam.dmitriytomashevich.javatr.courses.domain.User;
import by.epam.dmitriytomashevich.javatr.courses.logic.UserService;
import by.epam.dmitriytomashevich.javatr.courses.exceptions.LogicException;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDao();

    @Override
    public void updatePassword(String password, Long userId) throws LogicException {
        try {
            userDao.updatePassword(password, userId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

     @Override
    public boolean addUser(User user) throws LogicException {
        try {
            if(userDao.findByEmail(user.getEmail()) == null){
                userDao.create(user);
                return true;
            }else {
                return false;
            }
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public User findUserByEmail(String email) throws LogicException {
        try {
            return userDao.findByEmail(email);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public List<User> findAllByConversationId(Long id) throws LogicException {
        try {
            return userDao.findAllByConversationId(id);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public User findById(Long id) throws LogicException {
        try {
            return userDao.findById(id);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }
}

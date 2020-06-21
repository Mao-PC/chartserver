package com.amo.chartserver.service.impl;

import com.amo.chartserver.dao.UserDao;
import com.amo.chartserver.service.UserServer;
import com.amo.chartserver.util.exception.BusinessException;
import com.amo.chartserver.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServerImpl implements UserServer {

    @Autowired
    private UserDao userDao;

    @Override
    public long signIn(User user) throws BusinessException {
        return userDao.count(Example.of(user));
    }

    @Override
    public User signUp(User user) throws BusinessException {

        if (userDao.count(Example.of(user)) == 0) {
            user.setCreateTime(new Date());
            return userDao.save(user);
        } else {
            throw new BusinessException("用户已存在");
        }

    }
}

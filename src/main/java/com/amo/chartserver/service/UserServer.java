package com.amo.chartserver.service;

import com.amo.chartserver.util.exception.BusinessException;
import com.amo.chartserver.vo.User;

public interface UserServer {
    long signIn(User user) throws BusinessException;

    User signUp(User user) throws BusinessException;
}

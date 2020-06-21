package com.amo.chartserver.controller;

import com.amo.chartserver.service.UserServer;
import com.amo.chartserver.util.exception.BusinessException;
import com.amo.chartserver.view.ResponseView;
import com.amo.chartserver.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户各种方法
 */
@RestController
@RequestMapping("chart/user")
public class UserController {

    @Autowired
    private UserServer userServer;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @RequestMapping("signIn")
    public ResponseView signIn(User user) throws BusinessException {
        if (userServer.signIn(user) == 0) {
            return ResponseView.getFailedView();
        }
        return ResponseView.getSuccessView();
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping("signUp")
    public ResponseView signUp(User user) throws BusinessException {
        User res = userServer.signUp(user);
        return ResponseView.getSuccessView(res);
    }

}

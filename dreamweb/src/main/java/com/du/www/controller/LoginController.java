package com.du.www.controller;

import com.du.www.common.Constants;
import com.du.www.common.MD5Util;
import com.du.www.entity.User;
import com.du.www.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController extends BaseController{
    private final static Logger  log =Logger.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(Model model){
        User user = (User)getSession().getAttribute("user");
        if(user!=null){
            return "/personal/personal";
        }
        return "../login";
    }

    @RequestMapping("/doLogin")
    public String doLogin(Model model, @RequestParam(value = "username",required = false) String email,
                          @RequestParam(value ="password",required = false) String password,
                          @RequestParam(value = "code",required = false) String code,
                          @RequestParam(value = "state",required = false) String state,
                          @RequestParam(value = "pageNum",required = false) Integer pageNum,
                          @RequestParam(value = "pageSize",required = false) Integer pageSize){
        if (StringUtils.isBlank(code)){
            model.addAttribute("error","fail");
            return "../login";
        }
        int b = checkValidateCode(code);
        if(b == -1){
            model.addAttribute("error","fail");
            return "../login";
        }else if(b==0){
            model.addAttribute("error","fail");
            return "../login";
        }
        password = MD5Util.encodeToHex(Constants.SALT+password);
        User user = userService.login(email,password);
        if (user!=null){
            if ("0".equals(user.getState())){
                model.addAttribute("email",email);
                model.addAttribute("error","active");
                return "../login";
            }
            log.info("用户登录登录成功");
            getSession().setAttribute("user",user);
            model.addAttribute("user",user);
            return "/personal/personal";
        }else {
            log.info("用户登录登录失败");
            model.addAttribute("email",email);
            model.addAttribute("error","fail");
            return "../login";
        }
    }
    public int checkValidateCode(String code){
        Object vercode = getRequest().getSession().getAttribute("VERCODE_KEY");
        if(null==vercode){
            return -1;
        }
        if (!code.equalsIgnoreCase(vercode.toString())){
            return 0;
        }
        return 1;
    }
}

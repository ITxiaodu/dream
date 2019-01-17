package com.du.www.controller;

import com.du.www.common.CodeCaptchaServlet;
import com.du.www.common.MD5Util;
import com.du.www.entity.User;
import com.du.www.mail.SendEmail;
import com.du.www.service.RoleService;
import com.du.www.service.RoleUserService;
import com.du.www.service.UserService;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.jws.WebParam;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class RegisterController {
    private final static Logger log = Logger.getLogger(RegisterController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RoleUserService roleUserService;

    @RequestMapping("/checkPhone")
    @ResponseBody
    public Map<String, Object> checkPhone(Model model, @RequestParam(value = "phone", required = false) String phone) {
        log.debug("注册-判断手机号" + phone + "是否可用");
        Map map = new HashMap<String, Object>();
        User user = userService.findByphone(phone);
        if (user == null) {
            map.put("message", "success");
        } else {
            map.put("message", "fail");
        }
        return map;
    }

    @RequestMapping("/checkEmail")
    @ResponseBody
    public Map<String, Object> checkEmail(Model model, @RequestParam(value = "email", required = false) String email) {
        log.debug("注册-判断与邮箱" + email + "是否可用");
        Map map = new HashMap<String, Object>();
        User user = userService.findByEmail(email);
        if (user == null) {
            map.put("message", "success");
        } else {
            map.put("message", "fail");
        }
        return map;
    }

    @RequestMapping("/checkCode")
    @ResponseBody
    public Map<String, Object> checkCode(Model model, @RequestParam(value = "code", required = false) String code) {
        log.debug("注册-判断验证码" + code + "是否可用");
        Map map = new HashMap<String, Object>();
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String vcode = (String) attrs.getRequest().getSession().getAttribute(CodeCaptchaServlet.VERCODE_KEY);
        if (code.equals(vcode)) {
            map.put("message", "success");
        } else {
            map.put("message", "fail");
        }
        return map;
    }

    @RequestMapping("/doRegister")
    public String doRegister(Model model,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "nickName", required = false) String nickname,
                             @RequestParam(value = "code", required = false) String code) {
        log.debug("注册。。。");
        if (StringUtils.isBlank(code)){
            model.addAttribute("error","非法注册，请重新注册！");
            return "../register";
        }
        int b = checkValidateCode(code);
        if(b==-1){
            model.addAttribute("error","验证码超时，请重新注册！");
            return "../register";
        }else if (b==0){
            model.addAttribute("error","验证码不正确，请重新注册！");
            return "../register";
        }

        User user = userService.findByEmail(email);
        if(user != null){
            model.addAttribute("error","该用户已经被注册！");
            return "../register";
        }else {
            user = new User();
            user.setNickName(nickname);
            user.setPassword(MD5Util.encodeToHex("salt"+password));
            user.setPhone(phone);
            user.setEmail(email);
            user.setState("0");
            user.setImgUrl("/images/icon_m.img");

            String validateCode = MD5Util.encodeToHex("salt"+ email + password);
            redisTemplate.opsForValue().set(email,validateCode,24, TimeUnit.HOURS);

            userService.regist(user);

            log.info("注册成功");
            SendEmail.sendEmailMessage(email,validateCode);
            String message = email + "," + validateCode;
            model.addAttribute("message",message);
            return "/regist/registerSuccess";
        }
    }

    public int checkValidateCode(String code){
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object vercode = attrs.getRequest().getSession().getAttribute("VERCODE_KEY");
        if (null == vercode){
            return -1;
        }
        if (!code.equalsIgnoreCase(vercode.toString())){
            return 0;
        }
        return 1;
    }

    @RequestMapping("/activecode")
    public String active(Model model){
        log.info("==========激活验证===========");
        ServletRequestAttributes attrs =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String validateCode = attrs.getRequest().getParameter("validateCode");
        String email = attrs.getRequest().getParameter("email");
        String code = redisTemplate.opsForValue().get(email);
        log.info("验证邮箱为："+email+",邮箱激活码为："+code+",用户连接的激活码为："+validateCode);

        User userTrue = userService.findByEmail(email);
        if (userTrue!=null&&"1".equals(userTrue.getState())){
            model.addAttribute("success","你已激活，请直接登录！");
            return "../login";
        }

        if (code==null){
            model.addAttribute("fail","你的激活已过期，请重新注册！");
            userService.deleteByEmail(email);
            return "/regist/activeFail";
        }
        if(StringUtils.isNotBlank(validateCode)&&validateCode.equals(code)){
            userTrue.setEnable("1");
            userTrue.setState("1");
            userService.update(userTrue);
            model.addAttribute("email",email);
            return "/regist/activeSuccess";
        }
            else {
            model.addAttribute("fail","你的激活码错误，请重新激活！");
            return "/regist/activeFail";
        }
    }
    @RequestMapping("/sendEmail")
    @ResponseBody
    public Map<String,Object> sendEmail(Model model){
        Map map = new HashMap<String,Object>();
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String validateCode = attrs.getRequest().getParameter("validateCode");
        String email =attrs.getRequest().getParameter("email");
        SendEmail.sendEmailMessage(email,validateCode);
        map.put("success","success");
        return map;
    }

    @RequestMapping("/register")
    public String register(Model model){
        log.info("进入注册页面");
        return "../register";
    }
}

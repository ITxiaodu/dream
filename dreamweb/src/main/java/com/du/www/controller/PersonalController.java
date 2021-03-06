package com.du.www.controller;

import com.du.www.common.Constants;
import com.du.www.common.DateUtils;
import com.du.www.common.MD5Util;
import com.du.www.common.PageHelper;
import com.du.www.entity.Comment;
import com.du.www.entity.User;
import com.du.www.entity.UserContent;
import com.du.www.entity.UserInfo;
import com.du.www.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class PersonalController extends BaseController{
    private final static Logger log = Logger.getLogger(PersonalController.class);
    @Autowired
    private UserContentService userContentService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UpvoteService upvoteService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/updatePassword")
    public String updatePassword(Model model,@RequestParam(value ="old_password",required = false) String oldPassword,
                                 @RequestParam(value = "password",required = false) String password){
        User user = (User) getSession().getAttribute("user");
        if (user!=null){
            oldPassword = MD5Util.encodeToHex(Constants.SALT + oldPassword);
            if (user.getPassword().equals(oldPassword)){
                password = MD5Util.encodeToHex(Constants.SALT + password);
                user.setPassword(password);
                userService.update(user);
                model.addAttribute("message","success");
            }else {
                model.addAttribute("message","fail");
            }
        }
        model.addAttribute("user",user);
        return "personal/passwordSuccess";
    }

    @RequestMapping("/saveUserInfo")
    public String saveUserInfo(Model model,@RequestParam(value = "name",required = false) String name,
                               @RequestParam(value = "nick_name",required = false) String nickName,
                               @RequestParam(value = "sex",required = false) String sex,
                               @RequestParam(value = "address",required = false) String address,
                               @RequestParam(value = "birthday",required = false) String birthday){
        User user = (User) getSession().getAttribute("user");
        if (user == null){
            return "../login";
        }
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        boolean flag =false;
        if (userInfo == null){
            userInfo = new UserInfo();
        }else {
            flag = true;
        }
        userInfo.setName(name);
        userInfo.setAddress(address);
        userInfo.setSex(sex);
        Date bir = DateUtils.StringToDate(birthday,"yyyy-MM-dd");
        userInfo.setBirthday(bir);
        userInfo.setuId(user.getId());
        if (!flag){
            userInfoService.add(userInfo);
        }else {
            userInfoService.update(userInfo);
        }

        user.setNickName(nickName);
        userService.update(user);

        model.addAttribute("user",user);
        model.addAttribute("userInfo",userInfo);
        return "personal/profile";
    }

    @RequestMapping("/list")
    public String findList(Model model, @RequestParam(value = "id",required = false) String id,
                           @RequestParam(value = "pageNum",required = false) Integer pageNum,
                           @RequestParam(value = "pageSize",required = false) Integer pageSize){
        User user = (User)getSession().getAttribute("user");
        UserContent content = new UserContent();
        UserContent uc = new UserContent();
        if (user!=null){
            model.addAttribute("user",user);
            content.setuId(user.getId());
            uc.setuId(user.getId());
        }else {
            return "../login";
        }
        log.info("初始化个人主页信息");
        List<UserContent> categorys = userContentService.findCategoryByUid(user.getId());
        model.addAttribute("categorys",categorys);
        content.setPersonal("0");
        pageSize =4;
        PageHelper.Page<UserContent> page = findAll(content,pageNum,pageSize);

        model.addAttribute("page",page);

        uc.setPersonal("1");
        PageHelper.Page<UserContent> page2 = findAll(uc,pageNum,pageSize);
        model.addAttribute("page2",page2);

        UserContent uct = new UserContent();
        uct.setPersonal("0");
        PageHelper.Page<UserContent> hotPage = findAllByUpvote(uct,pageNum,pageSize);
        model.addAttribute("hotPage",hotPage);
        return "personal/personal";
    }

    @RequestMapping("/findByCategory")
    @ResponseBody
    public Map<String,Object> findByCategory(Model model,@RequestParam(value = "category",required = false) String category,
                                             @RequestParam(value = "pageNum",required =false ) Integer pageNum,
                                             @RequestParam(value = "pageSize",required = false) Integer pageSize){
        Map map = new HashMap<String,Object>();
        User user = (User)getSession().getAttribute("user");
        if (user == null){
            map.put("pageCate","fail");
            return map;
        }
        pageSize = 4;
        PageHelper.Page<UserContent> pageCate = userContentService.findByCategory(category,user.getId(),pageNum,pageSize);
        map.put("pageCate",pageCate);
        return map;
    }
    @RequestMapping("/findPersonal")
    @ResponseBody
    public Map<String,Object> findPersonal(Model model,@RequestParam(value = "pageNum",required = false) Integer pageNum,@RequestParam(value = "pageSize",required = false) Integer pageSize){
        Map map = new HashMap<String,Object>();
        User user = (User) getSession().getAttribute("user");
        if (user == null){
            map.put("page2","fail");
            return map;
        }
        pageSize= 4;
        PageHelper.Page<UserContent> page = userContentService.findPersonal(user.getId(),pageNum,pageSize);
        map.put("page2",page);
        return map;
    }
    @RequestMapping("/deleteContent")
    public String deleteContent(Model model,@RequestParam(value = "cid",required = false) Long cid){
        User user =(User) getSession().getAttribute("user");
        if (user ==null){
            return "../login";
        }
        commentService.deleteByContentId(cid);
        upvoteService.deleteByContentId(cid);
        userContentService.deleteById(cid);
        return "redirect:/list?manage =manage";
    }

    @RequestMapping("/profile")
    public String profile(Model model){
        User user = (User) getSession().getAttribute("user");
        if (user == null){
            return "../login";
        }
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        model.addAttribute("user",user);
        model.addAttribute("userInfo",userInfo);

        return "personal/profile";
    }
    @RequestMapping("/saveImage")
    @ResponseBody
    public Map<String,Object> saveImage(Model model,@RequestParam(value = "url",required = false) String url){
        Map map = new HashMap<String,Object>();
        User user = (User) getSession().getAttribute("user");
        user.setImgUrl(url);
        userService.update(user);
        map.put("msg","success");
        return map;
    }
}

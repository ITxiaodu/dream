package com.du.www.controller;

import com.du.www.entity.User;
import com.du.www.entity.UserContent;
import com.du.www.service.UserContentService;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class WriteController  extends BaseController{
    private final static Logger log = Logger.getLogger(WriteController.class);
    @Autowired
    private UserContentService userContentService;
    @RequestMapping("/doWritedream")
    public String doWritedream(Model model, @RequestParam(value = "id",required = false) String id,
                               @RequestParam(value = "cid",required = false) Long cid,
                               @RequestParam(value = "category",required = false) String category,
                               @RequestParam(value = "txtT_itle",required = false) String txtT_itle,
                               @RequestParam(value = "content",required = false) String content,
                               @RequestParam(value = "private_dream",required = false) String private_dream){
        log.info("进入写梦Controller");
        User user =(User) getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("error","请先登录！");
            return "../login";
        }
        UserContent userContent = new UserContent();
        if (cid!=null){
            userContent = userContentService.findById(cid);
        }
        userContent.setCategory(category);
        userContent.setContent(content);
        userContent.setRptTime(new Date());
        String imgUrl = user.getImgUrl();
        if (StringUtils.isBlank(imgUrl)){
            userContent.setImgUrl("/images/icon_m.jpg");
        }else {
            userContent.setImgUrl(imgUrl);
        }
        if ("on".equals(private_dream)){
            userContent.setPersonal("1");
        }else {
            userContent.setPersonal("0");
        }
        userContent.setTitle(txtT_itle);
        userContent.setuId(user.getId());
        userContent.setNickName(user.getNickName());

        if (cid ==null) {
            userContent.setUpvote(0);
            userContent.setDownvote(0);
            userContent.setCommentNum(0);
            userContentService.addContent(userContent);
        }else{
            userContentService.updateById(userContent);
        }
        model.addAttribute("content",userContent);
        return "write/writesuccess";
    }
    @RequestMapping("writedream")
    public String writedream(Model model,@RequestParam(value = "cid",required = false) Long cid){
        User user = (User) getSession().getAttribute("user");
        if (cid!=null){
            UserContent content =userContentService.findById(cid);
            model.addAttribute("cont",content);
        }
        model.addAttribute("user",user);
        return "writedream";
    }

    @RequestMapping("/watch")
    public String watchContent(Model model,@RequestParam(value = "cid",required = false) Long cid){
        User user =(User) getSession().getAttribute("user");
        if (user ==null){
            model.addAttribute("error","请先登录！");
            return "../login";
        }
        UserContent userContent = userContentService.findById(cid);
        model.addAttribute("cont",userContent);
        return "personal/watch";
    }
}

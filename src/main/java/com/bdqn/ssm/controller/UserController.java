package com.bdqn.ssm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.RoleService;
import com.bdqn.ssm.service.UserService;
import com.bdqn.ssm.util.Constants;
import com.bdqn.ssm.util.PageSupport;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15001 on 2019/4/5.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Resource
    private UserService us;
    @Resource
    private RoleService rs;
    private Logger logger=Logger.getLogger(UserController.class);
    @RequestMapping(value = "/login.html",method = RequestMethod.GET)
    public String login(){
        logger.info("进入登录页面");
        return "login";
    }

    @RequestMapping(value = "/dologin.html",method = RequestMethod.POST)
    public String doLogin(@RequestParam String userCode,
                          @RequestParam String userPassword,
                          HttpServletRequest request,
                          HttpSession session){
        User user=us.getLoginUser(userCode,userPassword);
        if(user==null){
            request.setAttribute("error","用户或密码错误");
            return "login";
        }else {
            session.setAttribute(Constants.USER_SESSION,user);
            return "redirect:/user/main.html";
        }

    }

    /**
     * 进入系统主页面frame.jsp
     * @param session
     * @return
     */
    @RequestMapping("/main.html")
    public String main(HttpSession session){
        if(session.getAttribute(Constants.USER_SESSION)==null){
            return "redirect:/user/login.html";
        }else {
            return "frame";
        }

    }
    /* @RequestMapping(value = "exlogin.html",method = RequestMethod.GET)
     public String exlogin(@RequestParam String userCode,
                           @RequestParam String userPassword){
         logger.info("exlogin===========");
         User user=us.getLoginUser(userCode,userPassword);
         if(user==null){
             throw new RuntimeException("用户或密码不正确");
         }
         return "redirect:/user/main.html";
     }*/
   /* @ExceptionHandler(value = {RuntimeException.class})
    public String handler(RuntimeException e,
                          HttpServletRequest request){
        request.setAttribute("e",e);
        return "error";

    }*/
  /* @RequestMapping(value = "userlist.html")
public String userlist(){
    return "";
}*/
    //执行登录，抛出RuntimeException
    @RequestMapping(value = "/exlogin.html",method = RequestMethod.GET)
    public String exlogin(@RequestParam String userCode,
                          @RequestParam String userPassword){
        logger.info("进入exlogin=======");
        User user=us.getLoginUser(userCode,userPassword);
        if(user==null){
            throw new RuntimeException("用户名或密码不正确");
        }
        return "redirect:/user/main.html";
    }
    /*//springmvc局部异常处理
    @ExceptionHandler(value = {RuntimeException.class})
    public String error(RuntimeException e,
                        HttpServletRequest request){
        request.setAttribute("e",e);
        return "error";
    }*/
    @RequestMapping(value = "/userlist.html")
    public String getUserList(Model model,
                              @RequestParam(value = "queryname",required = false) String queryUserName,
                              @RequestParam(value = "queryUserRole",required = false)String queryUserRole,
                              @RequestParam(value = "pageIndex",required = false)String pageIndex){
        logger.info("getUserList====== queryUserName:"+queryUserName);
        logger.info("getUserList====== queryUserRole:"+queryUserRole);
        logger.info("getUserList====== pageIndex:"+pageIndex);
        Integer _queryUserRole=0;
        List<User> userList=null;
        int pageSize=Constants.pageSize;
        //当前页码
        int currentPageNo=1;
        if(queryUserName==null){
            queryUserName="";
        }
        if(queryUserRole!=null&&!queryUserRole.equals("")){
            _queryUserRole=Integer.parseInt(queryUserRole);
        }
        if(pageIndex!=null){
            try {
                currentPageNo=Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                return "redirect:/user/syserror.html";
            }
        }
        //总数量（表）
        int  totalCount=us.getAllUser(queryUserName,_queryUserRole);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount=pages.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo<1){
            currentPageNo=1;
        }else if(currentPageNo>totalPageCount){
            currentPageNo=totalPageCount;
        }
        userList=us.getUserList(queryUserName,_queryUserRole,currentPageNo,pageSize);
        model.addAttribute("userList",userList);
        List<Role> roleList=null;
        roleList=rs.getUserList();
        model.addAttribute("roleList",roleList);
        model.addAttribute("queryUserName",queryUserName);
        model.addAttribute("queryUserRole",queryUserRole);
        model.addAttribute("totalPageCount",totalPageCount);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "userlist";
    }

    @RequestMapping(value = "/useradd.html",method = RequestMethod.GET)
    public String add(@ModelAttribute("user")User user){
        logger.debug("进入添加页面");
        return "useradd";
    }
    //当请求表单中不指定action请求，该表单默认请求为转入该页面的请求
    //添加用户
    @RequestMapping(value = "/useradd.html",method = RequestMethod.POST)
    public String addUser(User user,HttpSession session){
        user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        if(us.addUser(user)>0){
            return "redirect:/user/userlist.html";
        }
        return "useradd";
    }
    //使用Spring标签
    //@ModelAttribute("user")User user,使用spring标签库的表单，需要form元素
    //中使用modelAttribute属性
    //指定从model中取出的对象的名称，如果不指定对象名称，默认按照名称为command
    //去model中查找对象
    //如果@ModelAttribute（“command”）User user，form元素中可以省略modelAttribute属性
    @RequestMapping(value = "/addsaveuser.html",method = RequestMethod.GET)
    public String addUser(@ModelAttribute("user")User user){
        return "user/useradd";
    }
    //Spring标签表单提交处理方式
    @RequestMapping(value = "/addsaveuser.html",method = RequestMethod.POST)

    public String addUserSubmit(@Valid User user,
                                BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "user/useradd";
        }else{
            user.setCreationDate(new Date());
            if(us.addUser(user)>0){
                return "redirect:/user/userlist.html";
            }
        }

        return "user/useradd";
    }
    @RequestMapping(value = "/usermodify.html",method = RequestMethod.GET)
    public String modify(Model model,String userid){
        logger.debug("getUserById======="+userid);
        User user=us.getUserById(userid);
      /*  SimpleDateFormat format=new SimpleDateFormat();*/
        model.addAttribute(user);
        return "usermodify";
    }

    @RequestMapping(value = "/usermodifysave.html",method = RequestMethod.POST)
    public String modifyUserSave(User user,HttpSession session){
        logger.debug("modifyUserSave userid===="+user.getId());
        user.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        if(us.modify(user)>0){
            return "redirect:/user/userlist.html";
        }
        return "usermodify";
    }

    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public String view(@PathVariable String id,Model model){
        logger.debug("view id========"+id);
        User user=us.getUserById(id);
        model.addAttribute(user);
        return "userview";
    }
    /* @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
     public String delete(@PathVariable String id){
         int count=us.delUser(id);
         if(count>0){
             return "redirect:/user/userlist.html";
         }
         return "userview";
     }*/
    //文件上传
   /* @RequestMapping(value="/useraddsave.html",method=RequestMethod.POST)
    public String addUserSave(User user,HttpSession session,HttpServletRequest request,
                              @RequestParam(value ="a_idPicPath", required = false)
                                      MultipartFile attach){
        String idPicPath = null;
        //判断文件是否为空
        if(!attach.isEmpty()){
            //文件上传到服务器中的路径
            String path = request.getSession().getServletContext()
                    .getRealPath("statics"+ File.separator+"uploadfiles");
            logger.info("uploadFile path ============== > "+path);
            //原文件名称,即（个人证件照.jpg）
            String oldFileName = attach.getOriginalFilename();//原文件名
            logger.info("uploadFile oldFileName ============== > "+oldFileName);
            //获取文件的后缀即(.jpg)
            String prefix= FilenameUtils.getExtension(oldFileName);//原文件后缀
            logger.debug("uploadFile prefix============> " + prefix);
            int filesize = 500000;
            logger.debug("uploadFile size============> " + attach.getSize());
            //限制上传文件的大小，不得超过500k
            if(attach.getSize() >  filesize){//上传大小不得超过 500k
                request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
                return "useradd";
                //限制上传文件的扩展名即文件扩展名必须是（jpg/jpeg/png/pneg）
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                //根据系统当前日期与随机数生成新的文件名称
                String fileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000000)+"_Personal.jpg";
                logger.debug("new fileName======== " + attach.getName());
                //根据文件上传路径与新文件名创建文件对象
                File targetFile = new File(path, fileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                //保存
                try {
                    //将文件对象转储到新文件对象中
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("uploadFileError", " * 上传失败！");
                    return "useradd";
                }
                //文件上传的路径与文件名
                idPicPath = path+File.separator+fileName;
            }else{
                request.setAttribute("uploadFileError", " * 上传图片格式不正确");
                return "useradd";
            }
        }
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        //保存文件路径多数据表中
        user.setIdPicPath(idPicPath);
        if(us.addUser(user)>0){
            return "redirect:/user/userlist.html";
        }
        return "useradd";
    }*/
//多文件上传
    @RequestMapping(value="/addsave.html",method=RequestMethod.POST)
    public String addUserSave(User user,HttpSession session,HttpServletRequest request,
                              @RequestParam(value ="attachs", required = false) MultipartFile[] attachs){
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
        logger.info("uploadFile path ============== > "+path);
        for(int i = 0;i < attachs.length ;i++){
            MultipartFile attach = attachs[i];
            if(!attach.isEmpty()){
                if(i == 0){
                    errorInfo = "uploadFileError";
                }else if(i == 1){
                    errorInfo = "uploadWpError";
                }
                String oldFileName = attach.getOriginalFilename();//原文件名
                logger.info("uploadFile oldFileName ============== > "+oldFileName);
                String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
                logger.debug("uploadFile prefix============> " + prefix);
                int filesize = 500000;
                logger.debug("uploadFile size============> " + attach.getSize());
                if(attach.getSize() >  filesize){//上传大小不得超过 500k
                    request.setAttribute("e", " * 上传大小不得超过 500k");
                    flag = false;
                }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                    String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";
                    logger.debug("new fileName======== " + attach.getName());
                    File targetFile = new File(path, fileName);
                    if(!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    //保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    System.out.println(i);
                    if(i == 1){
                        idPicPath = path+File.separator+fileName;
                    }else if(i == 0){
                        idPicPath = path+File.separator+fileName;
                        workPicPath = path+File.separator+fileName;
                    }
                    logger.debug("idPicPath: " + idPicPath);
                    logger.debug("workPicPath: " + workPicPath);

                }else{
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if(flag){
            user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            try {
                if(us.addUser(user)>0){
                    return "redirect:/user/userlist.html";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "useradd";
    }
    @RequestMapping(value = "/userexist.html")
    @ResponseBody
    public Object userCodeIsExit(@RequestParam String userCode){
        logger.debug("userCodeIsExit userCode======="+userCode);
        HashMap<String,String> resultMap=new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode","exist");
        }else {
            User user=us.getLoginUserCode(userCode);
            if(null!=user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode","noexist");
            }

        }
        return JSONArray.toJSONString(resultMap);
    }
    //删除用户,使用json格式返回数据类型
    @RequestMapping(value="/delResult.html",method= RequestMethod.GET)
    @ResponseBody
    public Object delUserById(@RequestParam String uid){
        System.out.println("进入删除方法");
        Map<String,Object> map=new HashMap<String,Object>();
        int count=us.delUser(uid);
        if(count>0){
            map.put("delResult","true");
        }
        else{
            map.put("delResult","false");
        }
        return  JSONArray.toJSONString(map);

    }
    /*
    * 使用json返回数据格式查询用户信息详情，produces={"application/json;charset=UTF-8"}
    * 该配置处理返回json对象，页面中文乱码问题
    * */
    @RequestMapping(value = "/views",method = RequestMethod.GET
            /*,produces={"application/json;charset=UTF-8"}*/)
    @ResponseBody
    public Object views(@RequestParam String id){
        logger.debug("view id========"+id);
        String cjosn="";
        if(id==null||id.equals("")){
            return "nodata";
        }else {
            try {
                User user=us.getUserById(id);
                cjosn= JSON.toJSONString(user);
                logger.debug("cjson======"+cjosn);
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }
        return cjosn;
    }
    @RequestMapping(value = "/modify",method = RequestMethod.GET)
    @ResponseBody
    public Object modify(){
        String Cjson="";
        try {
            List<Role>roleList=rs.getUserList();
            Cjson=JSON.toJSONString(roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
        return Cjson;
    }

}

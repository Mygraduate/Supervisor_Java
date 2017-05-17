package com.graduate.api.user;

import com.graduate.api.course.CourseController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.college.model.College;
import com.graduate.system.college.service.CollegeService;
import com.graduate.system.teacher.model.Teacher;
import com.graduate.system.teacher.service.TeacherService;
import com.graduate.system.user.model.Role;
import com.graduate.system.user.model.User;
import com.graduate.system.user.model.UserAndRole;
import com.graduate.system.user.service.RoleService;
import com.graduate.system.user.service.UserAndRoleService;
import com.graduate.system.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by konglinghai on 2017/4/4.
 */
@RestController
@RequestMapping("api/account")     // 通过这里配置使下面的映射都在/users下，可去除
@Api(value = "api/account", description = "用户登录公用接口")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private UserService<User> userService;

    @Autowired
    private UserAndRoleService<UserAndRole> userAndRoleService;

    @Autowired
    private RoleService<Role> roleService;


    @ApiOperation(value="获取用户列表", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/list"}, method=RequestMethod.POST)
    public BaseJsonData getUserList() {
        BaseJsonData data = new BaseJsonData();
        try{
            List<User> userList = userService.findAll();
            return data.ok(userList);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/create", method=RequestMethod.POST)
    public BaseJsonData createUser(@RequestBody User user,@RequestParam("roleId") Long roleId) {
        BaseJsonData data = new BaseJsonData();
        try {
            user.setLastPasswordResetDate(new Date());
            userService.save(user);
            UserAndRole userAndRole = new UserAndRole();
            userAndRole.setRoleId(roleId);
            userAndRole.setUid(user.getId());
            userAndRoleService.save(userAndRole);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }

    }

    @ApiOperation(value="删除用户", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
    public BaseJsonData deleteUserList(@RequestBody List<User> user) {
        BaseJsonData data = new BaseJsonData();
        try{
            userService.delete(user);
            for (User u:user) {
                UserAndRole uar = userAndRoleService.findRoleByUid(u.getId());
                userAndRoleService.delete(uar);
            }
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }


    @ApiOperation(value="修改用户", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/update"}, method=RequestMethod.POST)
    public BaseJsonData updateUsereList(@RequestBody User user,@RequestParam("roleId")Long roleId) {
        BaseJsonData data = new BaseJsonData();
        try{
            userService.save(user);
            UserAndRole uar = userAndRoleService.findRoleByUid(user.getId());
            uar.setRoleId(roleId);
            userAndRoleService.save(uar);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="根据账号获取用户信息", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/userinfobyname"}, method=RequestMethod.POST)
    public BaseJsonData getUserInfoByName(@RequestParam("username")String username) {
        BaseJsonData data = new BaseJsonData();
        try{
            User userInfo = userService.findUserByname(username);
            return data.ok(userInfo);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="根据用户id获取用户信息", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/userinfobyid"}, method=RequestMethod.POST)
    public BaseJsonData getUserInfoById(@RequestParam("id")Long id) {
        BaseJsonData data = new BaseJsonData();
        try{
            User userInfo = userService.findUserByid(id);
            return data.ok(userInfo);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="根据用户id获取用户角色id", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/userroleinfo"}, method=RequestMethod.POST)
    public BaseJsonData getUserRoleInfo(@RequestParam("uid")Long uid) {
        BaseJsonData data = new BaseJsonData();
        try{
            UserAndRole uar = userAndRoleService.findRoleByUid(uid);
            return data.ok(uar);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

//    @ApiOperation(value="修改用户角色", notes="")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value={"/udrole"}, method=RequestMethod.POST)
//    public BaseJsonData updateusertorole(@RequestBody UserAndRole userandrole) {
//        BaseJsonData data = new BaseJsonData();
//        try{
//            userAndRoleService.save(userandrole);
//            return data.ok();
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error(e.getMessage(),e);
//            return data.fail(e.getMessage());
//        }
//    }

    @ApiOperation(value="根据角色查询用户id", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/userlistbyuarid"}, method=RequestMethod.POST)
    public BaseJsonData getuserListbyrole(@RequestParam("id")Long id) {
        BaseJsonData data = new BaseJsonData();
        try{
            List<UserAndRole> userList = userAndRoleService.finduserandroleByRoleId(id);
            return data.ok(userList);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }
    

    @ApiOperation(value="根据角色id获取角色信息", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/roleinfo"}, method=RequestMethod.POST)
    public BaseJsonData getRoleInfo(@RequestParam("id")Long id) {
        BaseJsonData data = new BaseJsonData();
        try{
            Role roleinfo = roleService.findRoleById(id);
            return data.ok(roleinfo);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }





}

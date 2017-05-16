package com.graduate.api.user;

import com.graduate.api.course.CourseController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.user.model.User;
import com.graduate.system.user.model.UserAndRole;
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
            userService.save(user);
            UserAndRole userAndRole = new UserAndRole();
            userAndRole.setRoleId(roleId);
            userAndRole.setUid(user.getId());
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
    public BaseJsonData updateCollegeList(@RequestBody User user) {
        BaseJsonData data = new BaseJsonData();
        try{
            userService.save(user);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="根据账号获取用户信息", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/userinfo"}, method=RequestMethod.POST)
    public BaseJsonData getUserInfo(String username) {
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




}

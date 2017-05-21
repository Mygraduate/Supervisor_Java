package com.graduate.api.user;

import com.graduate.api.course.CourseController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.user.model.User;
import com.graduate.system.user.model.UserAndRole;
import com.graduate.system.user.service.UserAndRoleService;
import com.graduate.system.user.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by konglinghai on 2017/4/4.
 */
@RestController
@RequestMapping("api/account")     // 通过这里配置使下面的映射都在/users下，可去除
@Api(value = "api/account", description = "用户登录公用接口")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserAndRoleService<UserAndRole> userAndRoleService;

    @Autowired
    private UserService<User> userService;


    @ApiOperation(value="获取用户列表", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MASTER')")
    @RequestMapping(value={"/list"}, method=RequestMethod.POST)
    public BaseJsonData getUserList(@ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
                                    @ApiParam(value = "页长")@RequestParam(value = "pageSize") int pageSize,
                                    @ApiParam(value = "用户id")@RequestParam(value = "uid",required = false) Long uid,
                                    @ApiParam(value = "角色id")@RequestParam(value = "roleId",required = false) Long roleId,
                                    @ApiParam(value = "学院id")@RequestParam(value = "cid",required = false) Long cid,
                                    @ApiParam(value = "用户账号")@RequestParam(value = "username",required = false) String username,
                                    @ApiParam(value = "老师姓名")@RequestParam(value = "name",required = false) String name,
                                    @ApiParam(value = "老师职称")@RequestParam(value = "title",required = false) String title
    ) {
        try{
            HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("uid",uid);
            searchVals.put("roleId",roleId);
            searchVals.put("cid",cid);
            searchVals.put("username",username);
            searchVals.put("name",name);
            searchVals.put("title",title);
            HashMap<String,String> orderVals = new HashMap<>();
            orderVals.put("uid","ASC");
            Page<UserAndRole> userList = userAndRoleService.findAllByField(searchVals,pageNo,pageSize,orderVals);
            return BaseJsonData.ok(userList);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }

    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MASTER')")
    @RequestMapping(value="/create", method=RequestMethod.POST)
    public BaseJsonData createUser(@RequestBody User user, @ApiParam(value = "角色id")@RequestParam(value = "roleId") Long roleId) {
        BaseJsonData data = new BaseJsonData();
        try {
            userService.save(user);
            UserAndRole userAndRole=new UserAndRole();
            userAndRole.setUid(user.getId());
            userAndRole.setRoleId(roleId);
            userAndRoleService.save(userAndRole);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }

    }

    @ApiOperation(value="删除用户", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MASTER')")
    @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
    public BaseJsonData deleteUserList(@RequestBody List<User> users) {
        BaseJsonData data = new BaseJsonData();
        try{
            List<UserAndRole> list=new ArrayList<UserAndRole>();
            for (User u: users) {
                UserAndRole userAndRole=userAndRoleService.findRoleByUid(u.getId());
                list.add(userAndRole);
            }
            userAndRoleService.delete(list);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }


    @ApiOperation(value="修改用户", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MASTER')")
    @RequestMapping(value={"/update"}, method=RequestMethod.POST)
    public BaseJsonData updateUsereList(@RequestBody UserAndRole userAndRole) {
        BaseJsonData data = new BaseJsonData();
        try{
            userAndRoleService.save(userAndRole);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }
}

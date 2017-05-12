package com.graduate.api.user;

import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.user.model.User;
import com.graduate.system.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    @Autowired
    private UserService<User> userService;

    @ApiOperation(value="获取用户列表", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/list"}, method=RequestMethod.POST)
    public BaseJsonData getUserList() {
        BaseJsonData data = new BaseJsonData();
        HashMap<String,Object> map = new HashMap<>();

        try{
            List<User> r = userService.findAll();
            map.put("info",r);
        }catch (Exception e){
            e.printStackTrace();

        }
        data.setCode(1);
        data.setData(map);
        data.setMsg("查询成功");
        return data;
    }

    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/create", method=RequestMethod.POST)
    public String createUser(@RequestBody User user) {
        try {
            User user1 = userService.save(user);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "success";
    }



}

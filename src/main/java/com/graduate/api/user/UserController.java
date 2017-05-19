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
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @Autowired
    private TeacherService<Teacher> teacherService;

    @Autowired
    private CollegeService<College> collegeService;


    @ApiOperation(value="获取用户列表", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
            Page<UserAndRole> userList = userAndRoleService.findAll(buildSearch(searchVals),userAndRoleService.buildPage(pageNo,pageSize,orderVals));
            return BaseJsonData.ok(userList);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }

    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/create", method=RequestMethod.POST)
    public BaseJsonData createUser(@RequestBody UserAndRole userAndRole) {
        BaseJsonData data = new BaseJsonData();
        try {
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
    public BaseJsonData deleteUserList(@RequestBody List<UserAndRole> userAndRole) {
        BaseJsonData data = new BaseJsonData();
        try{
            userAndRoleService.delete(userAndRole);
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



    //搜索
    public Specification buildSearch(HashMap<String,Object> vals) {
        return new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                if(vals.get("uid")!=null&&NumberUtils.isNumber(vals.get("uid").toString())){
                    predicate.getExpressions().add(
                                    criteriaBuilder.equal(root.<String>get("uid"),NumberUtils.toLong(vals.get("uid").toString()))
                            );
                }
                if(vals.get("roleId")!=null&&NumberUtils.isNumber(vals.get("roleId").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.equal(root.<String>get("roleId"),NumberUtils.toLong(vals.get("roleId").toString()))
                    );
                }
                if(vals.get("cid")!=null&&NumberUtils.isNumber(vals.get("cid").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.equal(root.<User>get("user").get("cid"),NumberUtils.toLong(vals.get("cid").toString()))
                    );
                }
                if(vals.get("username")!=null&&StringUtils.isNotBlank(vals.get("username").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.like(root.<User>get("user").get("username"),"%"+vals.get("cid").toString()+"%")
                    );
                }
                if(vals.get("name")!=null&&StringUtils.isNotBlank(vals.get("name").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.like(root.<User>get("user").get("teacher").get("name"),"%"+vals.get("name").toString()+"%")
                    );
                }
                if(vals.get("title")!=null&&StringUtils.isNotBlank(vals.get("title").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.like(root.<User>get("user").get("teacher").get("title"),"%"+vals.get("title").toString()+"%")
                    );
                }
                return predicate;
            }
        };
    }





}

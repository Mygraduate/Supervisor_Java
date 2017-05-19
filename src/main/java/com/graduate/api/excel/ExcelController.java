package com.graduate.api.excel;

import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.common.ExcelService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.service.UserService;
import com.graduate.timer.MessageTask;
import com.graduate.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

import static com.graduate.utils.UserUtil.getUser;
import static org.springframework.security.oauth2.common.AuthenticationScheme.form;

/**
 * Created by konglinghai on 2017/5/14.
 */
@Controller
@RequestMapping("api/excel")
@Api(value = "api/excel", description = "所有excel的导入和导出")
public class ExcelController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @Autowired
    UserService<User> userService;

    @Autowired
    ExcelService excelService;

    @ApiOperation(value="上传课程表", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    @ResponseBody
    public BaseJsonData importCourse(@RequestParam("file") MultipartFile file ) {
//      Long cid = userService.findUserByname( UserUtil.getUserName()).getCid();
        Long cid = userService.findUserByname( UserUtil.getUserName()).getCollege().getId();
        try {
            excelService.importCourseByExcel(cid,file.getInputStream());
        } catch (Exception e) {
           e.printStackTrace();
           logger.error(e.getMessage(),e);
           return BaseJsonData.fail(e.getMessage());
        }
        return BaseJsonData.ok();
    }
}

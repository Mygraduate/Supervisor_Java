package com.graduate.api.excel;

import com.graduate.common.BaseJsonData;
import com.graduate.system.user.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

import static org.springframework.security.oauth2.common.AuthenticationScheme.form;

/**
 * Created by konglinghai on 2017/5/14.
 */
@Controller
@RequestMapping("api/excel")
//@Api(value = "api/excel", description = "所有excel的导入和导出")
public class ExcelController {

    //@ApiOperation(value="上传课程表", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MASTER')")
    @RequestMapping("/import")
    @ResponseBody
    public BaseJsonData importCourse(@RequestParam("file") MultipartFile file ) {
        BaseJsonData data = new BaseJsonData();
        HashMap<String,Object> map = new HashMap<>();

        data.setCode(1);
        data.setData(map);
        data.setMsg("查询成功");
        return data;
    }
}

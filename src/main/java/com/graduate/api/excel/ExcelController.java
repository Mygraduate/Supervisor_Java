package com.graduate.api.excel;

import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.common.ExcelService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.service.UserService;
import com.graduate.timer.MessageTask;
import com.graduate.utils.UserUtil;
import com.graduate.utils.excel.exception.FormatException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
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
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    @ResponseBody
    public BaseJsonData importCourse(@RequestParam("file") MultipartFile file ) {
        Long cid = userService.findUserByname( UserUtil.getUserName()).getCollege().getId();
        int count = 0;
        try {
            if(StringUtils.substringAfter(file.getName(),".") == "xlsx"){
                throw new FormatException("请将xlsx文件另存为xls");
            }
            count =  excelService.importCourseByExcel(cid,file.getInputStream());
        } catch (Exception e) {
           e.printStackTrace();
           logger.error(e.getMessage(),e);
           return BaseJsonData.fail(e.getMessage());
        }
        return BaseJsonData.ok("成功导入："+count+"条");
    }

    @ApiOperation(value="导出学期听课安排", notes="")
    @RequestMapping(value = "/export/arrage",method = RequestMethod.GET)
    public void exportArrage(@RequestParam("cid")Long cid,HttpServletRequest request,HttpServletResponse response) {
        FileInputStream in = null;
        OutputStream out = null;
        try {
            String saveName = String.valueOf(new Date().getTime());
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msexcel");
            response.setHeader("Content-Disposition", "attachment;filename=" + convertFileName(request, saveName) + ".xls");

            String savePath = excelService.exportArrage(cid, saveName);
            in = (new FileInputStream(savePath));
            out = response.getOutputStream();

            byte[] buffer = new byte[2048];
            int n = -1;
            while ((n = in.read(buffer)) > 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException ignore) {

            }
        }
    }


    private static String convertFileName(HttpServletRequest request, String fileName) throws Exception {
        String agent = request.getHeader("User-Agent");
        if (agent == null) {
            return new String(fileName.getBytes(), "UTF-8");
        }
        // 火狐浏览器
        agent = agent.toLowerCase();
        if (agent.indexOf("firefox") != -1) {
            return new String(fileName.getBytes(), "ISO-8859-1");
        }
        return URLEncoder.encode(fileName, "UTF-8");
    }
}

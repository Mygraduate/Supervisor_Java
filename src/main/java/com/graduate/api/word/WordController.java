package com.graduate.api.word;

import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.common.ExcelService;
import com.graduate.common.WordService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.service.UserService;
import com.graduate.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by konglinghai on 2017/5/14.
 */
@Controller
@RequestMapping("api/word")
@Api(value = "api/word", description = "所有word文档的导出")
public class WordController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(WordController.class);
    @Autowired
    UserService<User> userService;

    @Autowired
    WordService wordService;

    @ApiOperation(value="导出课程表", notes="")
    @RequestMapping(value = "/export",method = RequestMethod.GET)
    public void  exportCourse(
            @ApiParam(value = "学院id")@RequestParam(value = "cid") Long cid,
            @ApiParam(value = "教师姓名")@RequestParam(value = "teacher") String teacher,
            @ApiParam(value = "专业")@RequestParam(value = "major",required = false) String major,
            @ApiParam(value = "周数")@RequestParam(value = "week",required = false) Integer week,
            @ApiParam(value = "天数")@RequestParam(value = "day",required = false) Long day,
            @ApiParam(value = "年级")@RequestParam(value = "grade",required = false) String grade,
            @ApiParam(value = "班级")@RequestParam(value = "classes",required = false) String classes,
            HttpServletRequest request, HttpServletResponse response) {
        try{
            HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("cid",cid);
            searchVals.put("week",week);
            searchVals.put("day",day);
            searchVals.put("teacher",teacher);
            searchVals.put("grade",grade);
            searchVals.put("classes",classes);
            searchVals.put("major",major);
            String wordFileName = teacher;
            String savePath = wordService.exportClassPlan(searchVals,wordFileName);

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment;filename="+convertFileName(request, wordFileName)+".docx");

            FileInputStream in = null;
            OutputStream out = null;
            try {
                in = (new FileInputStream(savePath));
                out = response.getOutputStream();

                byte[] buffer = new byte[2048];
                int n = -1;
                while ((n = in.read(buffer)) > 0) {
                    out.write(buffer, 0, n);
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                } catch(IOException ignore) {}
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
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

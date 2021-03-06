package com.graduate;


import com.alibaba.fastjson.JSON;
import com.graduate.common.ExcelService;
import com.graduate.common.TimeService;
import com.graduate.system.arrage.dto.ArrageConfig;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.college.model.College;
import com.graduate.system.college.service.CollegeService;
import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.system.evaluate.model.Evaluate;
import com.graduate.system.evaluate.service.EvaluateService;
import com.graduate.system.sparetime.model.SpareTime;
import com.graduate.system.sparetime.service.SparetimeService;
import com.graduate.system.teacher.model.Teacher;
import com.graduate.system.user.model.Role;
import com.graduate.system.user.service.RoleService;
import com.graduate.timer.MessageTask;
import com.graduate.utils.ArrageUtil;
import com.graduate.utils.excel.exception.FormatException;
import com.graduate.utils.wecat.WecatService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.coyote.http11.Constants.LC_OFFSET;
import static org.apache.coyote.http11.Constants.a;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupervisorApplicationTests {

	@Autowired
	private TimeService timeService;

	@Autowired
	private MessageTask task;

	@Autowired
	private ExcelService excelService;

	@Autowired
	private CourseService<Course> courseService;

    @Autowired
    private SparetimeService<SpareTime> sparetimeService;

    @Autowired
    private CollegeService<College> collegeService;

    @Autowired
    private RoleService<Role> roleService;

    @Autowired
    private EvaluateService<Evaluate> evaluateService;

    @Autowired
    private WecatService wecatService;
	@Test
	public void contextLoads() {
		//System.out.println(timeService.calTimeByWeekAndDay(1,1));
		//System.out.println(timeService.calWeekDayByTime("2017-4-23"));
		//task.stop();
//		try {
//			excelService.importCourseByExcel(1L,new FileInputStream("C:\\Users\\Administrator\\Desktop\\demo3.xls"));
//		} catch (Exception e){
//            e.printStackTrace();
//        }
//		Sort sort = new Sort(Sort.Direction.DESC, "id");
//		Pageable pageable = new PageRequest(0,3,sort);
//		try{
//            HashMap<String,Object> vals = new HashMap<>();
//            vals.put("week",6);
////            vals.put("day",1);
//			vals.put("scope","6-7");
//			vals.put("teacher","陈");
//			Page<Course> page = courseService.findAll(0,3,null,vals);
//			System.out.println(JSON.toJSONString(page));
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//		try{
//			HashMap<String,Object> serachVals = new HashMap<>();
//			serachVals.put("")
//			arrageTestService.findAll()
//			//System.out.println(JSON.toJSONString(list));
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//        try {
//            List<Course> courses = courseService.findAllByTid(-1l);
//            System.out.println(courses);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try {
//            HashMap<String,Object> searchVals = new HashMap<>();
//            searchVals.put("cid",1l);
//            HashMap<String,String> orderVals = new HashMap<>();
//            orderVals.put("week","ASC");
//            List<Course> courses = courseService.findAll(searchVals,orderVals);
//            List<SpareTime> spareTimes = sparetimeService.findSpareTimeBycid(1l);
//            ArrageConfig config = new ArrageConfig();
//            config.setApercent(100);
//            config.setDayListen(1);
//            config.setWeekListen(2);
//            config.setMaxPeople(3);
//            config.setMinPeople(1);
//            config.setTotal(20);
//            config.setStartDay(1);
//            config.setStartWeek(1);
//            config.setEndWeek(20);
//            List<Arrage> arrages = new ArrayList<>();
//
//            System.out.println("安排的总体情况："+JSON.toJSON(ArrageUtil.autoCreateArrage(arrages,courses,spareTimes,config)));
//            System.out.println("已经安排的空闲时间："+JSON.toJSON(spareTimes));
//            System.out.println("所有的听课安排："+JSON.toJSON(arrages));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//

//        try {
//            List<Evaluate> list = evaluateService.findEvaluateByArrageId(36l);
//            System.out.println(JSON.toJSON(list));
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        try{
            WxCpMessage.WxArticle article1 = new WxCpMessage.WxArticle();
            article1.setTitle("xxxx听课安排");
            article1.setPicUrl("http://www.gdmu.edu.cn/images/01.jpg");
            article1.setUrl("www.baidu.com");
            WxCpMessage message  = WxCpMessage.NEWS()
                    .toUser("1")
                    .agentId(1)
                    .addArticle(article1)
                    .build();
            wecatService.sendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

	//分页
	public Pageable buildPageRequest(HttpServletRequest request){
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String order = request.getParameter("order");
		String orderField = request.getParameter("orderField");
		Sort sort  = new Sort(order,orderField,"");
		Pageable pageable = new PageRequest(pageNo,pageSize,sort);
		return pageable;
	}

    //搜索
    public Specification buildPageSearch(HashMap<String,Object> vals) {

        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                for (String key : vals.keySet()) {
                    Object val = vals.get(key);
                    if(NumberUtils.isNumber(val.toString())){
                            predicate.getExpressions().add(
                                    criteriaBuilder.equal(root.get(key).as(Integer.class),val)
                            );
                    }else if(val instanceof String){
                        if(StringUtils.isNoneBlank(val.toString())){
                            predicate.getExpressions().add(
                                    criteriaBuilder.like(root.<String>get(key), "%" + val + "%")
                            );
                        }
                    }
                }
                return predicate;
            }
        };
        return specification;
    }
}

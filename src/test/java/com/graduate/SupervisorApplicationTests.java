package com.graduate;

import com.alibaba.fastjson.JSON;
import com.graduate.common.ExcelService;
import com.graduate.common.TimeService;
import com.graduate.system.college.model.College;
import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.system.sparetime.model.SpareTime;
import com.graduate.system.sparetime.service.SparetimeService;
import com.graduate.system.teacher.model.Teacher;
import com.graduate.timer.MessageTask;
import com.graduate.utils.excel.exception.FormatException;
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
import java.util.HashMap;
import java.util.List;

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
        try {
            List<Course> courses = courseService.findAll();
            List<SpareTime> spareTimes = courseService.autoCreateSpareTime(courses,1l,1l);
            System.out.println(spareTimes);
            sparetimeService.save(spareTimes);
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

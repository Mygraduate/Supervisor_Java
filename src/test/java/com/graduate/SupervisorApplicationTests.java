package com.graduate;

import com.alibaba.fastjson.JSON;
import com.graduate.common.ExcelService;
import com.graduate.common.TimeService;
import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.timer.MessageTask;
import com.graduate.utils.excel.exception.FormatException;
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
import java.io.IOException;

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

	@Test
	public void contextLoads() {
		//System.out.println(timeService.calTimeByWeekAndDay(1,1));
		//System.out.println(timeService.calWeekDayByTime("2017-4-23"));
		//task.stop();
//		try {
//			excelService.importCourseByExcel(1L,"C:\\Users\\Administrator\\Desktop\\demo3.xls");
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (FormatException e) {
//			e.printStackTrace();
//		}
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		Pageable pageable = new PageRequest(0,3,sort);
		try{
			Page<Course> page = courseService.findAll(new Specification() {
				@Override
				public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
					return criteriaBuilder.like(root.<String>get("content"), "%定积分%");
				}
			}, pageable);

			System.out.println(JSON.toJSONString(page));
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
		Sort sort  = new Sort(order,orderField);
		Pageable pageable = new PageRequest(pageNo,pageSize,sort);
		return pageable;
	}

	//搜索
	public Specification buildPageSearch(HttpServletRequest request){
		String searchField = request.getParameter("searchField");
		String searchValue = request.getParameter("searchValue");
		Specification specification  =  new Specification() {
			@Override
			public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.<String>get(searchField), "%"+searchValue+"%");
			}
		};
		return specification;
	}
}

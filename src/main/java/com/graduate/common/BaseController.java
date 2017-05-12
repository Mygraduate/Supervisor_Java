package com.graduate.common;

import com.graduate.utils.DateUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.Date;

import static javax.management.Query.value;

/**
 * Created by konglinghai on 2017/3/21.
 */
public class BaseController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });

        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtil.parseDate(text));
            }
        });

        // Timestamp 类型转换
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Date date = DateUtil.parseDate(text);
                setValue(date == null ? null : new Timestamp(date.getTime()));
            }
        });
    }

    //分页和排序
    public Pageable buildPageRequest(HttpServletRequest request) {
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        String order = request.getParameter("order");
        String orderField = request.getParameter("orderField");
        if (StringUtils.isNoneBlank(pageNo) &&
                StringUtils.isNoneBlank(pageSize) &&
                StringUtils.isNoneBlank(order) &&
                StringUtils.isNoneBlank(orderField)) {
            Sort sort = new Sort(Sort.Direction.DESC, orderField);
            if (StringUtils.equals(order, "ASC")) {
                sort = new Sort(Sort.Direction.ASC, orderField);
            }
            Pageable pageable = new PageRequest(Integer.parseInt(pageNo) - 1, Integer.parseInt(pageSize), sort);
            return pageable;
        }
        return null;
    }

    //搜索
    public Specification buildPageSearch(HttpServletRequest request) {
        String searchField = request.getParameter("searchField");
        String searchValue = request.getParameter("searchValue");
        if (StringUtils.isNoneBlank(searchField) && StringUtils.isNoneBlank(searchValue)) {
            Specification specification = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.like(root.<String>get(searchField), "%" + searchValue + "%");
                }
            };
            return specification;
        }
        return null;
    }
}

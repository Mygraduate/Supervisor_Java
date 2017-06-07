package com.graduate.utils;

import com.graduate.system.arrage.dto.ArrageConfig;
import com.graduate.system.arrage.dto.SpareTimeConfig;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.course.model.Course;
import com.graduate.system.sparetime.model.SpareTime;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static org.apache.coyote.http11.Constants.a;


/**
 * Created by konglinghai on 2017/5/20.
 */
public class ArrageUtil {

    private static Random random = new Random();

    //自动排课
    public static List<SpareTimeConfig> autoCreateArrage(List<Arrage> arrages, List<Course> courses, List<SpareTime> times, ArrageConfig config) {
        List<SpareTimeConfig> spareTimeConfigs = new ArrayList<>();
        CourseUtil.reBuildScope(courses);//将1-3节次格式换成1,2,3
        initSpareTimeConfig(times, spareTimeConfigs);//初始化每个督导的听课记录，周听课次数，日听课次数
        for (int week = config.getStartWeek(); week < config.getEndWeek() + 1; week++) {
            if (arrages.size() >= config.getTotal()) {
                break;
            }  //判断当前听课安排总数是否超过预设范围
            restSpareTimeConfigWeek(spareTimeConfigs); //督导员每周听课安排次数归零
            List<Course> week_list = findListByWeek(courses, week, config); //每周的课程安排
            if (week_list.size() == 0) {
                continue;
            }
            for (int day = config.getStartDay(); day < 8; day++) {
                //判断当前听课安排总数是否超过预设范围
                if (arrages.size() >= config.getTotal()) {
                    break;
                }
                restSpareTimeConfigDay(spareTimeConfigs);     //督导员每日听课安排次数归零
                List<Course> day_list = findListByDay(week_list, day);  //这一周每日的课程安排
                if (day_list.size() == 0) {
                    continue;
                } else {
                    List<SpareTime> spareTimes = findTimeByWeekAndDay(times, week, day);   //找出课程与空闲时间一致的记录
                    for (Course course : day_list) {
                        //判断当前听课安排总数是否超过预设范围
                        if (arrages.size() >= config.getTotal()) {
                            break;
                        }
                        List<SpareTime> equalTimes = findTimeByScope(course.getScope(), spareTimes, spareTimeConfigs, config); //找出课程与空闲时间一致的记录
                        String group = createSupervisorGroup(equalTimes,spareTimeConfigs ,config);  //构建督导小组
                        if (!StringUtils.isNoneBlank(group)) {
                            continue;
                        } else {
                            createArrage(arrages, group, course, spareTimeConfigs); //构建督导小组构建督导安排
                        }
                    }
                }
            }
        }
        return spareTimeConfigs;
    }

    //根据周次和天数找到对应的空闲时间
    private static List<SpareTime> findTimeByWeekAndDay(List<SpareTime> times, int week, int day) {
        List<SpareTime> spareTimes = new ArrayList<>();
        for (SpareTime spareTime : times) {
            if (spareTime.getIsArrange() == 0 && spareTime.getWeek() == week && spareTime.getDay() == day) {
                spareTimes.add(spareTime);
            }
        }
        return spareTimes;
    }

    //根据节次是否相同返回符合条件的空闲时间
    private static List<SpareTime> findTimeByScope(String scope, List<SpareTime> times, List<SpareTimeConfig> spareTimeConfigs, ArrageConfig config) {
        List<SpareTime> list = new ArrayList<>();
        for (SpareTime time : times) {
            if (StringUtils.contains(time.getScope(), scope)) {
                if (isAppease(time, spareTimeConfigs, config)) {
                    list.add(time);
                }
            }
        }
        return list;
    }

    //构建督导小组
    private static String createSupervisorGroup(List<SpareTime> equalTimes,List<SpareTimeConfig> spareTimeConfigs ,ArrageConfig config) {
        sortEqualTimes(equalTimes,spareTimeConfigs);//听课次数少的人做组长
        int peopleNum = equalTimes.size();
        String groups = "";
        if (peopleNum < config.getMinPeople()) {
            return groups;
        } else if (peopleNum >= config.getMinPeople() && peopleNum < config.getMaxPeople()) {
            for (int i = 0; i < peopleNum; i++) {
                groups = groups + "," + equalTimes.get(i).getUid();
                equalTimes.get(i).setIsArrange(1);
            }
        } else {
            for (int i = 0; i < config.getMaxPeople(); i++) {
                groups = groups + "," + equalTimes.get(i).getUid();
                equalTimes.get(i).setIsArrange(1);
            }
        }
        return groups.substring(1);
    }

    private static void sortEqualTimes(List<SpareTime> equalTimes,List<SpareTimeConfig> spareTimeConfigs){
        List<SpareTime> spareTimes = new ArrayList<>();
        spareTimeConfigs.sort(new Comparator<SpareTimeConfig>() {
            @Override
            public int compare(SpareTimeConfig o1, SpareTimeConfig o2) {
                return o1.getTotal()-o2.getTotal();
            }
        });
        for(SpareTimeConfig config : spareTimeConfigs){
           for(SpareTime time : equalTimes){
               if(config.getUid().equals(time.getUid())){
                   spareTimes.add(time);
               }
           }
        }
        equalTimes.clear();
        equalTimes.addAll(spareTimes);
    }

    //构建听课安排
    private static void createArrage(List<Arrage> arrages, String group, Course course, List<SpareTimeConfig> configs) {
        course.setIsArrange(1);
        Arrage arrage = new Arrage();
        arrage.setCourse(course);
        arrage.setStatus(0);
        arrage.setCollegeId(course.getCid());
        arrage.setCourseId(course.getId());
        arrage.setGroups(group);
        arrage.setTid(course.getTid());
        arrages.add(arrage);
        upateSpareTimeCofig(group.split(","), configs);
    }

    //初始化每个督导的听课记录，周听课次数，日听课次数
    private static void initSpareTimeConfig(List<SpareTime> spareTimes, List<SpareTimeConfig> spareTimeConfigs) {
        HashMap<String, Long> uidMap = new HashMap<>();
        for (SpareTime time : spareTimes) {
            if (!uidMap.containsKey(time.getUid().toString())) {
                uidMap.put(time.getUid().toString(), time.getUid());
            }
        }

        for (String key : uidMap.keySet()) {
            Long uid = uidMap.get(key);
            SpareTimeConfig spareTimeConfig = new SpareTimeConfig(uid, 0, 0, 0);
            spareTimeConfigs.add(spareTimeConfig);
        }
    }

    //每添加一条听课记录，对应的督导老师日听课，周听课次数，总听课次数都+1
    private static void upateSpareTimeCofig(String[] uids, List<SpareTimeConfig> configs) {
        for (int i = 0; i < uids.length; i++) {
            for (SpareTimeConfig config : configs) {
                if (StringUtils.equals(uids[i], config.getUid().toString())) {
                    config.add();
                }
            }
        }

    }

    //根据周次寻找未安排听课的课程
    private static List<Course> findListByWeek(List<Course> courses, int week, ArrageConfig config) {
        List<Course> results = new ArrayList<>();
        for (Course course : courses) {
            if (course.getWeek().equals(week) && course.getIsArrange() == 0 && course.getType().equals(ContorlProportion(config))) {
                results.add(course);
            }
        }
        return results;
    }

    //控制理论课和实验课的比例
    private static String ContorlProportion(ArrageConfig config) {

        int i = random.nextInt(100);
        String classtype = "";
        if (i > 0 && i <= config.getApercent()) {
            classtype = "理论";
        } else {
            classtype = "实验";
        }
        return classtype;
    }

    //根据天数寻找未安排听课的课程
    private static List<Course> findListByDay(List<Course> courses, int day) {
        List<Course> results = new ArrayList<>();
        for (Course course : courses) {
            if (course.getDay().equals(day) && course.getIsArrange() == 0) {
                results.add(course);
            }
        }
        return results;
    }

    //判断该用户日听课次数和周听课次数是否满足
    private static boolean isAppease(SpareTime time, List<SpareTimeConfig> spareTimeConfigs, ArrageConfig config) {
        for (SpareTimeConfig spareTimeConfig : spareTimeConfigs) {
            if (time.getUid().equals(spareTimeConfig.getUid())) {
                if (spareTimeConfig.getDayListen() < config.getDayListen() && spareTimeConfig.getWeekListen() < config.getWeekListen()) {
                    return true;
                }
            }
        }
        return false;
    }

    //周听课次数归零
    private static void restSpareTimeConfigWeek(List<SpareTimeConfig> spareTimeConfigs) {
        for (SpareTimeConfig config : spareTimeConfigs) {
            config.restWeek();
        }
    }

    //日听课次数归零
    private static void restSpareTimeConfigDay(List<SpareTimeConfig> spareTimeConfigs) {
        for (SpareTimeConfig config : spareTimeConfigs) {
            config.restDay();
        }
    }

    //根据最终的课程表更新已安排的空闲时间
    private static void updateSpareTimeByArrage(List<Arrage> arrages, List<SpareTime> spareTimes) {
        List<SpareTime> arrageSpareTimes = new ArrayList<>();
        for (Arrage arrage : arrages) {
            Course course = arrage.getCourse();
            for (SpareTime spareTime : spareTimes) {
                if (course.getWeek().equals(spareTime.getWeek()) && course.getDay().equals(spareTime.getDay())) {
                    spareTime.setIsArrange(1);
                    arrageSpareTimes.add(spareTime);
                }
            }
        }
        spareTimes.clear();
        BeanMapper.copy(arrageSpareTimes, spareTimes);
    }

}

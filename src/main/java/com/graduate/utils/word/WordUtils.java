package com.graduate.utils.word;

import com.alibaba.fastjson.JSON;
import com.graduate.system.course.model.Course;
import com.graduate.utils.BeanMapper;
import com.graduate.utils.CourseUtil;
import com.graduate.utils.excel.config.ImportField;
import com.graduate.utils.excel.config.TestField;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.graduate.utils.CourseUtil.findListByDay;
import static com.graduate.utils.CourseUtil.findListByWeek;
import static com.graduate.utils.CourseUtil.isSeries;
import static com.sun.tools.doclint.Entity.mu;

/**
 * Created by konglinghai on 2017/5/16.
 */
public class WordUtils {

    private static  String CLASSES_TEMPLATE_PATH = "doc\\template\\classes.docx";

    private static  String TEMPLATE_PATH = "doc\\template";

    private static  String TEMPLATE_CHIEF_PATH = "chief";

    private static  String TEMPLATE_NORMAL_PATH = "normal";

    private static  String CLASSES_PLAN_SAVE_PATH = "doc\\classes";

    private static  String ARRAGE_EVALUATE_SAVE_PATH = "doc\\feedback";

    private static String  ARRAGE_EVALUATE_ZIP_SAVE_PATH = "doc\\zip";

    private static final String WORD_SUFFIX_DOCX =".docx";

    private static final String WORD_SUFFIX_DOC =".doc";

    private static final String ZIP_SUFFIX =".zip";

    public static void main(String[] args) {

//        try {
//            String savaPath = "C:\\Users\\Administrator\\Desktop\\test.docx";
//            String json = "[{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第一章 第一节  映射与函数\",\"day\":1,\"grade\":\"年级：2014\",\"id\":10,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-03-23\",\"type\":\"理论\",\"week\":4},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第一章 第三节  函数的极限\",\"day\":5,\"grade\":\"年级：2014\",\"id\":18,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-8\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-03-27\",\"type\":\"理论\",\"week\":4},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第一章 第二节  数列的极限\",\"day\":4,\"grade\":\"年级：2014\",\"id\":39,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-03-26\",\"type\":\"理论\",\"week\":4},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"国庆放假\",\"day\":4,\"grade\":\"年级：2014\",\"id\":3,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-02\",\"type\":\"理论\",\"week\":5},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"国庆放假\",\"day\":5,\"grade\":\"年级：2014\",\"id\":19,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-8\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-03\",\"type\":\"理论\",\"week\":5},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第一章 第四节  无穷大与无穷小\\r       第五节  极限的运算法则\",\"day\":1,\"grade\":\"年级：2014\",\"id\":28,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-03-30\",\"type\":\"理论\",\"week\":5},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"国庆放假\",\"day\":1,\"grade\":\"年级：2014\",\"id\":4,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-06\",\"type\":\"理论\",\"week\":6},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第一章 第八节  函数的连续性与间断点\\r       第九节  连续函数的运算与初等函数的连续性\",\"day\":5,\"grade\":\"年级：2014\",\"id\":14,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-8\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-10\",\"type\":\"理论\",\"week\":6},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第一章 第六节  极限存在准则  两个重要极限\\r       第七节  无穷小的比较\",\"day\":4,\"grade\":\"年级：2014\",\"id\":24,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-09\",\"type\":\"理论\",\"week\":6},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第二章 第二节  函数的求导法则\",\"day\":5,\"grade\":\"年级：2014\",\"id\":5,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-8\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-17\",\"type\":\"理论\",\"week\":7},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第一章 第十节  闭区间上连续函数的性质  \\r       第一章  复习解答\",\"day\":1,\"grade\":\"年级：2014\",\"id\":8,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-13\",\"type\":\"理论\",\"week\":7},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第二章 第一节  导数的概念\",\"day\":4,\"grade\":\"年级：2014\",\"id\":31,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-16\",\"type\":\"理论\",\"week\":7},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第二章 第五节  函数的微分\",\"day\":5,\"grade\":\"年级：2014\",\"id\":21,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-8\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-24\",\"type\":\"理论\",\"week\":8},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第二章 第四节  隐函数及由参数方程所确定的函数的导数  相关变化率\",\"day\":4,\"grade\":\"年级：2014\",\"id\":27,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-23\",\"type\":\"理论\",\"week\":8},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第二章 第三节  高阶导数\",\"day\":1,\"grade\":\"年级：2014\",\"id\":42,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-20\",\"type\":\"理论\",\"week\":8},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第三章 第一节  微分中值定理\",\"day\":4,\"grade\":\"年级：2014\",\"id\":11,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-30\",\"type\":\"理论\",\"week\":9},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第三章 第二节  洛比达法则\",\"day\":5,\"grade\":\"年级：2014\",\"id\":25,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-8\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-05-01\",\"type\":\"理论\",\"week\":9},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第二章 复习解答\",\"day\":1,\"grade\":\"年级：2014\",\"id\":35,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-04-27\",\"type\":\"理论\",\"week\":9},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第三章 第三节  泰勒公式\",\"day\":1,\"grade\":\"年级：2014\",\"id\":15,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-7\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-05-04\",\"type\":\"理论\",\"week\":10},{\"address\":\"D栋(D2-2)\",\"cid\":0,\"classes\":\"班级：05,06,07\",\"content\":\"第三章 第五节  函数的极值与最大值最小植\\r       第六节  函数图形的描绘\",\"day\":5,\"grade\":\"年级：2014\",\"id\":20,\"isArrange\":0,\"major\":\"专业：劳动与社会保障 \",\"name\":\"课程名称：高等数学(一) \",\"scope\":\"6-8\",\"teacher\":\"肖静(讲师（高校）)\",\"tid\":1,\"time\":\"2017-05-08\",\"type\":\"理论\",\"week\":10}]";
//            List<Course> courses = JSON.parseArray(json,Course.class);
//            exportClassPlan(courses,savaPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
//            HashMap<String,String> data = new HashMap<>();
//            data.put("teacher","小静");
//            data.put("title","教授");
//            data.put("time","2017-05-02");
//            data.put("address","A(2-1)");
//            data.put("class","离散数学");
//            data.put("context","定积分");
//            data.put("grade","80");
//            data.put("grade","80");
//            data.put("groups","小王，小明，小红");
//            data.put("summar","很好，非常好");
//            fillChief(data,"小静");
//            HashMap<String,String> data = new HashMap<>();
//            data.put("teacher","小静");
//            data.put("time","2017-05-02");
//            data.put("address","A(2-1)");
//            data.put("class","2013级");
//            data.put("name","离散数学");
//            data.put("content","定积分");
//            data.put("{0}","6");
//            data.put("{1}","15");
//            data.put("{2}","5");
//            data.put("{3}","20");
//            data.put("{4}","8");
//            data.put("{5}","9");
//            data.put("{6}","10");
//            data.put("{7}","20");
//            data.put("supervisor","小王");
//            data.put("total","80");
//            data.put("summar","很好，非常好");
//            fillNormal(data, String.valueOf(new Date().getTime()));
            File file1 = new File("C:\\Users\\Administrator\\Desktop\\demo4.xls");
            File file2 = new File("C:\\Users\\Administrator\\Desktop\\demo3.xls");
            List<File> files = new ArrayList<>();
            files.add(file1);
            files.add(file2);
            makeZipFile("demo",files);
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public static String exportClassPlan(List<Course> courses,String saveName) throws IOException {
        String classPath = URLDecoder.decode(String.valueOf(Thread.currentThread().getContextClassLoader().getResource("").getPath()),"UTF-8");
        String templatePath = classPath+"\\"+CLASSES_TEMPLATE_PATH;
        String savedir = classPath+"\\"+CLASSES_PLAN_SAVE_PATH;
        String savePath = savedir+"\\"+saveName+WORD_SUFFIX_DOCX;
        XWPFDocument doc = new XWPFDocument(new FileInputStream(templatePath));
        int index = 1;
        for(XWPFTable table : doc.getTables()){
            List<Course> week_result = CourseUtil.findListByWeek(courses,index);

            if(week_result.size()==0){
                index++;
                continue;
            }
            for (int column = 2; column < 7; column++)
            {
                List<Course> day_result = CourseUtil.findListByDay(week_result,column-1);
                if(day_result.size() == 0){
                    continue;
                }
                for(Course course : day_result){
                    for (int row = 1; row < 12; row++)
                    {
                        if(CourseUtil.isSeries(course.getScope(),row) == false){
                            continue;
                        }
                        table.getRow(row).getCell(column).setText(CourseUtil.formatClass(course));
                    }
                }

            }
            index++;
        }
        File fileDir = new File(savedir);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File saveFile = new File(savePath);
        if(!saveFile.exists()){
            saveFile.createNewFile();
        }
        OutputStream os = new FileOutputStream(saveFile);
        //把doc输出到输出流
        doc.write(os);
        os.close();
        return savePath;
    }

    public static void MakeTemplate(String savePath) throws IOException {
        XWPFDocument doc = new XWPFDocument(new FileInputStream(CLASSES_TEMPLATE_PATH));
        int index = 1;
        for(XWPFTable table : doc.getTables()){

            for (int column = 2; column < 7; column++)
            {


                    for (int row = 1; row < 12; row++)
                    {
                        table.getRow(row).getCell(column).removeParagraph(0);
                        table.getRow(row).getCell(column).setText("");
                    }

            }
            index++;
        }
        OutputStream os = new FileOutputStream(savePath);
        //把doc输出到输出流
        doc.write(os);
        os.close();

    }

    public static String fillTemplate(HashMap<String,String> data,String templateName,String saveName) throws Exception{
        String classPath = URLDecoder.decode(String.valueOf(Thread.currentThread().getContextClassLoader().getResource("").getPath()),"UTF-8");
        String savedir = classPath+"\\"+ARRAGE_EVALUATE_SAVE_PATH;
        String savePath = savedir+"\\"+saveName+WORD_SUFFIX_DOC;
        String templatePath = classPath+"\\"+TEMPLATE_PATH+"\\"+templateName+WORD_SUFFIX_DOC;
        XWPFDocument doc = new XWPFDocument(new FileInputStream(templatePath));
        XWPFTable table = doc.getTables().get(0);
        for(int i=0;i<table.getNumberOfRows();i++){
            for(int col = 0 ;col<table.getRow(i).getTableCells().size();col++){
                XWPFTableCell cell = table.getRow(i).getCell(col);
                String text = cell.getText();
                for(String key : data.keySet()){
                    if(StringUtils.contains(text,key)){
                        if(StringUtils.startsWith(key,"{")&&StringUtils.endsWith(key,"}")){
                            double num = Integer.parseInt(data.get(key));
                            double total = Integer.parseInt(table.getRow(i).getCell(6).getText());
                            double present = (num/total)*100;
                            XWPFTableCell insertCell = null;
                            if(present>0&&present<60){
                               insertCell = table.getRow(i).getCell(2);
                            }else if(present>=60&&present<80){
                                insertCell = table.getRow(i).getCell(3);
                            }else if(present>=80&&present<90){
                                insertCell = table.getRow(i).getCell(4);
                            }else {
                                insertCell = table.getRow(i).getCell(5);
                            }
                            replaceWordText(insertCell,"√");
                        }
                        replaceWordText(cell,data.get(key));
                    }
                }
            }
        }
        File fileDir = new File(savedir);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File saveFile = new File(savePath);
        if(!saveFile.exists()){
            saveFile.createNewFile();
        }
        OutputStream os = new FileOutputStream(saveFile);
        //把doc输出到输出流
        doc.write(os);
        os.close();
        return savePath;
    }

    private static void replaceWordText(XWPFTableCell cell,String replaceText){
        cell.removeParagraph(0);
        cell.setText("");
        cell.setText(replaceText);

    }
    public static String fillChief(HashMap<String,String> data,String saveName)throws Exception{
        return fillTemplate(data,TEMPLATE_CHIEF_PATH,saveName);
    }

    public static String fillNormal(HashMap<String,String> data,String saveName)throws Exception{
        return fillTemplate(data,TEMPLATE_NORMAL_PATH,saveName);
    }

    public static  void makeZipFile(String saveName,List<File> files) throws Exception{


        String classPath = URLDecoder.decode(String.valueOf(Thread.currentThread().getContextClassLoader().getResource("").getPath()),"UTF-8");
        String savedir = classPath+"\\"+ARRAGE_EVALUATE_ZIP_SAVE_PATH;
        String savePath = savedir+"\\"+saveName+ZIP_SUFFIX;


        File fileDir = new File(savedir);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }


        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(savePath));



        for(int i=0;i<files.size();i++) {

            FileInputStream fis = new FileInputStream(files.get(i));

            out.putNextEntry(new ZipEntry(files.get(i).getName()));

            int len;

            //读入需要下载的文件的内容，打包到zip文件
            byte[] buffer = new byte[1024];
            while((len = fis.read(buffer))>0) {

                out.write(buffer,0,len);

            }

            out.closeEntry();

            fis.close();

        }

        out.close();

        System.out.println("生成Demo.zip成功");

    }
}

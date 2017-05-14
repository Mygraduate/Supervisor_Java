package com.graduate.utils.excel;

import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.utils.BeanMapper;
import com.graduate.utils.DateUtil;
import com.graduate.utils.excel.config.ImportField;
import com.graduate.utils.excel.config.TestField;
import com.graduate.utils.excel.exception.FormatException;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.dozer.DozerBeanMapper;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;
import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

/**
 * Created by konglinghai on 2017/4/26.
 */
public class ExcelUtils {

    private final  static String FIELDS = "FIELDS";

    private final  static String SHEET= "SHEET";


    public static void main(String[] args) {
//        String path = "C:\\Users\\Administrator\\Desktop\\demo3.xls";
        List<String> paths = new ArrayList<>();
        for(int i=0;i<1;i++){
            paths.add("C:\\Users\\Administrator\\Desktop\\demo4.xls");
        }
        Date start = new Date();
        int index = 0;
        for(String path : paths){
            try {
                List<ImportField> fields = initConfig();
                HashMap<String,Object> result = analysisExcel(path,fields);
                List<Map<String,String>>data = importExcel(result);
                List<TestField> list = BeanMapper.mapList(data,TestField.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            index++;
            System.out.println(index);
        }
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime());
    }
    //初始化导入配置
    private static List<ImportField> initConfig(){
        List<ImportField> fields = new ArrayList<>();
        fields.add(new ImportField("课程名称","name"));
        fields.add(new ImportField("专业","major"));
        fields.add(new ImportField("年级","grade"));
        fields.add(new ImportField("班级","classes"));

        fields.add(new ImportField("授课方式","type",true,1,0));
        fields.add(new ImportField("授课教师","teacher",true,1,0));
        fields.add(new ImportField("授课内容","content",true,1,0));
        fields.add(new ImportField("上课地点","address",true,1,0));
        fields.add(new ImportField("周次","week",true,1,0));
        fields.add(new ImportField("星期","day",true,1,0));
        fields.add(new ImportField("节次","scope",true,1,0));

        return fields;

    }
    //分析即将导入的文件
    private static HashMap<String,Object> analysisExcel(String path, List<ImportField> fields) throws IOException,FormatException{
        HashMap<String,Object> analysisResult = new HashMap<>();
        int progress = 0;//扫描进度
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(path));
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);//默认获取第一个
        if (hssfSheet == null) {
            throw new FormatException("读取模板文件为空！");
        }
        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if(hssfRow == null){
                continue;
            }
            for(int colNum = 0; colNum<hssfRow.getPhysicalNumberOfCells(); colNum++){
                HSSFCell cell = hssfRow.getCell(colNum);
                if(cell != null && StringUtils.isNoneBlank(cell.getStringCellValue())){
                    String val = cell.getStringCellValue().trim();
                    for(ImportField field : fields){
                        if(val.indexOf(field.getAlias()) != -1){
                            field.setPosition(cell.getRowIndex(),cell.getColumnIndex());
                            field.setComplete(true);
                            progress++;
                            if(progress == fields.size()){
                                //如果所需字段的位置都找到了，提前结束循环
                                analysisResult.put(FIELDS,fields);
                                analysisResult.put(SHEET,hssfSheet);
                                return analysisResult;
                            }
                        }
                    }

                }

            }
        }

        String errorMsg = "";
        for (ImportField field : fields){
            if(!field.isComplete()){
                errorMsg = errorMsg+" "+field.getAlias();
            }
        }
        throw new FormatException("导入课程表格式错误！不存在列"+errorMsg);
    }
    //导入excel
    private static List<Map<String,String>> importExcel(HashMap<String,Object> analysisResult) throws FormatException{
        List<ImportField> fields = (List<ImportField>) analysisResult.get(FIELDS);
        HSSFSheet hssfSheet = (HSSFSheet) analysisResult.get(SHEET);

        if(fields == null || hssfSheet == null){
            throw new FormatException("分析导入文件异常");
        }
        //按行大小排序
        fields.sort(new Comparator<ImportField>() {
            @Override
            public int compare(ImportField o1, ImportField o2) {
                return o1.getRow()-o2.getRow();
            }
        });
        //获取公共属性和多值属性
        HashMap<String,String> singleFields = new HashMap<>();
        HashMap<String,List<String>> multiFields = new HashMap<>();

        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if(hssfRow == null){
                continue;
            }
            for(int colNum = 0; colNum<hssfRow.getPhysicalNumberOfCells(); colNum++){
                HSSFCell cell = hssfRow.getCell(colNum);
                if(cell == null){
                    continue;
                }
                String val =  cell.getStringCellValue();

                for(ImportField field : fields){
                    if (field.getRow() == rowNum && field.getCol() == colNum){
                        if(!field.isMulti()){
                            singleFields.put(field.getName(),val);
                        }else{
                            //excel表格在A4纸出现第二页的情况下处理如下
                            if(val.indexOf(field.getAlias()) == -1 && StringUtils.isNoneBlank(val)){
                                if(!multiFields.containsKey(field.getName())){
                                    List<String> vals = new ArrayList<>();
                                    vals.add(val);
                                    multiFields.put(field.getName(),vals);
                                }else{
                                    multiFields.get(field.getName()).add(val);
                                }
                                field.addMultiCount();
                            }
                            field.getNextRow();//跳出循环后读取下一行
                        }
                        break;
                    }
                }
            }
        }

        int multiCount = 0;
        String errorMsg ="";
        String currentCol="";//选取的某一个多记录属性字段
        for(ImportField field : fields){
            if (field.isMulti()){
                if(multiCount == 0){
                    currentCol = field.getAlias();
                    multiCount = field.getMultiCount();
                }else {
                    if(field.getMultiCount() != multiCount){
                        errorMsg = errorMsg +","+field.getAlias()+"记录数为："+field.getMultiCount();
                    }
                }

            }
        }
        if(StringUtils.isNoneBlank(errorMsg)){
            throw new FormatException("导入课程表列["+errorMsg.substring(1)+"]与["+currentCol+"]记录数："+multiCount+"不一致");
        }

        for(ImportField field : fields){
            if(!field.isMulti()){
                List<String> vals = new ArrayList<>();
                for(int i=0;i<multiCount;i++){
                    vals.add(singleFields.get(field.getName()));
                }
                multiFields.put(field.getName(),vals);
            }
        }
        List<Map<String,String>> list = new ArrayList<>();
        for(int i=0;i<multiCount;i++){
            HashMap<String,String> data = new HashMap<>();
            for(String key : multiFields.keySet()){
                List<String> vals = multiFields.get(key);
                data.put(key,vals.get(i));
            }
            list.add(data);
        }
        return list;
    }
    //自定义配置
    public static<T> List<T> startImport(String path,List<ImportField> configs,Class<T> target) throws IOException, FormatException {
        HashMap<String,Object> result = analysisExcel(path,configs);
        List<Map<String,String>>data = importExcel(result);
        List<T> list = BeanMapper.mapList(data, target);
        return list;
    }
    //默认配置
    public static<T> List<T> startImport(String path,Class<T> target)throws IOException, FormatException{
        List<ImportField> fields = initConfig();
        HashMap<String,Object> result = analysisExcel(path,fields);
        List<Map<String,String>>data = importExcel(result);
        List<T> list = BeanMapper.mapList(data,target);
        return list;
    }

    
}
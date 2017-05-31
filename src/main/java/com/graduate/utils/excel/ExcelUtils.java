package com.graduate.utils.excel;

import com.graduate.system.arrage.model.Arrage;
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
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dozer.DozerBeanMapper;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;
import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

import static com.sun.org.apache.xml.internal.serialize.OutputFormat.Defaults.Encoding;
import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.title;
import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

/**
 * Created by konglinghai on 2017/4/26.
 */
public class ExcelUtils {

    private final static String FIELDS = "FIELDS";

    private final static String SHEET = "SHEET";

    private final static String ARRAGE_SAVE_PATH = "doc\\arrage";

    private final static String EXPORT_EXCEL_SUFFIX = ".xls";

    private final static String EXPROT_TITLE = "教学督导听课安排表";
    public static void main(String[] args) {
////        String path = "C:\\Users\\Administrator\\Desktop\\demo3.xls";
//        List<String> paths = new ArrayList<>();
//        for (int i = 0; i < 1; i++) {
//            paths.add("C:\\Users\\Administrator\\Desktop\\demo4.xls");
//        }
//        Date start = new Date();
//        int index = 0;
//        for (String path : paths) {
//            try {
//                List<ImportField> fields = initConfig();
//                HashMap<String, Object> result = analysisExcel(new FileInputStream(path), fields);
//                List<Map<String, String>> data = importExcel(result);
//                List<TestField> list = BeanMapper.mapList(data, TestField.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            index++;
//            System.out.println(index);
//        }
//        Date end = new Date();
//        System.out.println(end.getTime() - start.getTime());
        try{
            List<Arrage> arrages = new ArrayList<>();
            String [] header = new String[]{"序号", "课程", "授课内容", "授课方式", "专业", "教室", "教师", "周次", "听课时间", "听课人员安排"};
            startExport(String.valueOf(new Date().getTime()),arrages,header);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //初始化导入配置
    private static List<ImportField> initConfig() {
        List<ImportField> fields = new ArrayList<>();
        fields.add(new ImportField("课程名称", "name"));
        fields.add(new ImportField("专业", "major"));
        fields.add(new ImportField("年级", "grade"));
        fields.add(new ImportField("班级", "classes"));

        fields.add(new ImportField("授课方式", "type", true, 1, 0));
        fields.add(new ImportField("授课教师", "teacher", true, 1, 0));
        fields.add(new ImportField("授课内容", "content", true, 1, 0));
        fields.add(new ImportField("上课地点", "address", true, 1, 0));
        fields.add(new ImportField("周次", "week", true, 1, 0));
        fields.add(new ImportField("星期", "day", true, 1, 0));
        fields.add(new ImportField("节次", "scope", true, 1, 0));

        return fields;

    }

    //分析即将导入的文件
    private static HashMap<String, Object> analysisExcel(InputStream inputStream, List<ImportField> fields) throws IOException, FormatException {
        HashMap<String, Object> analysisResult = new HashMap<>();
        int progress = 0;//扫描进度
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);//默认获取第一个
        if (hssfSheet == null) {
            throw new FormatException("读取模板文件为空！");
        }
        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow == null) {
                continue;
            }
            for (int colNum = 0; colNum < hssfRow.getPhysicalNumberOfCells(); colNum++) {
                HSSFCell cell = hssfRow.getCell(colNum);
                if (cell != null && StringUtils.isNoneBlank(cell.getStringCellValue())) {
                    String val = cell.getStringCellValue().trim();
                    for (ImportField field : fields) {
                        if (val.indexOf(field.getAlias()) != -1) {
                            field.setPosition(cell.getRowIndex(), cell.getColumnIndex());
                            field.setComplete(true);
                            progress++;
                            if (progress == fields.size()) {
                                //如果所需字段的位置都找到了，提前结束循环
                                analysisResult.put(FIELDS, fields);
                                analysisResult.put(SHEET, hssfSheet);
                                return analysisResult;
                            }
                        }
                    }

                }

            }
        }

        String errorMsg = "";
        for (ImportField field : fields) {
            if (!field.isComplete()) {
                errorMsg = errorMsg + " " + field.getAlias();
            }
        }
        throw new FormatException("导入课程表格式错误！不存在列" + errorMsg);
    }

    //导入excel
    private static List<Map<String, String>> importExcel(HashMap<String, Object> analysisResult) throws FormatException {
        List<ImportField> fields = (List<ImportField>) analysisResult.get(FIELDS);
        HSSFSheet hssfSheet = (HSSFSheet) analysisResult.get(SHEET);

        if (fields == null || hssfSheet == null) {
            throw new FormatException("分析导入文件异常");
        }
        //按行大小排序
        fields.sort(new Comparator<ImportField>() {
            @Override
            public int compare(ImportField o1, ImportField o2) {
                return o1.getRow() - o2.getRow();
            }
        });
        //获取公共属性和多值属性
        HashMap<String, String> singleFields = new HashMap<>();
        HashMap<String, List<String>> multiFields = new HashMap<>();

        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow == null) {
                continue;
            }
            for (int colNum = 0; colNum < hssfRow.getPhysicalNumberOfCells(); colNum++) {
                HSSFCell cell = hssfRow.getCell(colNum);
                if (cell == null) {
                    continue;
                }
                String val = cell.getStringCellValue();

                for (ImportField field : fields) {
                    if (field.getRow() == rowNum && field.getCol() == colNum) {
                        if (!field.isMulti()) {
                            singleFields.put(field.getName(), val);
                        } else {
                            //excel表格在A4纸出现第二页的情况下处理如下
                            if (val.indexOf(field.getAlias()) == -1 && StringUtils.isNoneBlank(val)) {
                                if (!multiFields.containsKey(field.getName())) {
                                    List<String> vals = new ArrayList<>();
                                    vals.add(val);
                                    multiFields.put(field.getName(), vals);
                                } else {
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
        String errorMsg = "";
        String currentCol = "";//选取的某一个多记录属性字段
        for (ImportField field : fields) {
            if (field.isMulti()) {
                if (multiCount == 0) {
                    currentCol = field.getAlias();
                    multiCount = field.getMultiCount();
                } else {
                    if (field.getMultiCount() != multiCount) {
                        errorMsg = errorMsg + "," + field.getAlias() + "记录数为：" + field.getMultiCount();
                    }
                }

            }
        }
        if (StringUtils.isNoneBlank(errorMsg)) {
            throw new FormatException("导入课程表列[" + errorMsg.substring(1) + "]与[" + currentCol + "]记录数：" + multiCount + "不一致");
        }

        for (ImportField field : fields) {
            if (!field.isMulti()) {
                List<String> vals = new ArrayList<>();
                for (int i = 0; i < multiCount; i++) {
                    vals.add(singleFields.get(field.getName()));
                }
                multiFields.put(field.getName(), vals);
            }
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < multiCount; i++) {
            HashMap<String, String> data = new HashMap<>();
            for (String key : multiFields.keySet()) {
                List<String> vals = multiFields.get(key);
                data.put(key, vals.get(i));
            }
            list.add(data);
        }
        return list;
    }

    //自定义配置
    public static <T> List<T> startImport(InputStream inputStream, List<ImportField> configs, Class<T> target) throws IOException, FormatException {
        HashMap<String, Object> result = analysisExcel(inputStream, configs);
        List<Map<String, String>> data = importExcel(result);
        List<T> list = BeanMapper.mapList(data, target);
        return list;
    }

    //默认配置
    public static <T> List<T> startImport(InputStream inputStream, Class<T> target) throws IOException, FormatException {
        List<ImportField> fields = initConfig();
        HashMap<String, Object> result = analysisExcel(inputStream, fields);
        List<Map<String, String>> data = importExcel(result);
        List<T> list = BeanMapper.mapList(data, target);
        return list;
    }


    public static String startExport(String saveName,List<Arrage> arrages, String[] header)throws Exception {
        String classPath = URLDecoder.decode(String.valueOf(Thread.currentThread().getContextClassLoader().getResource("").getPath()),"UTF-8");
        String savedir = classPath+"\\"+ARRAGE_SAVE_PATH;
        String savePath = savedir+"\\"+saveName+EXPORT_EXCEL_SUFFIX;

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(EXPROT_TITLE);

        //绘制表头
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(EXPROT_TITLE);
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleStyle.setFont(titleFont);
        cell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, header.length-1));

        //创建表头
        HSSFRow headerRow = sheet.createRow(1);
        //创建单元格，设置表头值
        HSSFCellStyle style = workbook.createCellStyle();
        titleFont.setFontHeightInPoints((short) 18);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中格式
        for (int i = 0; i < header.length; i++) {
            HSSFCell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(header[i]);
            headerCell.setCellStyle(style);
        }

        int index = 2;
        int count = 0;
        for (Arrage arrage : arrages) {
            HSSFRow dataRow = sheet.createRow(index);
            for (int i = 0; i < header.length; i++) {
                switch (i) {
                    case 0:
                        //序号
                        dataRow.createCell(i).setCellValue(count + 1);
                        break;
                    case 1:
                        //课程
                        dataRow.createCell(i).setCellValue(arrage.getCourse().getName());
                        break;
                    case 2:
                        //授课内容
                        dataRow.createCell(i).setCellValue(arrage.getCourse().getContent());
                        break;
                    case 3:
                        //授课方式
                        dataRow.createCell(i).setCellValue(arrage.getCourse().getType());
                        break;
                    case 4:
                        //专业
                        dataRow.createCell(i).setCellValue(arrage.getCourse().getMajor());
                        break;
                    case 5:
                        //教室
                        dataRow.createCell(i).setCellValue(arrage.getCourse().getAddress());
                        break;
                    case 6:
                        //教师
                        dataRow.createCell(i).setCellValue(arrage.getCourse().getTeacher());
                        break;
                    case 7:
                        //周次
                        dataRow.createCell(i).setCellValue(arrage.getCourse().getWeek());
                        break;
                    case 8:
                        //听课时间
                        dataRow.createCell(i).setCellValue(arrage.getCourse().getTime());
                        break;
                    case 9:
                        //听课人员安排
                        dataRow.createCell(i).setCellValue(arrage.getGroups());
                        break;
                }
            }
            index ++;
            count++;
        }
        adjustColum(sheet,header);
        addBorder(sheet,workbook,header);
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
        workbook.write(os);
        os.close();

        return savePath;
    }

    /// <summary>
    /// 自动调整列宽自适应
    /// </summary>
    /// <param name="sheet">工作表对象</param>
    private static void adjustColum(Sheet sheet,String [] header)
    {
        for (int columnNum = 0; columnNum < header.length; columnNum++)
        {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;//获取当前列宽度
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++)//在这一列上循环行
            {
                Row currentRow = sheet.getRow(rowNum);
                Cell cell = currentRow.getCell(columnNum);

                int length = cell.toString().getBytes().length;
                if (columnWidth < length + 1)
                {
                    columnWidth = length + 1;
                }//若当前单元格内容宽度大于列宽，则调整列宽为当前单元格宽度，后面的+1是我人为的将宽度增加一个字符
            }
            sheet.setColumnWidth(columnNum, columnWidth * 300);
        }
    }

    /// <summary>
    /// 加边框
    /// </summary>
    /// <param Name="rowindex">1开始</param>
    /// <param Name="cellIndex">1开始</param>
    private  static  void addBorder(Sheet sheet, HSSFWorkbook workbook,String [] header)

    {
        CellStyle styel = workbook.createCellStyle();
        styel.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight((short) 600);
        font.setFontName("宋体");
        styel.setFont(font);
        for (int rowindex = 1; rowindex < sheet.getLastRowNum() + 1; rowindex++)
        {
            for (int cellIndex = 0; cellIndex < header.length; cellIndex++)
            {

                Cell cell = sheet.getRow(rowindex).getCell(cellIndex);
                HSSFCellStyle Style = workbook.createCellStyle();
                Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                Style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
                Style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                Style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                Style.setFont(font);
                cell.setCellStyle(Style);
            }
        }

    }

}
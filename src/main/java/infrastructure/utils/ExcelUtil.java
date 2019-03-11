package infrastructure.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * @author Qiuzh 2018/11/7 13:50
 */
public class ExcelUtil {

    private static final int SHEET_SIZE = 1000;

    /**
     * @param out    数据写入的文件
     * @param data   导入到excel中的数据
     * @param fields 需要注意的是这个方法中的map中：每一列对应的实体类的英文名为键，excel表格中每一列名为值
     * @throws Exception
     */
    public static <T> void listToExcel(OutputStream out, List<T> data,
                                       Map<String, String> fields, ExcelVersion version) throws Exception {
        if (version == null) {
            throw new Exception("请填写导出excel格式枚举参数");
        }
        HSSFWorkbook hssfWorkbook = null;
        XSSFWorkbook xssfWorkbook = null;
        if (version == ExcelVersion.XLS) {
            hssfWorkbook = new HSSFWorkbook();
        } else {
            xssfWorkbook = new XSSFWorkbook();
        }
        // 如果导入数据为空，则抛出异常。
        if (data == null || data.isEmpty()) {
            if (version == ExcelVersion.XLS) {
                hssfWorkbook.close();
            } else {
                xssfWorkbook.close();
            }
            throw new Exception("导入的数据为空");
        }
        // 根据data计算有多少页sheet
        int pages = data.size() / SHEET_SIZE;
        if (data.size() % SHEET_SIZE > 0) {
            pages += 1;
        }
        // 提取表格的字段名（英文字段名是为了对照中文字段名的）
        String[] egTitles = new String[fields.size()];
        String[] cnTitles = new String[fields.size()];
        Iterator<String> it = fields.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String egTitle = it.next();
            String cnTitle = fields.get(egTitle);
            egTitles[count] = egTitle;
            cnTitles[count] = cnTitle;
            count++;
        }
        // 添加数据
        for (int i = 0; i < pages; i++) {
            int rowNum = 0;
            // 计算每页的起始数据和结束数据
            int startIndex = i * SHEET_SIZE;
            int endIndex = (i + 1) * SHEET_SIZE - 1 > data.size() ? data.size()
                    : (i + 1) * SHEET_SIZE - 1;
            // 创建每页，并创建第一行
            HSSFSheet hssfSheet = null;
            XSSFSheet xssfSheet = null;
            HSSFRow hssfRow = null;
            XSSFRow xssfRow = null;
            if (version == ExcelVersion.XLS) {
                hssfSheet = hssfWorkbook.createSheet();
                hssfRow = hssfSheet.createRow(rowNum);
            } else {
                xssfSheet = xssfWorkbook.createSheet();
                xssfRow = xssfSheet.createRow(rowNum);
            }

//            HSSFRow row = sheet.createRow(rowNum);
//            XSSFRow row = sheet.createRow(rowNum);
            // 在每页sheet的第一行中，添加字段名
            for (int f = 0; f < cnTitles.length; f++) {
//                HSSFCell cell = row.createCell(f);
//                XSSFCell cell = row.createCell(f);
                HSSFCell hssfCell;
                XSSFCell xssfCell;
                if (version == ExcelVersion.XLS) {
                    hssfCell = hssfRow.createCell(f);
                    hssfCell.setCellValue(cnTitles[f]);
                } else {
                    xssfCell = xssfRow.createCell(f);
                    xssfCell.setCellValue(cnTitles[f]);
                }
            }
            rowNum++;
            // 将数据添加进表格
            for (int j = startIndex; j < endIndex; j++) {
//                row = sheet.createRow(rowNum);
                if (version == ExcelVersion.XLS) {
                    hssfRow = hssfSheet.createRow(rowNum);
                } else {
                    xssfRow = xssfSheet.createRow(rowNum);
                }
                T item = data.get(j);
                for (int h = 0; h < cnTitles.length; h++) {
                    Field fd = item.getClass().getDeclaredField(egTitles[h]);
                    fd.setAccessible(true);
                    Object o = fd.get(item);
                    String value = o == null ? "" : o.toString();
//                    HSSFCell cell = row.createCell(h);
//                    XSSFCell cell = row.createCell(h);
                    HSSFCell hssfCell;
                    XSSFCell xssfCell;
                    if (version == ExcelVersion.XLS) {
                        hssfCell = hssfRow.createCell(h);
                        hssfCell.setCellValue(value);
                    } else {
                        xssfCell = xssfRow.createCell(h);
                        xssfCell.setCellValue(value);
                    }
                }
                rowNum++;
            }
        }
        // 将创建好的数据写入输出流
//        workbook.write(out);
        // 关闭workbook
//        workbook.close();

        if (version == ExcelVersion.XLS) {
            hssfWorkbook.write(out);
            hssfWorkbook.close();
        } else {
            xssfWorkbook.write(out);
            xssfWorkbook.close();
        }
    }

    /**
     * excel表格转换成对象List集合,兼容HSSF（2003）和XSSF（2007）
     *
     * @param entityClass excel中每一行数据的实体类
     * @param in          excel文件
     * @param fields      字段名字
     *                    需要注意的是这个方法中的map中：
     *                    excel表格中每一列名为键，每一列对应的实体类的英文名为值
     * @throws Exception
     */
    public static <T> List<T> excelToList(InputStream in, Class<T> entityClass,
                                          Map<String, String> fields) throws Exception {

        List<T> resultList = new ArrayList<>();

//        HSSFWorkbook workbook = new HSSFWorkbook(in);
        Workbook workbook = WorkbookFactory.create(in);

        // excel中字段的中英文名字数组
        String[] egTitles = new String[fields.size()];
        String[] cnTitles = new String[fields.size()];
        Iterator<String> it = fields.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String cnTitle = it.next();
            String egTitle = fields.get(cnTitle);
            egTitles[count] = egTitle;
            cnTitles[count] = cnTitle;
            count++;
        }

        // 得到excel中sheet总数
        int sheetCount = workbook.getNumberOfSheets();

        if (sheetCount == 0) {
            workbook.close();
            throw new Exception("Excel文件中没有任何数据");
        }

        // 数据的导出
        for (int i = 0; i < sheetCount; i++) {
//            HSSFSheet sheet = workbook.getSheetAt(i);
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 每页中的第一行为标题行，对标题行的特殊处理
//            HSSFRow firstRow = sheet.getRow(0);
            Row firstRow = sheet.getRow(0);
            //空行跳过
            if (firstRow == null) continue;

            int cellLength = firstRow.getLastCellNum();

            String[] excelFieldNames = new String[cellLength];
            LinkedHashMap<String, Integer> colMap = new LinkedHashMap<>();

            // 获取Excel中的列名
            for (int f = 0; f < cellLength; f++) {
//                HSSFCell cell = firstRow.getCell(f);
                Cell cell = firstRow.getCell(f);
                excelFieldNames[f] = cell.getStringCellValue().trim();
                // 将列名和列号放入Map中,这样通过列名就可以拿到列号
                for (int g = 0; g < excelFieldNames.length; g++) {
                    colMap.put(excelFieldNames[g], g);
                }
            }
            // 由于数组是根据长度创建的，所以值是空值，这里对列名map做了去空键的处理
            colMap.remove(null);
            // 判断需要的字段在Excel中是否都存在
            // 需要注意的是这个方法中的map中：中文名为键，英文名为值
            boolean isExist = true;
            List<String> excelFieldList = Arrays.asList(excelFieldNames);
            for (String cnName : fields.keySet()) {
                if (!excelFieldList.contains(cnName)) {
                    System.err.println("column:[" + cnName + "] is not exist in excel");
                    isExist = false;
                    break;
                }
            }
            // 如果有列名不存在，则抛出异常，提示错误
            if (!isExist) {
                workbook.close();
                throw new Exception("Excel中缺少必要的字段，或字段名称有误");
            }
            // 将sheet转换为list
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
//                HSSFRow row = sheet.getRow(j);
                Row row = sheet.getRow(j);
                //空行跳过
                if (row == null) continue;
                // 根据泛型创建实体类
                T entity = entityClass.newInstance();
                //是否有效对象
                boolean isValid = false;
                // 给对象中的字段赋值
                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    // 获取中文字段名
                    String cnNormalName = entry.getKey();
                    // 获取英文字段名
                    String enNormalName = entry.getValue();
                    // 根据中文字段名获取列号
                    int col = colMap.get(cnNormalName);
                    // 获取当前单元格中的内容
                    String content = "";
                    Cell cell = row.getCell(col);
                    if (cell != null) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                content = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
                            } else {
                                content = NumberToTextConverter.toText(cell.getNumericCellValue());
                            }
                        } else {
                            content = cell.toString().trim();
                        }
                    }
                    // 给对象赋值
                    setFieldValueByName(enNormalName, content, entity);
                    // 只要有一个字段非空,就判定此对象为有效对象
                    if (StringUtils.isNotBlank(content)) isValid = true;
                }
                //有效对象才放入集合
                if (isValid) resultList.add(entity);
            }
        }
        workbook.close();
        return resultList;
    }

    /**
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param o          对象
     * @return 返回 此对象是否位空
     * @MethodName : setFieldValueByName
     * @Description : 根据字段名给对象的字段赋值
     */


    private static void setFieldValueByName(String fieldName,
                                            Object fieldValue, Object o) throws Exception {

        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            // 获取字段类型
            Class<?> fieldType = field.getType();

            // 根据字段类型给字段赋值
            if (String.class == fieldType) {
                field.set(o, String.valueOf(fieldValue));
            } else if ((Integer.TYPE == fieldType)
                    || (Integer.class == fieldType)) {
                field.set(o, Integer.parseInt(fieldValue.toString()));
            } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                field.set(o, Long.valueOf(fieldValue.toString()));
            } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                field.set(o, Float.valueOf(fieldValue.toString()));
            } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                field.set(o, Short.valueOf(fieldValue.toString()));
            } else if ((Double.TYPE == fieldType)
                    || (Double.class == fieldType)) {
                field.set(o, Double.valueOf(fieldValue.toString()));
            } else if (Character.TYPE == fieldType) {
                if ((fieldValue != null)
                        && (fieldValue.toString().length() > 0)) {
                    field.set(o, fieldValue.toString());
                }
            } else if (OffsetDateTime.class == fieldType) {
                field.set(o, DateUtil.dateConvertOffset(
                        DateUtil.parseDate(fieldValue.toString())
                ));
            } else if (Date.class == fieldType) {
                field.set(o, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse(fieldValue.toString()));
            } else {
                field.set(o, fieldValue);
            }
        } else {
            throw new Exception(o.getClass().getSimpleName() + " 类不存在字段名:"
                    + fieldName);
        }
    }

    /**
     * @param fieldName 字段名
     * @param clazz     包含该字段的类
     * @return 字段
     * @MethodName : getFieldByName
     * @Description : 根据字段名获取字段
     */
    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        // 拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        // 如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        // 否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }

        // 如果本类和父类都没有，则返回空
        return null;
    }


    /**
     * excel版本枚举
     */
    public enum ExcelVersion {
        XLS("2003以下版EXCEL"), XLSX("2007以上版EXCEL");
        private String desc;

        public String getDesc() {
            return desc;
        }

        ExcelVersion(String desc) {

            this.desc = desc;
        }
    }

}

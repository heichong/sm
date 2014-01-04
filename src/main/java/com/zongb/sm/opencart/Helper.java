package com.zongb.sm.opencart;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.zongb.sm.entity.Product;

public class Helper {

	/**
	 * 根据原始价格生成新价格
	 * @param oldPrice
	 * @return
	 */
	public static double getNewPrice(double oldPrice){
		
		double newPrice = oldPrice ;
		
		if(oldPrice < 4){
			newPrice = new BigDecimal(4 * oldPrice/6).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
		}else if(oldPrice < 10){
			newPrice = new BigDecimal(3 * oldPrice/6).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
		}else{
			newPrice = new BigDecimal(2 * oldPrice/6).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
		}
		
		return newPrice ;
	}

	/**
	 * 获取进行encode后的url
	 * @param orgUrl
	 * @return
	 */
	public static String getEncodeUrl(String orgUrl){
		String str;
		try {
			str = java.net.URLEncoder.encode(orgUrl,"UTF-8");
			str = str.replaceAll("%2F","/");  
			str = str.replaceAll("%3A",":");  
			str = str.replaceAll("%3F","?"); 
			str = str.replaceAll("%2C",","); 
			str = str.replaceAll("\\+","%20");  
			return str ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "" ;
		}   
	}
	/**
	 * 判断是否存在值，针对长宽高重量等。
	 * @return
	 */
	public static boolean hasValue(Double d){
		return d!=null && d.doubleValue() != 0d ;
	}
	
	
	/**
	 * 获取当前日期，格式为：yyMMdd
	 * @return
	 */
	public static String getCurrentDateStr(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(new Date()) ;
	}
	/**
	 * 获取当前年份
	 * @return
	 */
//	public static String getCurrentYear(){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//		return sdf.format(new Date()) ;
//		
//	}
	/**
	 * 获取当前年月
	 * @return
	 */
	public static String getCurrentYearMonth(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(new Date()) ;
		
	}
	/**
	 * 获取当前月份
	 * @return
	 */
//	public static String getCurrentMonth(){
//		SimpleDateFormat sdf = new SimpleDateFormat("MM");
//		return sdf.format(new Date()) ;
//		
//	}
	
	/**
	 * 从给定的字符串中，提取您需要的信息（根据提供的正则表达式字符串）
	 * 如果匹配不了，则返回空字符串(非null)
	 * @param regex
	 * @param source
	 * @return
	 */
	public static String getMatcher(String regex, String source,int groupIndex) {
			String result = "";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(source);
			if (matcher.find()) {
				result = matcher.group(groupIndex);//只取第一组
			}
			return result;
	}
	
	/**
	 * 输出错误日志到c:/log.xls
	 * @param pList
	 * @throws IOException
	 */
	@Deprecated
	public static int writeErrorLog(List<Product> pList) throws IOException {
        Workbook wb = new HSSFWorkbook(); //or new HSSFWorkbook();

        Sheet sheet = wb.createSheet();
        int i = 0 ;
        boolean hasError = false ;

        Row firstRow = sheet.createRow(i++);
        firstRow.setHeightInPoints(30);
        
        for (Product p : pList) {
        	if(!p.hasError()){
        		continue;
        	}
        	hasError = true ;
        	Row row = sheet.createRow(i++);
            row.setHeightInPoints(30);
            
            createCell(wb, row, (short) 0, p.getModel(), CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
            createCell(wb, row, (short) 1, StringUtils.join(p.getErrList().iterator(), "\\r\\n"), CellStyle.ALIGN_LEFT, CellStyle.VERTICAL_CENTER);
            createCell(wb, row, (short) 2, p.getOriginalUrl(), CellStyle.ALIGN_LEFT, CellStyle.VERTICAL_CENTER);
            
        }

        if(hasError){

            createCell(wb, firstRow, (short) 0, "商品编号", CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
            createCell(wb, firstRow, (short) 1, "错误消息", CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
            createCell(wb, firstRow, (short) 2, "原始链接", CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream("c:/log.xls");
            wb.write(fileOut);
            fileOut.close();
            return i - 1 ;
        }else{
        	return  0 ;
        }
    }
    /**
     * Creates a cell and aligns it a certain way.
     *
     * @param wb     the workbook
     * @param row    the row to create the cell in
     * @param column the column number to create the cell in
     * @param halign the horizontal alignment for the cell.
     */
    private static void createCell(Workbook wb, Row row, short column, String value, short halign, short valign) {
        //CreationHelper ch = wb.getCreationHelper();
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setWrapText(true);
        cell.setCellStyle(cellStyle);
    }
    
    /**
     * 获取错误消息
     * @param pList
     * @return
     * @throws IOException
     */
	public static List<Map<String,String>> getErrorList(List<Product> pList) throws IOException {
		List<Map<String,String>> errorList = new ArrayList<Map<String,String>>() ;
		Map<String,String> map = null ;
        for (Product p : pList) {
        	if(!p.hasError()){
        		continue;
        	}
        	map = new HashMap<String,String>() ;
        	map.put("model", p.getModel()) ;
        	map.put("oriUrl", p.getOriginalUrl()) ;
        	map.put("msg", StringUtils.join(p.getErrList().iterator(), "\\r\\n")) ;
            
        }
        
        return errorList ;
    }

}

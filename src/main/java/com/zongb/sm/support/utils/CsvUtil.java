package com.zongb.sm.support.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;

public class CsvUtil {
	private static final Logger log = LoggerFactory.getLogger(CsvUtil.class);  
	  
    // csv文件格式  
    private static final String CSV_CHARSET = "unicode";  
    // csv文件分隔符(逗号)  
    public static final String CSV_SPLIT_COMMA = "\t";  
  
    /** 
     * 将CSV文件转换为Bean列表 
     *  
     * @param csvFilePath 
     *            CSV文件的本路路径 
     * @param csv_split 
     *            CSV文件中各个字段间的分隔符 
      * @param isReadHead 
     *            是否读取CSV文件头部 
     * @param propMapping 
     *            CSV文件字段顺序(从0开始)与Bean字段的映射 
     * @param clazz 
     *            Bean的class 
     * @return 
     */  
    public static <T> List<T> getBeanList(String csvFilePath, String csv_split, boolean isReadHead, CsvBeanConveter<T> conveter) {  
        List<T> list = new ArrayList<T>();  
        //读取cvs中的所有记录
        List<String[]> records = readCsv(csvFilePath, csv_split,isReadHead);  
        if (records == null || records.size() == 0) {  
  
        } else {  
            for (String[] record : records) {  
            	//通过转化器转化成bean
            	T bean = conveter.rowToBean(record) ;
                if (bean != null) {  
                    list.add(bean);  
                }  
            }  
        }  
  
        return list;  
    }  
  
    /** 
     * 读取CSV文件 
     *  
     * @param csvFilePath 
     *            CSV文件服务器本地路径 
     * @param csv_split 
     *            CSV文件分隔符 
     * @param isReadHead 
     *            是否读取CSV文件头部 
     * @return 
     */  
    private static List<String[]> readCsv(String csvFilePath, String csv_split, boolean isReadHead) {  
        List<String[]> csvList = new ArrayList<String[]>();  
        if (new File(csvFilePath).exists()) {  
            // 读取文件  
            CsvReader reader = null;  
            try {  
                reader = new CsvReader(csvFilePath, csv_split.toCharArray()[0], Charset.forName(CSV_CHARSET));  
                // 跳过表头  
                if (isReadHead) {  
                    reader.readHeaders();  
                }  
                // 逐行读入除表头的数据  
                while (reader.readRecord()) {  
                    csvList.add(reader.getValues());  
                }  
            } catch (FileNotFoundException e) {  
                log.error("", e);  
            } catch (IOException e) {  
                log.error("", e);  
            } finally {  
                // 关闭流  
                reader.close();  
            }  
        }  
        return csvList;  
    }  
  
}

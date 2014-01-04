package com.zongb.sm.support.utils;

public interface CsvBeanConveter<T> {

	/**
	 * 把csv中的一行记录转化为对象T
	 * @param records
	 * @return
	 */
	public T rowToBean(String[] records) ;
}

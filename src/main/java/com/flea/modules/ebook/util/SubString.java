package com.flea.modules.ebook.util;
/**
 * 
 * @ClassName: SubString
 * @Description: 字符串截取的方法
 * @author QL
 * @date 2017年2月27日 下午5:38:52
 */
public class SubString {
	public static String getSubStr(String str, int num) {
		String result = "";
		int i = 0;
		while (i < num) {
			int lastFirst = str.lastIndexOf('/');
			System.out.println(lastFirst);
			result = str.substring(lastFirst) + result;
			System.out.println(result);
			str = str.substring(0, lastFirst);
			System.out.println(str);
			i++;
		}
		return result.substring(1);
	}
	public static String getSubCatalogByIsbn(String isbn) {
		return splitByIndex(isbn, 2) + "/" + splitByIndex(isbn, 1) + "/";
	}

	public static String splitByIndex(String isbn, int index) {
		return isbn.substring(isbn.length() - index, isbn.length() - index + 1);
	}
}

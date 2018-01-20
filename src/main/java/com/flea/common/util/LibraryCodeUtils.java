package com.flea.common.util;

import java.util.ArrayList;
import java.util.List;

import com.flea.common.dao.BaseDao;
import com.flea.modules.customer.dao.CustomerDao;
import com.flea.modules.customer.dao.impl.CustomerDaoImpl;

/**
 * 
 * @ClassName: LibraryCodeUtils
 * @Description:图书馆号的生成工具
 * @author QL
 * @date 2017年4月17日 上午9:48:08
 */
public class LibraryCodeUtils {

	public static List allCodeList = new ArrayList();
	public static List allCodeListOne = new ArrayList();
	public static List allCodeListTwo = new ArrayList();
	public static List allCodeListThree = new ArrayList();
	public static List allCodeListFour = new ArrayList();
	public static List allCodeListFive = new ArrayList();

	public static String getCode(int n, int bit) {
		String str = "";
		String tempStr = "";
		List<String> tempList = new ArrayList<String>();
		while (bit > 0) {
			int m = (n % 26);
			tempStr = (char) (m + 65) + "";
			tempList.add(tempStr);
			str = tempStr + str;

			n = ((n - m) / 26);
			bit--;
		}
		String strOne = tempList.get(3);
		String strTwo = tempList.get(2);
		String strThree = tempList.get(1);
		String strFour = tempList.get(0);

		boolean conditionThree = false;
		boolean conditionFour = false;

		conditionThree = ((strOne.equals(strTwo)) && (strThree.equals(strFour)))
				|| ((strOne.equals(strThree)) && (strTwo.equals(strFour)))
				|| ((strOne.equals(strFour)) && ((strOne.equals(strThree)) || (strOne.equals(strTwo))))
				|| strOne.equals(strFour) && strTwo.equals(strThree)

		;
		conditionFour = (strOne.equals(strTwo)) || (strOne.equals(strThree)) || (strOne.equals(strFour))
				|| (strTwo.equals(strThree)) || (strTwo.equals(strFour)) || (strThree.equals(strFour))
				|| (((int) strOne.charAt(0) == (int) strTwo.charAt(0) - 1)
						&& ((int) strTwo.charAt(0) == (int) strThree.charAt(0) - 1)
						&& ((int) strThree.charAt(0) == (int) strFour.charAt(0) - 1));
		if (tempList.get(0).equals(tempList.get(1)) && tempList.get(1).equals(tempList.get(2))
				&& tempList.get(2).equals(tempList.get(3)) && tempList.get(3).equals(tempList.get(0))) {
			// 一大类

			allCodeListOne.add(str);
		} else if ((tempList.get(0).equals(tempList.get(1)) && tempList.get(1).equals(tempList.get(2)))
				|| (tempList.get(1).equals(tempList.get(2)) && tempList.get(2).equals(tempList.get(3)))) {
			// 二大类

			allCodeListTwo.add(str);
		} else if (conditionThree) {
			// 三大类

			allCodeListThree.add(str);

		} else if (conditionFour) {
			// 四大类
			allCodeListFour.add(str);
		} else {
			// 五大类

			allCodeListFive.add(str);
		}

		return str;
	}

	/**
	 * 获取所有 bit 位数
	 */
	public static void makeAllCode(int bit) {

		double total = Math.pow(26, bit); // 26的4次方
		System.out.println("total :  " + total);

		int i = 0;
		// while (i < total) {
		while (i < total) {
			allCodeList.add(LibraryCodeUtils.getCode(i, bit));
			i++;
		}
		System.out.println("第一大类 " + "共：" + allCodeListOne.size() + "个： " + allCodeListOne);
		System.out.println("第二大类 " + "共：" + allCodeListTwo.size() + "个： " + allCodeListTwo);
		System.out.println("第三大类 " + "共：" + allCodeListThree.size() + "个： " + allCodeListThree);
		System.out.println("第四大类 " + "共：" + allCodeListFour.size() + "个： ");
		System.out.println("第五大类 " + "共：" + allCodeListFive.size() + "个： ");
		// System.out.println(allCodeList);
		System.out.println("共： " + allCodeList.size());

		// for ()
		// if(allCodeList.get(0).equals(allCodeList.get(1)) &&
		// allCodeList.get(1).equals(allCodeList.get(2)) &&
		// allCodeList.get(2).equals(allCodeList.get(3))
		// && allCodeList.get(3).equals(allCodeList.get(0))) {
		//
		// allCodeList.get(0);
		//
		// //
		// String s = "AAAA";
		// s.indexOf(0);
		//
		// }

	}

//	public static String generateUpperString(int n, int bit) {
//		String s = "";
//		while (bit > 0) {
//			int m = (n % 26);
//			s = (char) (m + 65) + s;
//			n = ((n - m) / 26);
//			bit--;
//		}
//		return s;
//	}

	public static void main(String[] args) {
		// makeAllCode(4);
		CustomerDao customerDao = new CustomerDaoImpl();
		LibraryCodeUtils libraryCodeUtils = new LibraryCodeUtils();
		List list= libraryCodeUtils.getLibraryCode("公共图书馆I", 1, customerDao);
	}

	/**
	 * 预分配馆号
	 * 
	 */
	public List<String> perGetLibraryCode(int customerId, int number) {

		return new ArrayList<String>();
	}

	/**
	 * 分配馆号
	 * 
	 */
	public List<String> getLibraryCode(int customerId, int number) {

		return new ArrayList<String>();
	}
	/**
	 * 分配馆号
	 * @param customerLevel 馆级别；number 数量；baseDao dao
	 * @return List 馆号列表
	 */
	public List<String> getLibraryCode(String customerLevel, int number, BaseDao baseDao) {
		List params = new ArrayList();
		List<String> list = null;
		//一级：AAAA 公共图书馆I
		if (customerLevel.equals("公共图书馆I")) {
			params.add(number);
			list = baseDao.getListBySQL("SELECT library_code from lib_code where `level` = 1 and `status` = 0 limit ? ", params);
			System.out.println("list : " + list.size());
			System.out.println("list : " + list.get(0));
		}
		//二级：AAAB\BAAA 公共图书馆II \高校馆I 
		if (customerLevel.equals("公共图书馆II") || customerLevel.equals("高校馆I")) {
			params.add(number);
			list = baseDao.getListBySQL("SELECT library_code from lib_code where `level` = 2 and `status` = 0 limit ? ", params);
			System.out.println("list : " + list.size());
			System.out.println("list : " + list.get(0));
		}
		//三级 AABB\ABAB\BAAB\AABA\ABAA   公共图书馆III\高校馆II\中小学馆I
		if (customerLevel.equals("公共图书馆III") || customerLevel.equals("高校馆II") || customerLevel.equals("中小学馆I")) {
			params.add(number);
			list = baseDao.getListBySQL("SELECT library_code from lib_code where `level` = 3 and `status` = 0 limit ? ", params);
		}
		//四级：AABC\BAAC\BCAA\ABCA\ABAC\CABA \ABCD  高校馆III\中小学馆II
		if (customerLevel.equals("高校馆III") || customerLevel.equals("中小学馆II")) {
			params.add(number);
			list = baseDao.getListBySQL("SELECT library_code from lib_code where `level` = 4 and `status` = 0 limit ? ", params);
		}
		//五级：其余  中小学馆III、农家书屋、社区书屋、社会馆
		if (customerLevel.equals("中小学馆III") || customerLevel.equals("农家书屋") || customerLevel.equals("社区书屋") || customerLevel.equals("社会馆")) {
			params.add(number);
			list = baseDao.getListBySQL("SELECT library_code from lib_code where `level` = 5 and `status` = 0 limit ? ", params);			
		}
		return list;
	}
	
}
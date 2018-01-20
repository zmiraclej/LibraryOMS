/**  
* @Package fleasite
* @Description: TODO
* @author bruce
* @date 2016年1月18日 下午2:34:34
* @version V1.0  
*/ 
package fleasite;

import java.util.Scanner;

import com.flea.common.util.PasswordHelper;

/**
 * @author bruce
 * @2016年1月18日 下午2:34:34
 */
public class TestScanner {

	  public static void main(String[] args) { 
//          Scanner s = new Scanner(System.in); 
//          System.out.println("请输入字符串："); 
//          while (true) { 
//                  String line = s.nextLine(); 
//                 // if (line.equals("exit")) 
//                  System.out.println(">>>" + line); 
//          } 
		  System.out.println(PasswordHelper.getStringRandom(6));
		  
  } 
}

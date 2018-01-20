package com.flea.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * @ClassName: FtpUtils
 * @Description: 文件上传到FTP服务器
 * @author QL
 * @date 2017年1月9日 下午4:53:15
 */
public class FtpUtils {
	private static FTPClient ftp;
	private static String addr = "120.77.80.162";
	private static String username = "oms";
	private static String password = "lA@1*2017%ytsg";

	/**
	 * 
	 * @param path
	 *            上传到ftp服务器哪个路径下
	 * @param addr
	 *            地址
	 * @param port
	 *            端口号
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public boolean connect(String path, String addr, int port, String username,
			String password) throws Exception {
		boolean result = false;
		ftp = new FTPClient();
		
		int reply;
		// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		ftp.connect(addr, port);
		// 登录
		ftp.login(username, password);
		// 设置文件传输模式(被动传输)
		ftp.enterLocalPassiveMode();
		// 设置文件传输类型为二进制
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		//设置增大缓存区
		ftp.setBufferSize(1024*1024);
		// 获取ftp登录应答代码
		reply = ftp.getReplyCode();
		// 验证是否登陆成功
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			return result;
		}
		// 转移到FTP服务器目录至指定的目录下
		ftp.changeWorkingDirectory(path);
		result = true;
		return result;
	}

	/**
	 * 
	 * @param file
	 *            上传的文件或文件夹
	 * @throws Exception
	 */
	public void upload(File file, String fileName, String isbn)
			throws Exception {

		FtpUtils t = new FtpUtils();
		t.connect("", addr, 21, username, password);
		
		// 测试当前file对象表示的文件是否是一个路径
		if (file.isDirectory()) {
			// 返回表示当前对象的文件名,给文件名转换编码
			ftp.makeDirectory(new String(file.getName().getBytes("utf-8"),
					"8859_1"));
			ftp.changeWorkingDirectory(new String(file.getName().getBytes(
					"utf-8"), "8859_1"));
			// 返回当前File对象指定的路径文件列表
			String[] files = file.list();
		
			for (int i = 0; i < files.length; i++) {
				File file1 = new File(file.getPath() + "\\" + files[i]);
				if (file1.isDirectory()) {
					upload(file1, fileName, isbn);
					ftp.changeToParentDirectory();
				} else {
					File file2 = new File(file.getPath() + "\\" + files[i]);
					FileInputStream input = new FileInputStream(file2);
					ftp.storeFile(new String(file2.getName().getBytes("utf-8"),"8859_1"), input);
					input.close();
				}
			}
		} else {
			File f = new File(file.getPath());
			isbn = isbn.substring(0,isbn.lastIndexOf("."));
			FileInputStream input = new FileInputStream(f);
			BufferedInputStream buffInput = new BufferedInputStream(new ByteArrayInputStream(getFileToByte(f)));
			System.out.println("+文件:"+"epub/" + getSubCatalogByIsbn(isbn) + fileName);  
			try {
				ftp.storeFile("epub//" + getSubCatalogByIsbn(isbn) + fileName, buffInput);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("ftp upload exception:"+e.getMessage());
			}
			
			input.close();
			
		}
	}
	
	/**
	 * create 2017-2-27 by gouxl
	 * 文件转为二进制bytes
	 * @param file
	 * @return
	 */
	public static byte[] getFileToByte(File file) {
		byte[] by = new byte[(int) file.length()];
		try {
			InputStream is = new FileInputStream(file);
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			byte[] bb = new byte[2048];
			int ch;
			ch = is.read(bb);
			while (ch != -1) {
				bytestream.write(bb, 0, ch);
				ch = is.read(bb);
			}
			by = bytestream.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return by;
	}

	public static String getSubCatalogByIsbn(String isbn) {
		return splitByIndex(isbn, 2) + "/" + splitByIndex(isbn, 1) + "/";
	}

	public static String splitByIndex(String isbn, int index) {
		return isbn.substring(isbn.length() - index, isbn.length() - index + 1);
	}

}

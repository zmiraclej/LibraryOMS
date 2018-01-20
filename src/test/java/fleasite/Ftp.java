package fleasite;

import java.io.File;

import org.apache.commons.net.ftp.FTPClient;

import com.flea.common.util.FtpUtils;

public class Ftp {
	public static void main(String[] args) throws Exception {
		FTPClient ftp=new FTPClient();
		FtpUtils t = new FtpUtils();
		boolean connect = t.connect("", "120.77.80.162 ", 21, "oms",
				"lA@1*2017%ytsg");
		System.out.println(connect);
		ftp.storeFile(null, null);
		File file = new File("D:\\Program Files\\apache-tomcat-7.0.65\\webapps\\ebooks\\temp\\电子书配发2.2.epub");
		t.upload(file, null, null);
	}
}

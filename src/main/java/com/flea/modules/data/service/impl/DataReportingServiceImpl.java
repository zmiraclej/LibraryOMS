package com.flea.modules.data.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.data.dao.ActivityDetailDao;
import com.flea.modules.data.dao.BorrowedDetailDao;
import com.flea.modules.data.dao.FetchCollectionsDao;
import com.flea.modules.data.dao.LiteratureDetailDao;
import com.flea.modules.data.mapping.ActivityDetail;
import com.flea.modules.data.mapping.BorrowedDetail;
import com.flea.modules.data.mapping.FetchCollections;
import com.flea.modules.data.mapping.LiteratureDetail;
import com.flea.modules.data.pojo.DataReport;
import com.flea.modules.data.pojo.vo.DataAuthoInfo;
import com.flea.modules.data.service.DataReportingService;
import com.flea.modules.data.util.DataReportSQL;
import com.flea.modules.ebook.dao.EbookDao;

/**
 * 数据上报Service
 * 
 * @author zhangjian
 * @version 2017-05-26
 */
@Service
public class DataReportingServiceImpl extends
		BaseServiceImpl<DataReport, Integer> implements DataReportingService {
	
	@Autowired
	EbookDao ebookDao;
	
	@Autowired
	LiteratureDetailDao literatureDetailDao;
	
	@Autowired
	BorrowedDetailDao borrowedDetailDao;
	
	@Autowired
	FetchCollectionsDao fetchCollectionsDao;
	
//  主方法
	@Override
	public String entrance(List<DataAuthoInfo> dataAuthoInfos) {
		String methodName = "";

		for (DataAuthoInfo dataAuthoInfo : dataAuthoInfos) {
//			定义主方法是否二次调用子方法的标志
			int myFlag = 0;
			int hisFlag = 0;
			methodName = dataAuthoInfo.getMethodname();
			if (StringUtils.isNotBlank(methodName)) {
					Method method=null;
					try {
						method = getClass().getDeclaredMethod(methodName,DataAuthoInfo.class,Integer.class);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					while (myFlag==hisFlag) {
						myFlag +=1;
						Map<String, Object> mapObj = null;
						try {
//							调用子方法
							mapObj = (Map<String, Object>) method.invoke(this, dataAuthoInfo,myFlag-1);
						} catch (Exception e) {
							e.printStackTrace();
						}
						hisFlag=(int)mapObj.get("times");
						mapObj.remove("times");
						String param = JSON.toJSONString(mapObj);
						System.out.println("param:" + param);
						String url = dataAuthoInfo.getServerurl()+dataAuthoInfo.getMethodname();
//						发送请求，上传数据
						String result = sendPost(url, param,dataAuthoInfo.getLibcode(),dataAuthoInfo.getUnicode());
//						打印每次上传相应的结果
						JSONObject jsonObject = JSON.parseObject(result);
						Integer state = Integer.valueOf((String)jsonObject.get("state"));
						System.out.println(result);
						if (state==-1) {
							logger.error("次数："+myFlag+"详情："+(String)jsonObject.get("statedesc"));
						}
//						System.out.println("1000的倍数："+myFlag);
			}
			
			}
		
		}
		return null;
	}
//	发送请求
	public static String sendPost(String url, String param,String libcode,String unicode) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Accept-Charset", "UTF-8");  
            conn.setRequestProperty("contentType", "UTF-8");  
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("librarycode", libcode); 
            conn.setRequestProperty("unicode", unicode); 
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            param = URLEncoder.encode(param,"UTF-8");
            out.print(param);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
	
	/**
	 * 接口1
	 * 
	 * @接口名称 : 获取上次上传截止时间
	 * @接口标识: finddeadline
	 * @接口说明: 调用明细接口前，获取上次上传的截止时间，避免重复上传
	 * @指标索引号: 无
	 * @频率: 每次调用明细数据接口前
	 */
	private JSONObject finddeadline(DataAuthoInfo dataAuthoInfo,Integer times)throws Exception {
		JSONObject params = new JSONObject();
		params.put("librarycode", dataAuthoInfo.getLibcode());
		params.put("verification", dataAuthoInfo.getLibcode());
		params.put("times", times);
		return params;
	}
	
	/**
	 * 接口2
	 * 
	 * @接口名称 : 更新本次上传截止时间
	 * @接口标识: updatedeadline
	 * @接口说明: 调用明细接口后，更新本次上传的截止时间 (可将本地记录的添加时间作为截止时间）
	 * @指标索引号: 无
	 * @频率: 每次调用明细数据接口后
	 */
	private Map<String, Object> updatedeadline(DataAuthoInfo dataAuthoInfo,Integer times) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject params = new JSONObject();
		params.put("librarycode", dataAuthoInfo.getLibcode());
		params.put("verification", dataAuthoInfo.getLibcode());
		params.put("deadline", sdf.format(new Date()));
		params.put("times", times);
		return params;
	}
	/**
	 * 接口4
	 * 
	 * @接口名称 :接收公共图书馆年流通册次明细信息接口
	 * @接口标识: borrowedDetail
	 * @接口说明: 接收公共图书馆年流通册次明细信息接口
	 * @指标索引号: C1294
	 * @频率: 10分钟 （评估期数据，每次1000条，增量上传）
	 */
	private JSONObject borrowedDetail(DataAuthoInfo dataAuthoInfo,Integer times) {
		//sql参数准备
		List<Object> values = new ArrayList<Object>();
//		values.add(dataAuthoInfo.getLibcode());
//		values.add(dataAuthoInfo.getLibcode());
		//查询数据库获取数据
		List<BorrowedDetail> borrowedDetails = borrowedDetailDao.getBorrowedDetailsBySql(DataReportSQL.getBorrowedDetailSQL(times), values);
		JSONObject params = new JSONObject();
//		//判断是否一千条，有则二次调用
		if (borrowedDetails.size() == 2) {
			params.put("times", times+1);
		}else {
			System.out.println("最后一次不足2："+borrowedDetails.size());
			params.put("times", times);
		}
//		params.put("times", times);
		StringBuilder sb = new StringBuilder();
		JSONObject tempJson = new JSONObject();
		for (BorrowedDetail borrowedDetail : borrowedDetails) {
			tempJson.put("readercode", (borrowedDetail.getIdCard()==null) ? " " : borrowedDetail.getIdCard());
			tempJson.put("readername", (borrowedDetail.getName()==null) ? " " : borrowedDetail.getName());
			tempJson.put("BorrowID", (borrowedDetail.getId()==null) ? " " : borrowedDetail.getId());
			tempJson.put("barcode", (borrowedDetail.getBarNumber()==null) ? " " : borrowedDetail.getBarNumber());
			tempJson.put("booktitle", (borrowedDetail.getProperTitle()==null) ? " " : borrowedDetail.getProperTitle());
			tempJson.put("bbpname", (borrowedDetail.getOutname()==null) ? " " : borrowedDetail.getOutname());
			tempJson.put("brpname", (borrowedDetail.getInname()==null) ? " " : borrowedDetail.getInname());
			tempJson.put("reborrownum", (borrowedDetail.getReborrownum()==null) ? " " : borrowedDetail.getReborrownum());
			tempJson.put("borrowtype", (borrowedDetail.getBorrowtype()==null) ? " " : borrowedDetail.getBorrowtype());
			tempJson.put("borrowdate", (borrowedDetail.getBorrowDate()==null) ? " " : borrowedDetail.getBorrowDate());
			tempJson.put("returndate", (borrowedDetail.getTheoryReturnDate()==null) ? " " : borrowedDetail.getTheoryReturnDate());
			tempJson.put("realreturndate", (borrowedDetail.getReturnDate()==null) ? " " : borrowedDetail.getReturnDate());
			tempJson.put("bstaffcode", (borrowedDetail.getOperUser()==null) ? " " : borrowedDetail.getOperUser());
			tempJson.put("rstaffcode", (borrowedDetail.getReturnUser()==null) ? " " : borrowedDetail.getReturnUser());
			sb.append(tempJson.toJSONString()+",");
			tempJson.clear();
		}
		if (sb.length()!=0) {
			sb.insert(0,"[").deleteCharAt(sb.length()-1).append("]");
		}
		String dataList = sb.toString();
		params.put("librarycode", dataAuthoInfo.getLibcode());
		params.put("verification", dataAuthoInfo.getMethodcode());
		params.put("sycode", "C1294");
		params.put("branchcode", "");
		params.put("datalist", dataList);
		return params;
		
	}
	
	/**
	 * 接口1
	 * 
	 * @接口名称 : 接收公共图书馆年流通数据量（万册次）接口
	 * @接口标识: borrowedTotal
	 * @接口说明: 获取成员馆或分馆年流通数据
	 * @指标索引号: C1294
	 * @频率: 一次（按年份区分）
	 */
	private JSONObject borrowedTotal(DataAuthoInfo dataAuthoInfo,Integer times){
		List<Object> result = literatureDetailDao.getObjectBySql(DataReportSQL.getLibVisitorsTotalSQL());
		JSONObject params = new JSONObject();
		params.put("times", times);
		StringBuilder sb = new StringBuilder();
		JSONObject tempJson = new JSONObject();
		for (Object object : result) {
			Object[] row = (Object[])object;
			tempJson.put("year", row[0]);
			tempJson.put("borrowed", row[1]);
			sb.append(tempJson.toJSONString()+",");
			tempJson.clear();
		}
		if (sb.length()!=0) {
			sb.insert(0,"[").deleteCharAt(sb.length()-1).append("]");
		}
		String dataList = sb.toString();
		params.put("librarycode", dataAuthoInfo.getLibcode());
		params.put("verification", dataAuthoInfo.getMethodcode());
		params.put("sycode", "C1294");
		params.put("datalist",dataList);
		return params;
	}
	
	/**
	 * 接口12
	 * 
	 * @接口名称 : 接收普通文献编目明细数据接口
	 * @接口标识: literatureDetail
	 * @接口说明: 获取馆内各类型文献的明细信息
	 * @指标索引号: C3404
	 * @频率: 10分钟（评估期数据，每次1000条，增量上传）
	 */
	private JSONObject literatureDetail(DataAuthoInfo dataAuthoInfo,Integer times) {
		// 获得请求体所需数据
		List<Object> values = new ArrayList<Object>();
//		values.add(dataAuthoInfo.getLibcode());
	    List<LiteratureDetail> literatureDetails = literatureDetailDao.getLiteratureDetailsBySql(DataReportSQL.getLiteratureDetailSQL(times), values);
	    JSONObject params = new JSONObject();
		if (literatureDetails.size() == 2) {
			params.put("times", times+1);
		}else {
			params.put("times", times);
		}
//		params.put("times", times);
		StringBuilder sb = new StringBuilder();
		JSONObject tempJson = new JSONObject();
		for (LiteratureDetail literatureDetail : literatureDetails) {
			tempJson.put("catalogcode", literatureDetail.getBarNumber()==null ? "" : literatureDetail.getBarNumber());
			tempJson.put("seriesname", literatureDetail.getCollectionTitle()==null ? "" : literatureDetail.getCollectionTitle());
			tempJson.put("bookname", literatureDetail.getProperTitle()==null ? "" : literatureDetail.getProperTitle());
			tempJson.put("isocode", literatureDetail.getIsbn()==null ? "" : literatureDetail.getIsbn());
			tempJson.put("publisher",literatureDetail.getPress()==null ? "" : literatureDetail.getPress());
			tempJson.put("publishplace", literatureDetail.getIssuePlace()==null ? "" : literatureDetail.getIssuePlace());
			tempJson.put("publishdate", literatureDetail.getPublishDate()==null ? "" : literatureDetail.getPublishDate());
			tempJson.put("author", literatureDetail.getAuthor()==null ? "" : literatureDetail.getAuthor());
			tempJson.put("authormodifier", literatureDetail.getAuthormodifier()==null ? "" : literatureDetail.getAuthormodifier());
			tempJson.put("abstract",literatureDetail.getContentDescript()==null ? "" : literatureDetail.getContentDescript() );
			tempJson.put("topic", literatureDetail.getTopic()==null ? "" : literatureDetail.getTopic());
			//图片路径加根路径
			tempJson.put("coverpicture", literatureDetail.getImagePathL()==null ? "" : literatureDetail.getImagePathL());
			tempJson.put("publishingperiod", literatureDetail.getPublishingperiod()==null ? "" : literatureDetail.getPublishingperiod());
			tempJson.put("reservescopier", literatureDetail.getReplicaRate()==null ? "" : literatureDetail.getReplicaRate());
			tempJson.put("harvestsource", literatureDetail.getName()==null ? "" : literatureDetail.getName());
			tempJson.put("origindate", literatureDetail.getCatalogueDate()==null ? "" : literatureDetail.getCatalogueDate());
			tempJson.put("searchtype", literatureDetail.getSearchtype()==null ? "" : literatureDetail.getSearchtype());
			tempJson.put("mediatype", literatureDetail.getMediatype()==null ? "" : literatureDetail.getMediatype());
			tempJson.put("isdigit", literatureDetail.getIsdigit()==null ? "" : literatureDetail.getIsdigit());
			tempJson.put("fileformat", literatureDetail.getFileformat()==null ? "" : literatureDetail.getFileformat());
			tempJson.put("arttype", literatureDetail.getArttype()==null ? "" : literatureDetail.getArttype());
			tempJson.put("location",literatureDetail.getLocation()==null ? "" : literatureDetail.getLocation() );
			tempJson.put("language", literatureDetail.getLanguage()==null ? "" : literatureDetail.getLanguage());
			tempJson.put("classid",literatureDetail.getClassificationNumber()==null ? "" : literatureDetail.getClassificationNumber());
			tempJson.put("remark", literatureDetail.getRemark()==null ? "" : literatureDetail.getRemark());
			sb.append(tempJson.toJSONString()+",");
			tempJson.clear();
		}
		if (sb.length()!=0) {
			sb.insert(0,"[").deleteCharAt(sb.length()-1).append("]");
		}
		String dataList = sb.toString();
		System.out.println(dataList);
		params.put("librarycode", dataAuthoInfo.getLibcode());
		params.put("verification", dataAuthoInfo.getMethodcode());
		params.put("sycode", "C3404");
		params.put("datalist",dataList);
		return params;
	}
	
	/**
	 * 接口13
	 * 
	 * @接口名称 : 接收公共图书馆馆藏数据接口
	 * @接口标识: fetchCollections
	 * @接口说明: 获取指定书目的成员馆馆藏描述明细信息。
	 * @指标索引号: C3404
	 * @频率: 10分钟（评估期数据，每次1000条，增量上传）
	 */
	private JSONObject fetchCollections(DataAuthoInfo dataAuthoInfo,Integer times){
		List<Object> values = new ArrayList<Object>();
		List<FetchCollections> fetchCollections = fetchCollectionsDao.getFetchCollectionsBySql(DataReportSQL.getFetchCollectionsSQL(times), values);
		 JSONObject params = new JSONObject();
		 if (fetchCollections.size() == 2) {
				params.put("times", times+1);
			}else {
				params.put("times", times);
			}
//		 params.put("times", times);
		 	StringBuilder sb = new StringBuilder();
			JSONObject tempJson = new JSONObject();
			for (FetchCollections fetchCollection : fetchCollections) {
				if (fetchCollection.getBarNumber()!=null) {
					tempJson.put("catalogcode",fetchCollection.getBarNumber());
				}
				tempJson.put("isocode", fetchCollection.getIsbn()==null ? "" : fetchCollection.getIsbn());
				tempJson.put("bookname", fetchCollection.getProperTitle()==null ? "" : fetchCollection.getProperTitle());
				tempJson.put("copier", fetchCollection.getReplicaRate()==null ? "" : fetchCollection.getReplicaRate());
				if (fetchCollection.getVolumes()!=null) {
					tempJson.put("volumes", fetchCollection.getVolumes());
				}
				if (fetchCollection.getBarCode()!=null) {
					tempJson.put("barcode", fetchCollection.getBarCode());
				}
				tempJson.put("indexnumber", fetchCollection.getIndexnumber()==null ? "" : fetchCollection.getIndexnumber());
				tempJson.put("roomlocation", fetchCollection.getStoreroom()==null ? "" : fetchCollection.getStoreroom());
				tempJson.put("holdlocation", fetchCollection.getFrameCode()==null ? "" : fetchCollection.getFrameCode());
				tempJson.put("libstatus", fetchCollection.getBookState()==null ? "" : fetchCollection.getBookState());
				tempJson.put("appenddate", fetchCollection.getStorageTime()==null ? "" : fetchCollection.getStorageTime());
				sb.append(tempJson.toJSONString()+",");
				tempJson.clear();
			}
			if (sb.length()!=0) {
				sb.insert(0,"[").deleteCharAt(sb.length()-1).append("]");
			}
			String dataList = sb.toString();
			params.put("librarycode", dataAuthoInfo.getLibcode());
			params.put("verification", dataAuthoInfo.getMethodcode());
			params.put("sycode", "C3404");
			params.put("datalist",dataList);
			return params;
	}
	
	/**
	 * 接口2
	 * 
	 * @接口名称 : 接收公共图书馆读者办证数据接口
	 * @接口标识: userDetail
	 * @接口说明: 接收成员馆中的读者数据
	 * @指标索引号: C1292
	 * @频率: 每周增量
	 */
	private JSONObject userDetail(DataAuthoInfo dataAuthoInfo,Integer times){
		return null;
	}

	/**
	 * 接口1
	 * 
	 * @接口名称 : 接收公共图书馆用户总数接口
	 * @接口标识: userTotal
	 * @接口说明: 获取成员馆读者用户总数
	 * @指标索引号: C1292
	 * @频率: 一次/周
	 */
	private JSONObject userTotal(DataAuthoInfo dataAuthoInfo,Integer times){
		Object result = literatureDetailDao.getOneBySQL(DataReportSQL.getUserTotalSQL(), new ArrayList<Object>());
		JSONObject params = new JSONObject();
		params.put("times", times);
		params.put("librarycode", dataAuthoInfo.getLibcode());
		params.put("verification", dataAuthoInfo.getMethodcode());
		params.put("sycode", "C1292");
		params.put("readertype",1);
		if (result!=null) {
			String readercount = result.toString();
			params.put("readercount",readercount);
		}else {
			params.put("readercount",0);
		}
		return params;
	}
	
	/**
	 * 接口4
	 * 
	 * @接口名称 :接收年读者到馆量接口
	 * @接口标识: libVisitorsTotal
	 * @接口说明: 接收年读者到馆量（人次）接口
	 * @指标索引号: C1293
	 * @频率: 一次（按年份区分
	 */
	private JSONObject libVisitorsTotal(DataAuthoInfo dataAuthoInfo,Integer times){
		List<Object> result = literatureDetailDao.getObjectBySql(DataReportSQL.getLibVisitorsTotalSQL());
		JSONObject params = new JSONObject();
		params.put("times", times);
		StringBuilder sb = new StringBuilder();
		JSONObject tempJson = new JSONObject();
		for (Object object : result) {
			Object[] row = (Object[])object;
			tempJson.put("year", row[0]);
			tempJson.put("total", row[1]);
			sb.append(tempJson.toJSONString()+",");
			tempJson.clear();
		}
		if (sb.length()!=0) {
			sb.insert(0,"[").deleteCharAt(sb.length()-1).append("]");
		}
		String dataList = sb.toString();
		params.put("librarycode", dataAuthoInfo.getLibcode());
		params.put("verification", dataAuthoInfo.getMethodcode());
		params.put("sycode", "C1293");
		params.put("datalist",dataList);
		return params;
	}
	
	/**
	 * 接口4
	 * 
	 * @接口名称 :接收活动详情
	 * @接口标识: activityDetail
	 * @接口说明: 获取成员馆活动元数据信息，提供本接口功能的系统需要自行查重处理
	 * @指标索引号: C1304
	 * @频率: 10分钟（评估期数据，每次200条，增量上传）
	 */
	
	@Autowired
	ActivityDetailDao activityDetailDao;
	
	private JSONObject activityDetail(DataAuthoInfo dataAuthoInfo,Integer times){
		List<Object> values = new ArrayList<Object>();
		List<ActivityDetail> activityDetails = activityDetailDao.getActivityDetailBySql(DataReportSQL.getactivityDetailSQL(times), values);
		 JSONObject params = new JSONObject();
		 if (activityDetails.size() == 2) {
				params.put("times", times+1);
			}else {
				params.put("times", times);
			}
//		 params.put("times", times);
		 	StringBuilder sb = new StringBuilder();
			JSONObject tempJson = new JSONObject();
			for (ActivityDetail activityDetail : activityDetails) {
				tempJson.put("code",activityDetail.getId());
				tempJson.put("title", activityDetail.getTitle());
				tempJson.put("abstracts", activityDetail.getContent()==null ? "" : activityDetail.getContent());
				tempJson.put("keywords", activityDetail.getSensitiveWord()==null ? "" : activityDetail.getSensitiveWord());
				tempJson.put("url", activityDetail.getUrl()==null ? "" : activityDetail.getUrl());
				tempJson.put("address", activityDetail.getActiveAddress()==null ? "" : activityDetail.getActiveAddress());
				tempJson.put("startdate", activityDetail.getStartDate());
				tempJson.put("continuenum", activityDetail.getContinuenum()==null ? "" : activityDetail.getContinuenum());
				tempJson.put("enddate", activityDetail.getEndDate());
				tempJson.put("periodnum", 0);
				tempJson.put("status",1);
				tempJson.put("portable", 1);
				tempJson.put("contactman", activityDetail.getContactman()==null ? "" : activityDetail.getContactman());
				tempJson.put("contactphone", activityDetail.getContactphone()==null ? "" : activityDetail.getContactphone());
				tempJson.put("sponsor", activityDetail.getSponsor()==null ? "" : activityDetail.getSponsor());
				tempJson.put("sponsorphone", activityDetail.getSponsorphone()==null ? "" : activityDetail.getSponsorphone());
				tempJson.put("organizer",activityDetail.getOrganizer()==null ? "" : activityDetail.getOrganizer());
				tempJson.put("organizerphone", activityDetail.getOrganizerphone()==null ? "" : activityDetail.getOrganizerphone());
//				tempJson.put("partingnum", "");需要数字类型，不能传空字符串过去
//				tempJson.put("partednum", "");需要数字类型，不能传空字符串过去
				tempJson.put("commentsnum", 0);
				
				sb.append(tempJson.toJSONString()+",");
				tempJson.clear();
			}
			if (sb.length()!=0) {
				sb.insert(0,"[").deleteCharAt(sb.length()-1).append("]");
			}
			String dataList = sb.toString();
			params.put("librarycode", dataAuthoInfo.getLibcode());
			params.put("verification", dataAuthoInfo.getMethodcode());
			params.put("sycode", "C1304");
			params.put("datalist",dataList);
			return params;
	}
	
	/**
	 * 接口11
	 * 
	 * @接口名称 :接收文献的馆藏量
	 * @接口标识: genaralCollection 
	 * @接口说明: 接收文献的馆藏量(册件)
	 * @指标索引号: C3404
	 * @频率: 一次（按年份区分）
	 */
	private JSONObject genaralCollection(DataAuthoInfo dataAuthoInfo,Integer times) {
		JSONObject params = new JSONObject();
		System.out.println(times);
		if (times==0) {
			Object ebookResult = literatureDetailDao.getOneBySQL(DataReportSQL.getgenaralCollectionEbookSQL(), new ArrayList<Object>());
			params.put("librarycode", dataAuthoInfo.getLibcode());
			params.put("verification", dataAuthoInfo.getMethodcode());
			params.put("sycode", "C3404");
			params.put("deadline", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			params.put("restype", "2");
			if (ebookResult!=null) {
				String collectioncount = ebookResult.toString();
				params.put("collectioncount",collectioncount);
			}else {
				params.put("collectioncount",0);
			}
			params.put("source", "云图书馆");//电子书厂商提供者
			params.put("times", times+1);
			return params;
		}else{
			Object normalbookResult = literatureDetailDao.getOneBySQL(DataReportSQL.getgenaralCollectionNormalbookSQL(), new ArrayList<Object>());
			params.put("librarycode", dataAuthoInfo.getLibcode());
			params.put("verification", dataAuthoInfo.getMethodcode());
			params.put("sycode", "C3404");
			if (normalbookResult!=null) {
				String collectioncount = normalbookResult.toString();
				params.put("collectioncount",collectioncount);
			}else {
				params.put("collectioncount",0);
			}
			params.put("deadline", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			params.put("restype", "1");
			params.put("times", times);
			return params;
		}
	}
	
	/**
	 * 接口6
	 * 
	 * @接口名称 : 接收活动总数接口
	 * @接口标识: activityTotal
	 * @接口说明: 接收活动总数接口
	 * @指标索引号: C1304
	 * @频率: 一次（按年份区分）
	 */
	private JSONObject activityTotal(DataAuthoInfo dataAuthoInfo,Integer times) {
		JSONObject params = new JSONObject();
		List<Object> result = literatureDetailDao.getObjectBySql(DataReportSQL.getactivityTotalSQL());
		int flag = 0;
		StringBuilder sb = new StringBuilder();
		JSONObject tempJson = new JSONObject();
		for (Object object : result) {
			if (flag==0) {//当年数据不用上报
				continue;
			}else if (flag==4) {//4年以前的数据不用上报
				break;
			}else {
				Object[] row = (Object[])object;
				tempJson.put("year", row[0]);
				tempJson.put("total", row[1]);
				sb.append(tempJson.toJSONString()+",");
				tempJson.clear();
			}
			flag +=flag;
		}
		if (sb.length()!=0) {
			sb.insert(0,"[").deleteCharAt(sb.length()-1).append("]");
		}else {
			sb.append("[]");
		}
		String dataList = sb.toString();
		params.put("librarycode", dataAuthoInfo.getLibcode());
		params.put("verification", dataAuthoInfo.getMethodcode());
		params.put("sycode", "C1304");
		params.put("times", times);
		params.put("datalist",dataList);
		return params;
	}
}

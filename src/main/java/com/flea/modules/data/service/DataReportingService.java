package com.flea.modules.data.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.service.BaseService;
import com.flea.modules.data.pojo.DataReport;
import com.flea.modules.data.pojo.vo.DataAuthoInfo;

public interface DataReportingService extends BaseService<DataReport, Integer>{

	String entrance(List<DataAuthoInfo> dataAuthoInfos);
	
}

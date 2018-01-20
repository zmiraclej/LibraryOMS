package com.flea.modules.report.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.Common;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.dao.LightCirculationDao;
import com.flea.modules.report.dao.ScattergramDao;
import com.flea.modules.report.pojo.LightCirculation;
import com.flea.modules.report.pojo.Scattergram;
import com.flea.modules.report.pojo.vo.ScattergramQueryCriteria;
import com.flea.modules.report.service.ScattergramService;
import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Function 全景分布图service实现类
 * @Author DaiBo
 * @DATE 2017/5/17 11:33
 */
@Service
public class ScattergramServiceImpl  extends BaseServiceImpl<Scattergram,Integer> implements ScattergramService {

    @Autowired
    private ScattergramDao scattergramDao;

    @Autowired
    private LightCirculationDao lightCirculationDao;

    @Override
    public List<Scattergram> list(ScattergramQueryCriteria query) {
        User user =  ShiroUtils.getCurrentUser();
        String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
        if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
            query.setCustomerId(user.getCustomer().getId());
        }
        return scattergramDao.list(query);
    }

    @Override
    @Transactional
    public List<Map<String,Object>> listLight(ScattergramQueryCriteria query){
        User user =  ShiroUtils.getCurrentUser();
        String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
        if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
            query.setCustomerId(user.getCustomer().getId());
        }

        //热度数据有效期暂定为 7天. 超过七天则更新最新的.
        Date date = (Date)(lightCirculationDao.getUpdateLightColorDate().get("updateDate"));
        long now = System.currentTimeMillis();

        if(date==null){
            lightCirculationDao.updateLightInfo();
        }else{
            if(now/(1000 * 60 * 60 * 24)-7 > date.getTime()/(1000 * 60 * 60 * 24)){
                lightCirculationDao.updateLightInfo();
            }
        }
        // 默认为不点亮.
        List<LightCirculation> lightList = scattergramDao.listLight(query);
        List lightListResult = new ArrayList();
        Map<Object,Object> map;
        String lightFlag;
        LightCirculation lightCirculation;
        for (int i=0;i<lightList.size();i++){
            lightCirculation = lightList.get(i);
            map = new HashMap<>();
            lightFlag = getLightFlag(lightCirculation);
            lightCirculation.setLightOption("");
            map.put("libraryCode",lightCirculation.getLibraryCode());
            map.put("latitude",lightCirculation.getLatitude());
            map.put("longitude",lightCirculation.getLongitude());
            map.put("totalBook",lightCirculation.getTotalBook());
            map.put("totalBookBelong",lightCirculation.getTotalBookBelong());
            map.put("lightFlag",lightFlag);
            lightListResult.add(map);
        }
        //联合查询取得数据.
        return lightListResult;
    }

    /**
     * 根据 light_option 字段信息,计算现在是否点亮.
     * @param lightCirculation
     * @return
     */
    private static String getLightFlag(LightCirculation lightCirculation){
        String lightFlag = "close";
        ;
        if (lightCirculation != null) {
            JSONObject jsonObject = JSONObject.parseObject(lightCirculation.getLightOption());
            if(jsonObject==null){
                return lightFlag;
            }
            String week = jsonObject.getString("week");
            JSONArray jsonArray = JSONArray.parseArray(week);
            for (int i = 0; i < jsonArray.size(); i++) {
                Object object = jsonArray.get(i);
                if (Integer.parseInt(object.toString()) == dayForWeek()) { //今天的星期数 在配置中
                    if(inTimeRange(jsonObject)){ //时间对比, 时间在配置范围内
                        lightFlag = "open";
                        continue;
                    }
                }
            }
        }
        return lightFlag;
    }

    /**
     * 返回今天是星期几
     * @return
     * @throws Exception
     */
    private static int dayForWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 返回当前时间是否在开放时间段内
     * @param jsonObject
     * @return
     */
    private static boolean inTimeRange(JSONObject jsonObject)  {
        boolean isIn = false; // 默认为false
        String weekTime = jsonObject.getString("weekTime");
        String weekTimeAm = JSONObject.parseObject(weekTime).getString("am");
        String weekTimePm = JSONObject.parseObject(weekTime).getString("pm");
        String weekAmBegin = JSONObject.parseObject(weekTimeAm).getString("begin");
        String weekAmEnd = JSONObject.parseObject(weekTimeAm).getString("end");
        String weekPmBegin = JSONObject.parseObject(weekTimePm).getString("begin");
        String weekPmEnd = JSONObject.parseObject(weekTimePm).getString("end");
        DateFormat df = new SimpleDateFormat("HH:mm");
        Calendar now = Calendar.getInstance(); //可以对每个时间域单独修改
        String nowStr = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
        try {
            Long nowTime = df.parse(nowStr).getTime();
            Long weekAmBeginTime = df.parse(weekAmBegin).getTime();
            Long weekAmEndTime = df.parse(weekAmEnd).getTime();
            Long weekPmBeginTime = df.parse(weekPmBegin).getTime();
            Long weekPmEndTime = df.parse(weekPmEnd).getTime();
            if((nowTime > weekAmBeginTime && nowTime <weekAmEndTime)
                    || (nowTime > weekPmBeginTime && nowTime < weekPmEndTime)){
                isIn = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isIn;
    }


    public static void main(String[] args) {
        LightCirculation lightCirculation = new LightCirculation();
        lightCirculation.setLightOption("{\"date\":\"2017-03-30 15:34\",\"week\":[1,2,5,3,4,6],\"weekTime\":{\"am\":{\"end\":\"12:00\",\"begin\":\"09:00\"},\"pm\":{\"end\":\"21:00\",\"begin\":\"14:00\"}},\"dayTime\":{\"am\":{\"end\":\"12:00\",\"begin\":\"09:00\"},\"pm\":{\"end\":\"21:00\",\"begin\":\"14:00\"}}}");
        String flag = getLightFlag(lightCirculation);
        System.out.println("-");
        System.out.println(flag);
    }
}

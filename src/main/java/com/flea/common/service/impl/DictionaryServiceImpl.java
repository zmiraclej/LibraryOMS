package com.flea.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flea.common.dao.DictionaryDao;
import com.flea.common.pojo.Dictionary;
import com.flea.common.pojo.InitData;
import com.flea.common.service.DictionaryService;
@Service
public class DictionaryServiceImpl implements DictionaryService{

	@Resource
	private DictionaryDao dictionaryDao;
	@Override
	public List<Dictionary> listDictionary() {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("from Dictionary as dictionary ");
		List<Dictionary> list = dictionaryDao.getListByHQL(buffer.toString(), null);
		return list;
	}
	
	public  void initDictionary(){
		List<Dictionary> list = listDictionary();
		for(Dictionary l:list){
			InitData.dictionary.put(l.getCode(), l);
		}
	}

}

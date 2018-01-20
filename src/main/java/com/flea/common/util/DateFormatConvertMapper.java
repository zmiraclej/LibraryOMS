/**  
* @Package com.flea.cms.utils
* @Description: TODO
* @author bruce
* @date 2015年11月26日 下午3:38:41
* @version V1.0  
*/ 
package com.flea.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;


/**
 * @author bruce
 * @2015年11月26日 下午3:38:41
 */
public class DateFormatConvertMapper extends ObjectMapper {

	 public DateFormatConvertMapper(){  
//	        CustomSerializerFactory factory = new CustomSerializerFactory();  
//	        factory.addGenericMapping(Date.class, new JsonSerializer<Date>(){  
//	            @Override  
//	            public void serialize(Date value,   
//	                    JsonGenerator jsonGenerator,   
//	                    SerializerProvider provider)  
//	                    throws IOException, JsonProcessingException {  
//	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
//	                jsonGenerator.writeString(sdf.format(value));  
//	            }  
//	        });  
//	        this.setSerializerFactory(factory);  
	    }  
}

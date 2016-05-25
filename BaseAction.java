package com.ldp.datahub.action;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.ldp.datahub.common.Constant;
import com.ldp.datahub.exception.LinkServerException;
import com.ldp.datahub.util.RequestUtil;

import net.sf.json.JSONObject;
// 测试提交
public class BaseAction {
	private static Log log = LogFactory.getLog(BaseAction.class);
	
	public void setResponseStatus(Map<String, Object> jsonMap,HttpServletResponse response){
		Integer code = (Integer)jsonMap.get(Constant.result_code);
		 if(code>0){
    		 if(code==Constant.fail_code){
    			 response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		 }
    		 else{
    			 response.setStatus(HttpStatus.BAD_REQUEST.value());
    		 }
    	 }else{
    		 response.setStatus(HttpStatus.OK.value());
    	 }
	}
	 
	 public boolean isAdminOrI(String me,String loginName,Map<String, Object> jsonMap) throws LinkServerException{
		try {
			if (StringUtils.isEmpty(me)) {
				log.error("please login:"+loginName);
				jsonMap.put(Constant.result_code, Constant.no_login_code);
				jsonMap.put(Constant.result_msg, Constant.no_login);
				return false;
			} 
			else {
				if (!me.equals(loginName)) {
					JSONObject json = RequestUtil.getUserInfo(me);
					JSONObject user = json.getJSONObject(Constant.result_data);
					if (user.getInt("userType") != Constant.UserType.ADMIN) {
						log.error(me + " no auth");
						jsonMap.put(Constant.result_code, Constant.no_auth_code);
						jsonMap.put(Constant.result_msg, Constant.no_auth);
						return false;
					}
				}

			}
			return true;
		}
		catch (Exception e) {
			throw new LinkServerException("link user server error");
		}
	}
	 
	 public boolean isAdmin(String me,Map<String, Object> jsonMap) throws LinkServerException{
		 try {
			 JSONObject json = RequestUtil.getUserInfo(me);
			 JSONObject user = json.getJSONObject(Constant.result_data);
				if(user.getInt("userType")!=Constant.UserType.ADMIN){
					log.error(me+" no auth");
					jsonMap.put(Constant.result_code, Constant.no_auth_code);
					jsonMap.put(Constant.result_msg, Constant.no_auth);
					return false;
				}
				return true;
			
		 }
		 catch (Exception e) {
				throw new LinkServerException("link user server error");
		}
	 }	 
	 
}

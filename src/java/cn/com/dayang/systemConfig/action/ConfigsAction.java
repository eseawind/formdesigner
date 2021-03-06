package cn.com.dayang.systemConfig.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.com.dayang.systemConfig.domain.SystemConfig;
import cn.com.dayang.systemConfig.service.ConfigService;
import cn.com.dayang.systemConfig.web.AppAction;

import com.opensymphony.xwork2.ModelDriven;

import flexjson.JSONSerializer;

@SuppressWarnings("serial")
@Controller("configsAction")
@Log4j
public class ConfigsAction extends AppAction  implements ModelDriven<SystemConfig> {
	
	@Autowired
	private ConfigService configService = null;
	
	
	
	
	private SystemConfig model = new SystemConfig();
	
	public void setModel(SystemConfig model) {
		this.model = model;
	}
	
	public SystemConfig getModel() {
		return model;
	}
	
	public void save() throws IOException {
		log.info("saving config...");
		log.debug("The new model: " + model.getConfigName());
		configService.addConfig(model);
		
		String guid = model.getConfigGuid();
		log.debug("the result id: " + guid);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		
		JSONResponse ret = JSONResponse.saveSuccess(guid);
		JSONSerializer s = new JSONSerializer().exclude("*.class");
		String json = s.serialize(ret);
		
		this.responseJson(response, json);
	}
	
	public String getById() throws IOException {
		log.info("get by id...");
		log.debug("--guid: " + model.getConfigGuid());
		
		SystemConfig config = configService.findConfigById(model.getConfigGuid());
		
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONResponse ret = JSONResponse.getSuccess(config);
		JSONSerializer s = new JSONSerializer().include("body").exclude("*.class");
		String json = s.serialize(ret);
		
		log.debug("response json: \n" + json);
		response.setHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "must-revalidate");
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		
		this.responseJson(response, json);
		
		return null;
	}
	
	public String findTmpls() throws IOException {
		log.info("find news and topics tmpls....");
		
		List<SystemConfig> list = configService.findTmpls();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		JSONSerializer s = new JSONSerializer().exclude("*.class");
		String json = s.serialize(list);
		
		this.responseJson(response, json);
		
		return null;
	}
	
	public String delById() throws IOException {
		log.info("remove SystemConfig ById ...");
		log.debug("--guid: " + model.getConfigGuid());
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String guid = model.getConfigGuid();
		
		SystemConfig config = configService.findConfigById(guid);
		
		if(config == null) {
			JSONResponse ret = JSONResponse.delFail(guid);
			JSONSerializer s = new JSONSerializer().exclude("*.class");
			String json = s.serialize(ret);
			
			this.responseJson(response, json);
			return null;
		}
		
		configService.deleteConfig(config);

		
		JSONResponse ret = JSONResponse.delSuccess(guid);
		JSONSerializer s = new JSONSerializer().exclude("*.class");
		String json = s.serialize(ret);
		
		this.responseJson(response, json);
		
		return null;
	}
	
	public static void main(String[] args) {
		SystemConfig obj = new SystemConfig();
		JSONResponse resp = new JSONResponse();
		resp.setBody(obj);
		
		JSONSerializer s = new JSONSerializer().include("body").exclude("*.class");
		String json = s.serialize(resp);
		System.out.println(json);
	}
	
}


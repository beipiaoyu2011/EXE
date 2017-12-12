package controllers.register;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import controllers.gogoalweb.DataHandler;
import ggframework.bottom.log.GGLogger;
import gogoal.api.GoGoalAPI;
import play.libs.Images;
import play.mvc.Controller;


public class ValidCodeAction  extends Controller {
	
	public static  void buildValidCode(String tmstamp) {
		System.setProperty("java.awt.headless","true");
    	Images.Captcha captcha = Images.captcha(100,50);
    	List<Font> fonts = new ArrayList<Font>();
    	fonts.add(new Font("DialogInput", 2, 24));
    	fonts.add(new Font("SansSerif", 2, 25));
    	captcha.fonts = fonts;
    	captcha.addNoise("#333333");
    	captcha.setBackground("10002060");
		String randomCode = captcha.getText(4);
		
		Map<String, String> params = new HashMap<String,String>();
		try {
			params.put("validcode", randomCode);
			String ip = DataHandler.getClientIp(request);
			params.put("ip", ip);
			GoGoalAPI.post("v1/valid/set_img_code", params, ip);
			renderBinary(captcha);
		} catch (Exception e) {
			play.Logger.error(e, "API请求异常");
			JsonObject json = new JsonObject();
			json.addProperty("code", 500);
			json.addProperty("message", "login异常");
			renderJSON(json.toString());
		}
	}
	
}

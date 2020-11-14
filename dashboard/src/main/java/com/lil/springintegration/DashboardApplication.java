package com.lil.springintegration;

import com.lil.springintegration.manage.DashboardManager;
import com.lil.springintegration.domain.AppProperties;
import com.lil.springintegration.service.StatusMonitorService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@Controller
public class DashboardApplication {

	private static DashboardManager dashboardManager;

	private static Logger logger = LoggerFactory.getLogger(DashboardApplication.class);

	public static void main(String[] args) {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/application.xml", DashboardApplication.class);
		AppProperties props = (AppProperties) context.getBean("appProperties");
		dashboardManager = new DashboardManager();
		SpringApplication.run(DashboardApplication.class, args);
		logger.info("Open this application in your browser at http://localhost:" + props.getRuntimeProperties().getProperty("server.port", "") + ". (Modify port number in src/main/resources/application.properties)");
		dashboardManager.initCallback();
		context.close();
	}

	@GetMapping("/")
	public String dashboard(Model model) {
		model.addAttribute("status", DashboardManager.getDashboardStatus());
		return "dashboard";
	}

	@RequestMapping(value = "/api")
	public ResponseEntity<Object> getProducts() {
		String payload = simulateRestApiResponse();
		//System.out.println("API Returning: " + payload);
		return new ResponseEntity<>(payload, HttpStatus.OK);
	}

	private static String simulateRestApiResponse() {
		Random random = new Random();
		JSONObject json = new JSONObject();
		try {
			json.put("snapTime", new Date().toString());
			json.put("updateRequired", random.nextBoolean());
			json.put("netSolar", random.nextInt(40));
			json.put("netWind", random.nextInt(40));
		} catch (JSONException e) {
			logger.info(e.toString());
		}
		return json.toString();
	}

}

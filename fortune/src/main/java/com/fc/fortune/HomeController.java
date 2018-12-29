package com.fc.fortune;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static Properties prop;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping("/fortune")
	public String fortune(Model model) {
		//python script ������ �������� num1, num2�� �Ķ���ͷ� �ѱ� �� num1+num2 �� ���� ����ϴ��� test�ϴ� �޼ҵ�
		int num1 = 10;
		int num2 = 20;
		String pythonPath  = prop.getProperty("pythonPath");
		
		try{
			ProcessBuilder pb = new ProcessBuilder("python","model.py",""+num1, ""+num2);
			pb.directory(new File(pythonPath));
			Process p = pb.start();

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			int result = Integer.parseInt(in.readLine());
			
			model.addAttribute("result", "python result is : " + result);
			
			}catch(Exception e){
				System.out.println(e);
			}
		
		return "fortune";
	}
	
	@PostConstruct //Controller ������ �Բ� init �޼ҵ�� ����. config�� �о�� �� �ֵ���!
	public static void loadConfig(){
		prop = new Properties();
		
		String config = "config.properties";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource(config); 

		try {
			prop.load(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

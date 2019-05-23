package com.ydhw.ip.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Remeber
 * 
 * Mozilla/5.0 (Linux; Android 6.0; vivo Y67A Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko)
 *  Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36 MicroMessenger/7.0.3.1400(0x2700033C) 
 *  Process/appbrand2 NetType/4G Language/zh_CN
 *  
 * 
 	+ 匹配一个或者多个。
 *  * 号代表字符可以不出现，也可以出现一次或者多次（0次、或1次、或多次）。
 *  ? 问号代表前面的字符最多只可以出现一次（0次、或1次）。
 *  \s	匹配任何空白字符，包括空格、制表符、换页符等等。等价于 [ \f\n\r\t\v]。注意 Unicode 正则表达式会匹配全角空格符。
	\S	匹配任何非空白字符。等价于 [^ \f\n\r\t\v]。
 * 
 * @author hp
 *
 */
public class MMM {
	public static void main(String[] args) throws IOException {

		// 匹配手机型号关键字(Build/)
//		Pattern pattern = Pattern.compile(";\\s?(\\S*?)\\s?(Build)/");
		// 匹配网络信号 关键字(NetType/)
		// 发现请求中有NetType/WIFI"【少见，也不晓得是否是正常】,还有NetType/WIFI La..【常见，应该是正常数据】
		Pattern pattern2 = Pattern.compile("\\s?NetType/(\\S*)[\", ' ']");
		FileReader fr=new FileReader("E:/Test/ua.txt");
        BufferedReader br=new BufferedReader(fr);
        String line="";
        while ((line=br.readLine())!=null) {
        	Matcher matcher = pattern2.matcher(line); 
            String model = null; 
            if (matcher.find()) { 
            	model = matcher.group(1);
//            	model = matcher.group(1).split("\"")[0].trim();
            	if(model.length()>4 && model.length()<8 &&  !"NON_NETWORK".equals(model)) {
            		model = model.split("\"")[0].trim();
            		System.out.println("这个长度大于4的请求的手机型号是：" + model);;
            	}
            	else if(model.length()<=2 && !"4G".equals(model) && model.length()>0){
//            		System.out.println("这个长度小于=4的请求的手机型号是：" + model);;
            	}
            		
            		
                 
            }
        }
		
		
	}
}

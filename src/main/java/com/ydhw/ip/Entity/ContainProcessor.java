package com.ydhw.ip.Entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sargeraswang.util.ExcelUtil.ExcelLogs;
import com.sargeraswang.util.ExcelUtil.ExcelUtil;
import com.ydhw.ip.Entity.baseEntity.SingleCityEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置文件信息太多 1 国家级别 2 省 3 市 4 县级/区
 * 
 * @author hp
 *
 */

@Getter
@Setter
@Controller
@RequestMapping("/ddd")
public class ContainProcessor {
	private DbConfig dbconfig;

//	@Autowired(required = false)
//	@Qualifier("dbpath")
	private String dbpath = "E:/Test/ip2region.db";
//
//	@Autowired(required = false)
//	@Qualifier("errpath")
	private String errpath = "E:/workspace/ip2region/data/error_log.txt";

	private DbSearcher search;

	// 存放city相关信息
	private Map<Integer, SingleCityEntity> cityMap = Maps.newConcurrentMap();
	// 存放city信息
	private Map<String, SingleCityEntity> cityNameMap = Maps.newConcurrentMap();
//
//	@Autowired(required = false)
//	@Qualifier("cityFilePath")
	private String cityFilePath = "E:/workspace/ip2region/data/asdasd.xlsx";

	public ContainProcessor() {
		try {
			init();
		} catch (FileNotFoundException | DbMakerConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() throws DbMakerConfigException, FileNotFoundException {
		// 初始化配置--ip模块 eg:[0.0.0.0|0.255.255.255|0|0|0|内网IP|内网IP]
		dbconfig = new DbConfig();
		search = new DbSearcher(dbconfig, dbpath);

		// 初始化配置--city模块
		File f = new File(cityFilePath);
		InputStream inputStream = new FileInputStream(f);
		ExcelLogs logs = new ExcelLogs();
		Collection<Map> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs, 0);
		for (Map<String, String> m : importExcel) {
			if (null == m.get("id"))
				continue;
			try {
				SingleCityEntity temp = new SingleCityEntity();
				temp.setCityLevel(Integer.parseInt((String) m.get("cityLevel")));
				temp.setCityCode(Integer.parseInt((String) m.get("cityCode")));
				temp.setId(Integer.parseInt((String) m.get("id")));
				temp.setName((String) m.get("name"));
				temp.setProvinceCode((Integer.parseInt((String) m.get("provinceCode"))));
				cityMap.put(temp.getId(), temp);
//				System.out.println((String) m.get("name"));
				cityNameMap.put((String) m.get("name"), temp);

			} catch (Exception e) {
				// TODO: handle exception
				System.err.println(e);
				e.printStackTrace();
			}

		}
	}

	public List<SingleCityEntity> getByIpList(List<String> IPList) throws IOException {
		List<SingleCityEntity> result = Lists.newArrayList();
		for (String ip : IPList) {
			DataBlock memorySearchResult = search.memorySearch(ip);
			if (memorySearchResult != null) {
				result.add(cityMap.get(memorySearchResult.getCityId()));
			}
		}
		return result;
	}

	@ResponseBody
	@RequestMapping("/cccc")
	public SingleCityEntity getByid(String ip) throws IOException {
		DataBlock memorySearchResult = search.memorySearch(ip);
		SingleCityEntity entity = null;
		if (memorySearchResult.getCityId() == 0) {
			System.out.println(memorySearchResult);
			String region = memorySearchResult.getRegion().trim();
			String cityName = region.split("\\|")[3];
			if (!"0".equals(cityName)) {
				entity = cityNameMap.get(cityName);
			}
			// 不是城市的直接过滤 外国的应该没有 将有问题的数据存储到日志中
			Logger.getLogger("ip-"+ip+"---region:"+region);
		} else {
			entity = cityMap.get(memorySearchResult.getCityId());
		}
		if (entity != null) {
			return entity;
		} else {
			return null;
		}
	}

	@ResponseBody
	@RequestMapping("/dddd")
	public List<SingleCityEntity> postt(String ip) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> result = Lists.newArrayList();

		if (ip != null && !ip.isEmpty())
			result.add(ip);
		else {
			System.out.println("参数异常");
			return null;
		}
//		result.add("183.14.29.253");// shenzhen
//		result.add("123.125.71.38");// beijing
//		result.add("27.18.198.235");// wuhan
//		result.add("113.108.182.52");// guangzhou
//		result.add("27.22.92.20");// xiangyang
//		result.add("27.28.52.23");// yichang
//		result.add("101.69.62.7");// taizhou
//		result.add("27.23.191.147");// huanggang
//		
//		for (int i = 0; i < 100000; i++) {
//			result.add("101.69.62.7");// taizhou
//		}
		List<SingleCityEntity> byIpList = Lists.newArrayList();
		if (!result.isEmpty()) {
			byIpList = getByIpList(result);
		}
		long timeEnd = System.currentTimeMillis();

		System.err.println("消耗时长" + (timeEnd - timeStart) + "millis");
		return byIpList;
	}

	public static void main(String[] args) throws FileNotFoundException {

		String add = "中国|0|新疆|阿克苏|电信";
		String[] split = add.split("\\|");
		System.out.println(split.length);
		System.out.println(split[3]);
	}

}

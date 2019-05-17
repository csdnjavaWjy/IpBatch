package com.ydhw.ip.Entity.searchResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResult {
	private String countryName;
	private String cityName;
	private String cityCode;
	
	private String provinceCode;
	private String provinceName;
	//城市级别: 一线/新一线/二线/三线/四线/五线
	private int cityType;
	
}

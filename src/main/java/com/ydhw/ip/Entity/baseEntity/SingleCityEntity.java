package com.ydhw.ip.Entity.baseEntity;

import com.sargeraswang.util.ExcelUtil.ExcelCell;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 从excel中导入到内存的实体类型
 * 
 * @author hp
 *
 */
@Getter
@Setter
@ToString
public class SingleCityEntity {
	@ExcelCell(index = 0)
	private int id;
	/**
	 * 
	 */
	@ExcelCell(index = 1)
	private int provinceCode;
	/**
	 * 	可能是国家名 也可能是城市名称(一般也用不到国家)
	 */
	@ExcelCell(index = 2)
	private String name;

	/**
	 * 1，表示国家 2，表示省级/直辖市/自治区 3，市级 4，区,县
	 */
	@ExcelCell(index = 3)
	private int cityLevel;

	/**
	 * 	该地区是什么级别的城市 1 一线 2 新一线 3 二线 4 三线 5 四线 6 五线
	 */
	@ExcelCell(index = 4)
	private int cityType;
	


}

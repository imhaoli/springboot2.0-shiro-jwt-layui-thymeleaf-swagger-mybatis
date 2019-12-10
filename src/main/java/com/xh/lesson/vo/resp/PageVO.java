package com.xh.lesson.vo.resp;

import lombok.Data;

import java.util.List;

/**
* @ClassName:       PageVO
*                   分页VO
* @Author:          小霍
* @CreateDate:      2019/9/19 13:25
* @UpdateUser:      小霍
* @UpdateDate:      2019/9/19 13:25
* @Version:         0.0.1
*/
@Data
public class PageVO<T>  {
	/**
	 * serial_number
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 总记录数
	 */
	private Long totalRows;
	/**
	 * 总页数
	 */
	private Integer totalPages;
	/**
	 * 当前第几页
	 */
	private Integer pageNum;
	/**
	 * 每页记录数
	 */
	private Integer pageSize;
	/**
	 * 当前页记录数
	 */
	private Integer curPageSize;
	/**
	 * 数据列表
	 */
	private List<T> list;

}

package com.cat.origin.common.result;

import java.io.Serializable;
import java.util.List;

/**
 */
public class PageResult implements Serializable {
	private static final long serialVersionUID = 1L;
	// 总记录数
	private long total;
	// 列表数据
	private List<?> rows;

	/**
	 * 分页
	 *
	 * @param list
	 *            列表数据
	 * @param totalCount
	 *            总记录数
	 * @param pageSize
	 *            每页记录数
	 * @param currPage
	 *            当前页数
	 */
	public PageResult(List<?> list, long total) {
		this.rows = list;
		this.total = total;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

}

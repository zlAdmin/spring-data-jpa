package com.zl.jpa.common.dao;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryMap implements Serializable {
	private static final long serialVersionUID = -2682649394691545410L;
	private Integer CURRENT_ROWS_SIZE = 20;

	public Integer getCURRENT_ROWS_SIZE() {
		return CURRENT_ROWS_SIZE;
	}

	public void setCURRENT_ROWS_SIZE(Integer cURRENT_ROWS_SIZE) {
		CURRENT_ROWS_SIZE = cURRENT_ROWS_SIZE;

	}

	public void setPage(Integer cURRENT_ROWS_SIZE, int displayStart) {
		this.CURRENT_ROWS_SIZE = cURRENT_ROWS_SIZE;
		this.pageNum = displayStart / cURRENT_ROWS_SIZE + 1;
	}

	private Integer totalRows;
	private Integer pageNum;
	private String connector = " and ";
	public String searchQuery;

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public List<String> dataProp;

	public List<String> getDataProp() {
		return dataProp;
	}

	public void setDataProp(List<String> dataProp) {
		this.dataProp = dataProp;
	}

	public String getConnector() {
		return connector;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageSize) {
		this.pageNum = pageSize;
	}

	private String sortColumn;
	private String sortDesc;
	private Object entity;
	private Map<String, String> aliasMap = new HashMap<String, String>();
	private Map<String, String> operMap = new HashMap<String, String>();
	private Map<String, String> actualMap = new HashMap<String, String>();
	private Map<String, Object> valueMap = new HashMap<String, Object>();

	public String getSortAndDesc(String orders) {
		if (StringUtils.hasText(this.sortColumn)) {
			if (StringUtils.hasText(this.sortDesc))
				return " order by " + sortColumn + " " + sortDesc;
			return " order by " + sortColumn;
		}
		return orders;
	}

	public Map<String, String> getAliasMap() {
		return aliasMap;
	}

	public void setAliasColumn(String column) {
		String[] columns = column.split("\\.");
		if (columns.length >= 2)
			this.aliasMap.put(getColumn(columns[0].length(), column), column);
		if (columns.length == 1)
			this.aliasMap.put(columns[0], columns[0]);
	}

	public void setAliasColumn(String column, String operation) {
		setAliasColumn(column);
		String[] columns = column.split("\\.");
		if (columns.length >= 2)
			this.operMap.put(getColumn(columns[0].length(), column), operation);
		if (columns.length == 1)
			this.operMap.put(columns[0], operation);
	}

	public void setAliasColumn(String column, String operation,
			String actualColumn) {
		setAliasColumn(column, operation);
		String[] columns = column.split("\\.");
		if (columns.length >= 2)
			this.actualMap.put(getColumn(columns[0].length(), column),
					actualColumn);
		if (columns.length == 1)
			this.actualMap.put(columns[0], actualColumn);
	}

	private String getColumn(int index, String column) {
		return column.substring(index + 1);
	}

	public void setColumnValue(String column, Object value) {
		this.valueMap.put(column, value);
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public Map<String, String> getOperMap() {
		return operMap;
	}

	public void setOperMap(Map<String, String> operMap) {
		this.operMap = operMap;
	}

	public Map<String, String> getActualMap() {
		return actualMap;
	}

	public void setActualMap(Map<String, String> actualMap) {
		this.actualMap = actualMap;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortDesc() {
		return sortDesc;
	}

	public void setSortDesc(String sortDesc) {
		this.sortDesc = sortDesc;
	}

	public Map<String, Object> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
	}

}

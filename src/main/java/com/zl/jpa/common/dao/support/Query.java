package com.zl.jpa.common.dao.support;


import com.zl.jpa.common.dao.QueryMap;
import com.zl.jpa.util.GenericsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Query {
	private static Logger log = LoggerFactory.getLogger(Query.class);

	@SuppressWarnings({ "unused", "rawtypes" })
    public static String queryAccession(String hql, QueryMap pageLimit) {
		String result;

		Map<String, String> aliasMap = pageLimit.getAliasMap();
		Map<String, String> operMap = pageLimit.getOperMap();
		Map<String, String> actualMap = pageLimit.getActualMap();
		Map<String, Object> entity = pageLimit.getValueMap();

		StringBuffer buffer = new StringBuffer();
		Iterator iterator = entity.keySet().iterator();
		while (iterator.hasNext()) {
			try {
				String column = (String) iterator.next();
				Object fieldValue = null;

				fieldValue = ((Map) entity).get(column);

				if (fieldValue != null && !fieldValue.equals("")) {// 如果变量值非空

					String oper = " = ";

					String fieldColumn = column;//要查询的字段名称，暂时不使用对象的反射和取值
					
					oper = operMap.get(column) == null ? oper : operMap
							.get(column);

					buffer.append(" " + pageLimit.getConnector() + " ");
					
					if (oper.toLowerCase().equals("like")) {
						buffer.append(fieldColumn + " " + oper + "'%'||:"
								+ column + "||'%'");
					} else {
						buffer.append(fieldColumn + oper + ":"
								+ column.replace(".", ""));
					}
				}
			} catch (Exception e) {
			    log.error(e.getMessage(), e);
			}
		}
		result = GenericsUtil.removeOrders(hql)
				+ ((hql.indexOf("where") > 0 || hql.indexOf("WHERE") > 0) ? buffer
						.toString() : buffer.toString().replaceFirst("and",
						"where"))
				+ pageLimit.getSortAndDesc(GenericsUtil.ordersfromHql(hql));
		log.debug(result);
		return result;
	}

	public static String queryAccession(String hql, List<String> dataProp,
			String fieldValue) {
		String result;

		StringBuffer buffer = new StringBuffer();
		for (String column : dataProp) {
			java.util.regex.Pattern pattern = java.util.regex.Pattern
					.compile("[0-9]*");
			java.util.regex.Matcher match = pattern.matcher(column);
			if (match.matches() == false) {
				try {
					if (fieldValue != null && !fieldValue.equals("")) {// 如果变量值非空
						String oper = " like ";
						buffer.append(" or ");
						buffer.append(column + " " + oper + "'%'||:" + column
								+ "||'%'");
					}

				} catch (Exception e) {
				    log.error(e.getMessage(), e);
				}
			}
		}
		result = GenericsUtil.removeOrders(hql)
				+ ((hql.indexOf("where") > 0 || hql.indexOf("WHERE") > 0) ? buffer
						.toString() : buffer.toString().replaceFirst("or",
						"where")) + (GenericsUtil.ordersfromHql(hql));
		return result;
	}
}

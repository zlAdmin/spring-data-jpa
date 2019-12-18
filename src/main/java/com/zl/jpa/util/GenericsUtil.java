package com.zl.jpa.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings({"rawtypes","unchecked"})
public class GenericsUtil {
	private static Logger log = LoggerFactory.getLogger(GenericsUtil.class);

	/**
	 * 将通用对象s转换为long类型，如果字符穿为空或null，返回r；
	 * 
	 * @param s
	 * @param r
	 * @return
	 */
	public static long obj2long(Object s, long r) {
		long i = r;

		String str = "";
		try {
			str = s.toString();
			i = Long.parseLong(str);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return i;
	}

	/**
	 * 将通用对象s转换为String类型，如果字符穿为空或null，返回r；
	 * 
	 * @param s
	 * @param r
	 * @return
	 */
	public static String obj2Str(Object s, String r) {
		String str = r;
		try {
			str = s.toString();
			if (str.equals("null") || str == null || str.trim().length() == 0) {
				str = r;
			}
		} catch (Exception e) {
			str = r;
		}
		return str;
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public templateManager extends
	 * GenricManager<Book>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or <code>Object.class</code> if
	 *         cannot be determined
	 */
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
	 * GenricManager<Book>
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or <code>Object.class</code> if
	 *         cannot be determined
	 */
	public static Class getSuperClassGenricType(Class clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName()
					+ "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName()
					+ "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName()
					+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}

	/**
	 * 去除hql的select 子句，未考虑union的情况,用于pagedQuery.
	 * 
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	@SuppressWarnings("deprecation")
	public static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql
				+ " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 转换sql语句变为读取记录条数的语句,用于pagedQuery.
	 * 
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	@SuppressWarnings("deprecation")
	public static String convertSelect(String hql) {
		Assert.hasText(hql);
		String result = "";
		if (hql.toLowerCase().indexOf("union") > 0) {
			result = "select count(1) from (" + hql + ")";
		} else if (hql.toLowerCase().indexOf("distinct") > 0) {
			result = "select count(*) from (" + hql + ")";
		} else {
			int beginPos = hql.toLowerCase().indexOf("from");
			Assert.isTrue(beginPos != -1, " hql : " + hql
					+ " must has a keyword 'from'");
			result = "select count(1) " + hql.substring(beginPos);
		}

		return result;
	}

	/**
	 * 获取hql中的order by 子句
	 * 
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String ordersfromHql(String hql) {

		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("order by");

		if (beginPos > 0)
			return " " + hql.substring(beginPos);
		return "";

	}

	public static String changeComma(String comma) {
		if (GenericsUtil.isNull(comma)) {
			return comma.replaceAll(",", "','");
		}
		return "";
	}

	/**
	 * 删除字符串的前后逗号
	 * 
	 * @param string
	 * @return
	 */
	public static String trimComma(String string) {
		try {
			if (string != null && string.trim().length() > 0) {
				if (string.lastIndexOf(",") == string.length() - 1) {
					string = string.substring(0, string.length() - 1);
				}
				if (string.indexOf(",") == 0) {
					string = string.substring(1, string.length());
				}
			} else {
				string = "";
			}
		} catch (Exception e) {
		    log.error(e.getMessage(),e);
		}
		return string;
	}

	/**
	 * 根据正则截取HQL
	 * 
	 * @param hql
	 * @param regular
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String removeInHql(String hql, String regular) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 * 
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	public static String removeOrders(String hql) {
		return removeInHql(hql, "order\\s*by[\\w|\\W|\\s|\\S]*");
	}

	/**
	 * 去除hql的where子句，用于pagedQuery
	 * 
	 * @param hql
	 * @return
	 */
	public static String removeWheres(String hql) {
		return removeInHql(hql, "where[\\w|\\W|\\s|\\S]*");
	}

	/**
	 * Encode a string using algorithm specified in web.xml and return the
	 * resulting encrypted password. If exception, the plain credentials string
	 * is returned
	 * 
	 * @param password
	 *            Password or other credentials to use in authenticating this
	 *            username
	 * @param algorithm
	 *            Algorithm used to do the digest
	 * 
	 * @return encypted password based on the algorithm.
	 */
	public static String encodePassword(String password) {
		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			log.error("Exception: " + e);

			return password;
		}

		md.reset();

		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// now calculate the hash
		byte[] encodedPassword = md.digest();

		StringBuffer buf = new StringBuffer();

		for (byte anEncodedPassword : encodedPassword) {
			if ((anEncodedPassword & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString(anEncodedPassword & 0xff, 16));
		}

		return buf.toString();
	}

	/**
	 * @author liangyang 判断字符串是否为NULL或者为空字符串
	 * @param str
	 * @return 如果是null或者空则返回false，反之，返回true
	 */
	public static boolean isNull(String str) {
		if (null != str && !"".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 将通用对象s转换为long类型，如果字符穿为空或null，返回r；
	 * 
	 * @param s
	 * @param r
	 * @return
	 */
	public static int obj2int(Object s, int r) {
		int i = r;

		String str = "";
		try {
			str = s.toString();
			i = Integer.parseInt(str);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return i;
	}

	public static String nullToString(Object s) {
		try {
			if (s != null) {
				return String.valueOf(s);
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	/**
	 * 截取字符用？标识返回
	 * 
	 * @return
	 */
	public static String getQueryNum(String ids) {
		StringBuffer flag = new StringBuffer();
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			if (id.toString().equals(""))
				flag.append("?");
			else
				flag.append(",?");
		}
		return flag.toString();
	}

	public static List<String> getHavingId(String allId, String compareId,
			boolean flag) {
		List<String> list = new ArrayList<String>();
		Map map = new HashMap();
		String[] allIdArray = allId.split(",");
		String[] compareIdArray = compareId.split(",");
		for (String allid : allIdArray) {
			if (allid.contains("_"))
				map.put(allid, allid);
		}
		if (flag) {
			for (String compareid : compareIdArray) {
				if (map.get(compareid) != null)
					list.add(compareid);
			}
		} else {
			for (String compareid : compareIdArray) {
				if (map.get(compareid) == null)
					list.add(compareid);
			}
		}
		return list;
	}

	public static Map getIdMap(String allId, String addId, String removeId) {
		Map map = new HashMap();

		String[] allArray = allId.split(",");
		String[] addArray = addId.split(",");
		String[] removeArray = removeId.split(",");

		for (String allid : allArray) {
			map.put(allid, allid);
		}

		for (String addid : addArray) {
			map.put(addid, addid);
		}

		for (String removeid : removeArray) {
			map.remove(removeid);
		}

		return map;

	}

	public static boolean isNumeric(String str) {
		if (str.matches("\\d *")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getRandNumber(int length){
		Random rand = new Random();
		String code ="";
		for(int i=0;i<length;i++){
			code += rand.nextInt(10);
		}
		return code ;
	} 

	public static String get32UUID(){
		String uuid = UUID.randomUUID()+"";
		uuid = uuid.replaceAll("-","");
		return uuid;
	}
}

package com.platform.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sqlca {

	private static Logger logger = LoggerFactory.getLogger(Sqlca.class);

	/***
	 * 更新数据根据sql(参数用?作为占位符)和传过来的参数，参数只支持String,可事务控制
	 * @param sql
	 * @param objs
	 * @return
	 * @throws Exception
	 */
	public static int updateObject(Connection conn,String sql,String[] objs)
	{
		logger.info(sql);
		logger.info(objs.toString());
		int cnt = 0;
		PreparedStatement ps = null;
		try
		{
			ps = conn.prepareStatement(sql);
			int len = objs.length;
			for(int i=1; i<=len; i++)
			{
				ps.setString(i, objs[i-1]);
			}

			cnt = ps.executeUpdate();
		}catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			closeAll(conn,null,ps);
		}
		return cnt;

	}

	/***
	 * 得到一个数据库字段，只支持查询一个字段，只返回第一条记录的一个字段
	 * @param sql
	 * @return String
	 * @throws Exception
	 */
	public static String getString(Connection conn,String sql)
	{
		logger.info(sql);
		String obj = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next())
			{
				obj = rs.getString(1);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			closeAll(conn,rs,ps);
		}
		return obj;
	}

	public static StringBuffer getStringBuffer(String sql,Connection conn) throws Exception {
		StringBuffer obj = new StringBuffer();
//		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next())
		{
			obj = obj.append("'").append(rs.getString(1)).append("'").append(",") ;
		}
		closeAll(conn,rs,ps);
		return obj;
	}

	/**
	 * 返回ArrayList<Map> 将sql语句中的字段用map对象封装到ArrayList返回
	 * @param sql
	 * @param conn 指定的数据库连接不能传空
	 * @return ArrayList<Map<String,Object>>
	 * @throws Exception
	 */
	public static ArrayList<Map<String,Object>> getArrayListFromMap(String sql,Connection conn) {
		logger.info(sql);
		ArrayList<Map<String,Object>> array = new ArrayList<Map<String,Object>>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				for(int i = 1;i<=metaData.getColumnCount();i++) {
					String columname =metaData.getColumnLabel(i);
					String columnValue = rs.getString(i);

					if(columnValue==null)
					{
						columnValue = "";
					}
					map.put(columname, columnValue);
				}
				array.add(map);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			closeAll(conn,rs,ps);
		}
		return array;
	}


	/***
	 *   返回一个ArrayList<Object>,Object字段与数据库字段一致才能匹配上
	 * @param sql
	 * @param clss 指定的Bean
	 * @param conn 指定的数据库连接不能传空
	 * @return ArrayList<?>
	 * @throws Exception
	 */
	public static ArrayList<Object> getArrayListFromObj(String sql,Class<?> clss,Connection conn){
		logger.info(sql);
		ArrayList<Object> array = new ArrayList<Object>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Object obj = clss.newInstance();
				Field[] fields = obj.getClass().getFields();
				for(Field f : fields) {
					String fieldname = f.getName();
					if("int".equals(f.getType().toString())){
						f.set(obj, rs.getInt(fieldname));
					}else{
						f.set(obj, rs.getString(fieldname));
					}
				}
				array.add(obj);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			closeAll(conn,rs,ps);
		}
		return array;
	}


	/**
	 * 关闭连接
	 * @param conn
	 * @param rs
	 * @param ps
	 */
	public static void closeAll(Connection conn,ResultSet rs,PreparedStatement ps) {

		try {
			if(rs!=null) {
				rs.close();
			}
			if(ps!=null) {
				ps.close();
			}
			if(conn!=null) {
				conn.close();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null && !rs.isClosed()) {
					rs.close();
				}
				if(ps!=null && !ps.isClosed()) {
					ps.close();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

	}
}

package com.servlet.parent_servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import tools.json.JsonReader;

import com.bean.CheckMessage;

import db.connect.DBConnect;

public class ReceiveCheck extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CheckMessage check = null;
		
		JSONObject json = JsonReader.receivePost(request);
		JSONObject returnJson = new JSONObject();
		
		try{
			check = (CheckMessage)JSONObject.toBean(json, CheckMessage.class);			
		}catch(Exception e){
			e.printStackTrace();
		}
		int flag = dataOpt(check);
		if (flag == 1){
			returnJson.put("check_flag", 1);
			returnJson.put("reason", 0);
		}
		else if (flag == 2){
			returnJson.put("check_flag", 1);
			returnJson.put("reason", 1);
		}
		else{
			returnJson.put("check_flag", 0);
			returnJson.put("reason", 2);
		}

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(returnJson.toString());
		out.flush();
		out.close();
	}
	
	public int dataOpt (CheckMessage cm){
		ResultSet rs = null;
		String class_sql = "select * from class where school_name = '" + cm.getSchool_name() + "' and class_name = '" + cm.getClass_name() + "';";
		try{
			rs = DBConnect.queryData(class_sql);
			if (rs.next()){
				String parent_sql = "select * from parent where parent_tel = '" + cm.getParent_tel() + "';";
				ResultSet parent_rs = DBConnect.queryData(parent_sql);
				if (parent_rs.next()){
					Long class_id = (Long)rs.getLong("class_id");
					String checked_sql = "select * from checked where parent_id = '" + parent_rs.getLong("parent_id") + "' and class_id = '" + class_id + "';";
					if (DBConnect.queryData(checked_sql).next()){
						return 0;
					}
					else{
						String uncheck_sql = "select * from uncheck where parent_id = '" + parent_rs.getLong("parent_id") + "' and class_id = '" + class_id + "';";
						if (DBConnect.queryData(uncheck_sql).next()){
							String updata_sql = "update uncheck set uncheck_comment = '" + cm.getUncheck_comment() + "', student_name = '" + cm.getStudent_name() + "', student_num = '" + cm.getStudent_name() + "', class_id = '" + class_id + "' where parent_id = '" + parent_rs.getLong("parent_id") + "';";
							int r = DBConnect.upData(updata_sql);
							if (r != 0){
								return 2;
							}
							else{
								return 0;
							}
							
						}
						else{
							String insert_sql = "insert into uncheck values(null, '" + parent_rs.getLong("parent_id") + "', '" + cm.getUncheck_comment() + "', '" + cm.getStudent_name() + "', '" + cm.getStudent_name() + "', '" + cm.getStudent_name() + "', '" + class_id + "';";
							int r = DBConnect.upData(insert_sql);
							if (r != 0){
								return 1;
							}
							else{
								return 0;
							}
						}
					}
				}
				else{
					return 0;
				}
				
			}
			else{
				return 0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
		
	}

}

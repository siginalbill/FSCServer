package com.servlet.teacher_servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import tools.json.JsonReader;

import com.bean.CheckParent;

import db.connect.DBConnect;

public class Checked extends HttpServlet {

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

		CheckParent checker = null;
		
		JSONObject json = JsonReader.receivePost(request);
		JSONObject returnJson = new JSONObject();
		
		try{
			checker = (CheckParent)JSONObject.toBean(json, CheckParent.class);			
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String parent_sql = "select * from parent where parent_tel = '" + checker.getParent_tel() + "';";
			ResultSet parent_rs = DBConnect.queryData(parent_sql);
			String teacher_sql = "select * from teacher where teacher_num = '" + checker.getUsername() +"' and teacher_pwd = '" + checker.getPassword() + "';";
			ResultSet teacher_rs = DBConnect.queryData(teacher_sql);
			if(parent_rs.next() && teacher_rs.next()){
				//É¾ Ôö ¸Ä
				String updata_sql = "update student set parent_id = '" + parent_rs.getString("parent_id") + "'  where student_num = '" + checker.getStudent_num() + " and class_id = '" + teacher_rs.getLong("class_id") + "';";
				DBConnect.upData(updata_sql);
				String insert_sql = "insert into checked values(null, '" + parent_rs.getLong("parent_id") + "', '" + checker.getComment() + "', '" + teacher_rs.getLong("class_id") + "';";
				DBConnect.upData(insert_sql);
				String delete_sql = "delete * from uncheck where parent_id = '" + parent_rs.getString("parent_id") + "';";
				DBConnect.upData(delete_sql);
				returnJson.put("regist_flag", 1);
				returnJson.put("reason", 0);
			}
			else{
				returnJson.put("regist_flag", 0);
				returnJson.put("reason", 1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(returnJson.toString());
		out.flush();
		out.close();
	}

}

package com.servlet.teacher_servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import tools.json.JsonReader;

import com.bean.LoginUser;

import db.connect.DBConnect;

public class CheckList extends HttpServlet {

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

		LoginUser login_user = null;
		
		JSONObject json = JsonReader.receivePost(request);
		JSONObject returnJson = new JSONObject();
		List<JSONObject> uncheckArray = new ArrayList<JSONObject>();
		List<JSONObject> checkedArray = new ArrayList<JSONObject>();
		
		try{
			login_user = (LoginUser)JSONObject.toBean(json, LoginUser.class);			
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			if (login_user.getFlag() == 2){
				String teacher_sql = "select * from teacher where teacher_num = '" + login_user.getUsername() +"' and teacher_pwd = '" + login_user.getPassword() + "';";
				ResultSet teacher_rs = DBConnect.queryData(teacher_sql);
				if (teacher_rs.next()){
					Long class_id = teacher_rs.getLong("class_id");
					String uncheck_sql = "select * from uncheck where class_id = '" + class_id + "';";
					ResultSet uncheck_rs = DBConnect.queryData(uncheck_sql);
					while(uncheck_rs.next()){
						JSONObject jo = new JSONObject();
						String parent_sql = "select * from parent where parent_id = '" + uncheck_rs.getLong("parent_id") + "';";
						ResultSet parent_rs = DBConnect.queryData(parent_sql);
						String student_sql = "select * from student where student_num = '" + uncheck_rs.getLong("student_num") +"' and class_id = '" + class_id + "';";
						ResultSet student_rs = DBConnect.queryData(student_sql);
						if (parent_rs.next() && student_rs.next()){
							String user_img = DBConnect.getImgUrl("parent_id", parent_rs.getLong("parent_id"));
							jo.put("parent_name", parent_rs.getString("parent_name"));
							jo.put("parent_tel", parent_rs.getString("parent_tel"));
							jo.put("student_name", student_rs.getString("student_name"));
							jo.put("student_num", student_rs.getString("student_num"));
							jo.put("parent_img", user_img);
						}
						uncheckArray.add(jo);						
					}
					String checked_sql = "select * from checked where class_id = '" + class_id + "';";
					ResultSet checked_rs = DBConnect.queryData(checked_sql);
					while(checked_rs.next()){
						JSONObject jo = new JSONObject();
						String parent_sql = "select * from parent where parent_id = '" + checked_rs.getLong("parent_id") + "';";
						ResultSet parent_rs = DBConnect.queryData(parent_sql);
						String student_sql = "select * from student where student_num = '" + checked_rs.getLong("student_num") +"' and class_id = '" + class_id + "';";
						ResultSet student_rs = DBConnect.queryData(student_sql);
						if (parent_rs.next() && student_rs.next()){
							String user_img = DBConnect.getImgUrl("parent_id", parent_rs.getLong("parent_id"));
							jo.put("parent_name", parent_rs.getString("parent_name"));
							jo.put("parent_tel", parent_rs.getString("parent_tel"));
							jo.put("student_name", student_rs.getString("student_name"));
							jo.put("student_num", student_rs.getString("student_num"));
							jo.put("parent_img", user_img);
						}
						checkedArray.add(jo);					
					}
					
					returnJson.put("uncheck", uncheckArray);
					returnJson.put("checked", checkedArray);
					response.setContentType("text/html;charset=utf-8");
					PrintWriter out = response.getWriter();
					out.print(returnJson.toString());
					out.flush();
					out.close();
				}
				else{
					
				}
			}
			else{
				
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}

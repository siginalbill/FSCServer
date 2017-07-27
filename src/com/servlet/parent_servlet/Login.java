package com.servlet.parent_servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.connect.DBConnect;
import tools.json.JsonReader;
import net.sf.json.JSONObject;

import com.bean.LoginUser;

public class Login extends HttpServlet {

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
		JSONObject returnJson = null;
		
		try{
			login_user = (LoginUser)JSONObject.toBean(json, LoginUser.class);			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		returnJson = parentLoginReturn(login_user);
		

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(returnJson.toString());
		out.flush();
		out.close();
	}
	
	
	public JSONObject parentLoginReturn (LoginUser user){
		ResultSet rs = null;
		JSONObject newJson = null;
		if (user.flag == 0){
			
			try{
				String sql = "select * from parent where parent_tel = '" + user.getUsername() +"' and parent_pwd = '" + user.getPassword() + "';";
				rs = DBConnect.queryData(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(rs != null){
				newJson = new JSONObject();
				try{
					if (rs.next()){
						newJson.put("login_flag", 1);
						Long parent_id = (Long)rs.getLong("parent_id");
						newJson.put("user_id", parent_id);
						newJson.put("user_trueName", rs.getString("parent_name"));
						newJson.put("user_pwd", rs.getString("parent_pwd"));
						String user_img = DBConnect.getImgUrl("parent_id", parent_id);
						newJson.put("user_img", user_img);
						String sql_stu = "select * from student where parent_id = '" + parent_id + "';";
						ResultSet rs_stu = DBConnect.queryData(sql_stu);
						if (rs_stu.next()){
							newJson.put("user_status", 1);
							JSONObject stuJson = new JSONObject();
							stuJson.put("student_id", rs_stu.getLong("student_id"));
							stuJson.put("student_name", rs_stu.getString("student_name"));
							newJson.put("student", stuJson);
						}
						else{
							newJson.put("user_status", 0);
							newJson.put("student", null);
						}
					}
					else{
						newJson.put("login_flag", 0);
					}
					
				}catch (Exception e) {
					e.printStackTrace();				
				}
			}
		}
		else if (user.flag == 1){
			//学生登录
			try{
				String sql = "select * from student where student_num = '" + user.getUsername() +"' and student_pwd = '" + user.getPassword() + "';";
				rs = DBConnect.queryData(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(rs != null){
				newJson = new JSONObject();
				try{
					if (rs.next()){
						newJson.put("login_flag", 1);
						newJson.put("user_flag", 1);
						Long student_id = (Long)rs.getLong("student_id");
						newJson.put("user_id", student_id);
						newJson.put("user_trueName", rs.getString("student_name"));
						newJson.put("user_pwd", rs.getString("student_pwd"));
		
					}
					else{
						newJson.put("login_flag", 0);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
		}
		else if (user.flag == 2){
			//老师登录
			try{
				String sql = "select * from teacher where teacher_num = '" + user.getUsername() +"' and teacher_pwd = '" + user.getPassword() + "';";
				rs = DBConnect.queryData(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(rs != null){
				newJson = new JSONObject();
				try{
					if (rs.next()){
						newJson.put("login_flag", 1);
						newJson.put("user_flag", 2);
						Long teacher_id = (Long)rs.getLong("teacher_id");
						newJson.put("user_id", teacher_id);
						newJson.put("user_trueName", rs.getString("teacher_name"));
						newJson.put("user_pwd", rs.getString("teacher_pwd"));
		
					}
					else{
						newJson.put("login_flag", 0);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		else if (user.flag == 3){
			//领导登录
			try{
				String sql = "select * from leader where leader_id = '" + user.getUsername() +"' and leader_pwd = '" + user.getPassword() + "';";
				rs = DBConnect.queryData(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(rs != null){
				newJson = new JSONObject();
				try{
					if (rs.next()){
						newJson.put("login_flag", 1);
						newJson.put("user_flag", 3);
						Long leader_id = (Long)rs.getLong("leader_id");
						newJson.put("user_id", leader_id);
						newJson.put("user_trueName", rs.getString("leader_name"));
						newJson.put("user_pwd", rs.getString("leader_pwd"));
		
					}
					else{
						newJson.put("login_flag", 0);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		return newJson;
	}
}

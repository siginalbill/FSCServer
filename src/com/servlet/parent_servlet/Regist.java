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

import com.bean.ParentRegist;

import db.connect.DBConnect;

public class Regist extends HttpServlet {

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

		ParentRegist regist_parent = null;
		
		JSONObject json = JsonReader.receivePost(request);
		JSONObject returnJson = null;
		
		try{
			regist_parent = (ParentRegist)JSONObject.toBean(json, ParentRegist.class);			
		}catch(Exception e){
			e.printStackTrace();
		}
		if (regist_parent.getFlag() == 0){
			returnJson = parentRegistReturn(regist_parent);
		}

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(returnJson.toString());
		out.flush();
		out.close();
	}
	
	public JSONObject parentRegistReturn (ParentRegist user){
		ResultSet rs = null;
		JSONObject newJson = null;
		if (user.flag == 0){
			
			try{
				String select_sql = "select * from parent where parent_tel = '" + user.getParent_tel() +"';";
				rs = DBConnect.queryData(select_sql);
				newJson = new JSONObject();
				
				if(rs.next()){
					
					newJson.put("regist_flag", 0);
					newJson.put("reason", 1);
					rs.close();
															
				}
				else {
						rs.close();
					
						String insert_sql = "insert into parent(parent_id, parent_tel, parent_name, parent_pwd, parent_img) " + 
						 "values ( null, '" + user.getParent_tel() + "', null, '" + user.getParent_pwd() + "', null" + ");";
						
						if (DBConnect.insertData(insert_sql) == false){
							newJson.put("regist_flag", 1);
							newJson.put("reason", 0);
						}
						else{
							newJson.put("regist_flag", 0);
							newJson.put("reason", 2);
						}
						rs.close();
						
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		else {
			//???
		}
		return newJson;
	}

}

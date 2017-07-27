package com.publicport;

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
import db.connect.DBConnect;

public class News extends HttpServlet {

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

		String news_sql = "select * from news;";
		ResultSet news_rs = null;
		List<JSONObject> newsArray = new ArrayList<JSONObject>();
		JSONObject returnJson = new JSONObject();
		try {
			news_rs = DBConnect.queryData(news_sql);
			while (news_rs.next()){
				JSONObject jo = new JSONObject();
				Long news_id = news_rs.getLong("news_id");
				jo.put("news_id", news_id);
				jo.put("news_title", news_rs.getString("news_title"));
				jo.put("news_date", news_rs.getTimestamp("news_date").toString());
				jo.put("news_content", news_rs.getString("news_content"));
				
				String img = DBConnect.getImgUrl("news_id", news_id);
				jo.put("news_img", img);
				
				//System.out.println(jo.toString());
				
				newsArray.add(jo);
			}
			returnJson.put("news", newsArray);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(returnJson.toString());
		out.flush();
		out.close();
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

		this.doGet(request, response);
	}

}

package db.connect;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.Statement;


public class DBConnect {
	public static String connUrl = null;  
    public static String driverName = null;  
    public static String user = null;  
    public static String password = null;  
    public static Connection conn = null;  
    public static PreparedStatement pst = null;
    private static Statement stm = null;
    private static ResultSet rs = null;
    
    static 
    {
    	InputStream ips = null;
    	try
    	{
    		ips = DBConnect.class.getResourceAsStream("DBConfig.properties");
    		Properties prop=new Properties();
    		prop.load(ips);
    		driverName=prop.getProperty("driverName");
    		connUrl=prop.getProperty("connUrl");
    		user=prop.getProperty("user");
    		password=prop.getProperty("password");
    		ips.close();
    	} catch (Exception e)
    	{
    		e.getStackTrace();
    	}
    }
    
    public static ResultSet queryData(String sql) throws ClassNotFoundException
    {
    	try
    	{
    		conn = getConnection();
   	 		stm = conn.createStatement();
   	 		rs = stm.executeQuery(sql);
   	 		
    	}catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
    	
   		return rs;
    }
    
    public static boolean insertData(String sql) throws ClassNotFoundException
    {
    	boolean flag = true;
    	try
    	{
    		conn = getConnection();
   	 		stm = conn.createStatement();
   	 		flag = stm.execute(sql);
   	 		
    	}catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
    	
   		return flag;
    }
    
    public static int upData(String sql) throws ClassNotFoundException
    {
    	int flag = 0;
    	try
    	{
    		conn = getConnection();
   	 		stm = conn.createStatement();
   	 		flag = stm.executeUpdate(sql);
   	 		
    	}catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
    	
   		return flag;
    }
    
    
 
    	
    public static Connection getConnection() throws ClassNotFoundException, SQLException
    {
        try {  
                Class.forName(driverName);//指定连接类型  
                conn = DriverManager.getConnection(connUrl, user, password);//获取连接  
                return conn;
            } catch (Exception e) {  
            	e.printStackTrace();
            	return null;
            }  
    }
 
    public static void close()
    {
    	if(rs!=null)
    	{
    		try {
				rs.close();
			} catch (SQLException e) {
				rs=null;
			} 
    	}
    	if(stm!=null)
		{
			try {
				stm.close();
			} catch (SQLException e) {
				stm=null;
			}
		}
    	if(conn!=null)
		{
			try {
				conn.close();
			} catch (SQLException e) {
				conn=null;
			}
		}
    }
    
    public static String getImgUrl (String table_pk, Long id){
    	String sql = "select * from image where " + table_pk + " = '" + id +"';";
    	ResultSet rs = null;
    	String result = null;
    	try{
    		rs = DBConnect.queryData(sql);
    		if(rs.next()){
        		result = (String)rs.getString("image_url");
        	}
        	else{
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return result;
    }

}

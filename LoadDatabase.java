package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.json.JSONObject;

public class LoadDatabase implements LoadDbInterface{
	
	private Connection conn;
	public LoadDatabase() 
	{
	    
		
	  		FileReader reader=null;
	  		String host="";
	  		String user="";
	  		String passwd="";
	  		String databaseName="";
	  		String port="";

	  		File file = new File("/Users/alk/eclipse-workspace/LoadDatabase/src/db.properties");
			try {
				
				reader=new FileReader(file);
				
			} catch (FileNotFoundException e1) {
				
				e1.printStackTrace();
			}
			Properties p=new Properties();  
			
			try {
				p.load(reader);
			} catch (IOException e) {
				
				e.printStackTrace();
			}  
			  
   
			host = p.getProperty("host");
			user = p.getProperty("user");
			passwd = p.getProperty("passwd");
			databaseName=p.getProperty("databaseName");
			port=p.getProperty("port");  
	      
		    
	        
	        String url="jdbc:mysql://"+host+":"+port+"/"+databaseName;
	
	        try {
	        	
	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	            conn = DriverManager.getConnection(url,user,passwd);
	            System.out.println("Connected to DB");
	            
	
	        } catch (Exception sqle) {
	            sqle.printStackTrace();
	        }
	}
	//function to load data to database
	public void loadDataToDB() {
		
		 try {
	            File f1 = new File("/Users/alk/Downloads/reviews_Beauty_5_mjson.json");
	            FileReader fr = new FileReader(f1);
	            BufferedReader br = new BufferedReader(fr);
	            String line=null;
	            
	            while ((line = br.readLine()) != null) {
	            	JSONObject ob=new JSONObject(line.toString());
	            	
	            	String insertTableSQL = "INSERT INTO REVIEW "
	    	                + "(reviewerID, asin, reviewerName, reviewText, overall, summary, unixReviewTime, reviewTime) "
	    	                + "VALUES(?,?,?,?,?,?,?,?)";
	            	
	            	try (PreparedStatement preparedStatement = this.conn.prepareStatement(insertTableSQL)) {
	            		
	    	            preparedStatement.setString(1, ob.getString("reviewerID"));
	    	            preparedStatement.setString(2, ob.getString("asin"));
	    	            preparedStatement.setString(3, ob.getString("reviewerName"));
	    	            preparedStatement.setString(4, ob.getString("reviewText"));
	    	            preparedStatement.setDouble(5, ob.getDouble("overall"));
	    	            preparedStatement.setString(6, ob.getString("summary"));
	    	            preparedStatement.setFloat(7, ob.getInt("unixReviewTime"));
	    	            preparedStatement.setString(8, ob.getString("reviewTime"));
	    	            preparedStatement.executeUpdate();
	    	            preparedStatement.close();
	    	            
	    	        } catch (SQLException e) {
	    	            System.out.println("SQL Add Error: " + e.getMessage());
	    	
	    	        } catch (Exception e) {
	    	            System.out.println("Add Error: " + e.getMessage());
	    	        }
	            
	            }
	            fr.close();
	            br.close();
		 }
		 catch(Exception ex) {
			 
	            ex.printStackTrace();
	        }
	        
	        
	
	        
	    }
	//Total number of records
	public int getRecordCount()
	{
		String query="SELECT COUNT(*) AS COUNT FROM REVIEW";
		int count;
		
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				count=rs.getInt("COUNT");
				return count;
			}
			rs.close();
            stmt.close();
		}
		catch (SQLException e) {	
            System.out.println("SQL Query Error: " + e.getMessage());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		return 0;
	}
	
	//Top ten reviewers and their count of reviews
	public void countReviewsByReviewers()
	{
		String query="SELECT reviewerName, count(*) AS count\n" + 
				"   FROM Review_Schema.REVIEW\n" + 
				"   GROUP BY reviewerName\n" + 
				"   ORDER BY count(*) DESC\n" + 
				"   LIMIT 10";
		
		System.out.println("Reviewer Name    Count");
		System.out.println("--------------------------------");

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				System.out.println(rs.getString("reviewerName")+"   "+rs.getInt("count"));
			}
			rs.close();
            stmt.close();
		}
		catch (SQLException e) {
            System.out.println("SQL Query Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	//Top ten products which had reviews, their count of reviews and average rating
	public void countReviewsByProduct()
	{
		String query="SELECT asin, count(*) AS count,avg(overall) AS AVG\n" + 
				"   FROM Review_Schema.REVIEW\n" + 
				"   GROUP BY asin\n" + 
				"   ORDER BY count(*) DESC\n" + 
				"   LIMIT 10";
		
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			
			System.out.println("ProductId    "+"Count    "+"Avg Rating");
			System.out.println("------------------------------------------");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				System.out.println(rs.getString("asin")+"   "+rs.getInt("count")+"    "+rs.getDouble("AVG"));
			}
			rs.close();
            stmt.close();
		}
		catch (SQLException e) {
            System.out.println("SQL Query Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public static void main(String args[])
	{
		LoadDatabase load=new LoadDatabase();
		
		load.loadDataToDB();
		
		System.out.println("Total no of records:"+load.getRecordCount());
		
		load.countReviewsByReviewers();
		
		load.countReviewsByProduct();
	}

}

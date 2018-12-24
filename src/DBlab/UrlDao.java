package DBlab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class UrlDao {
	private String dbURL = "jdbc:mysql://localhost:3306/ExperimentPages";
	private String username = "root";
	private String password = "9184";
	private String sql = "INSERT INTO page (SITE, NO_PAGE, NO_PARENT, PAGE, DEPTH) VALUES (?, ?, ?, ?, ?)";
	public void insertUrl(UrlBean url) {
		try {
			Connection conn = DriverManager.getConnection(dbURL, username, password);
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setString(1, url.getSite());
			statement.setInt(2, url.getNoPage());
			statement.setInt(3, url.getNoParent());
			statement.setString(4, url.getPage());
			statement.setInt(5, url.getDepth());

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new url was inserted successfully!");
			}

			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void insertAllUrls(LinkedHashMap<String, UrlBean> urlList) {
		try {
			Connection conn = DriverManager.getConnection(dbURL, username, password);
			String sql = "INSERT INTO page (SITE, NO_PAGE, NO_PARENT, PAGE, DEPTH) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(sql);
			
			Iterator<UrlBean> iter = urlList.values().iterator();
			while (iter.hasNext()) {
				UrlBean url = iter.next();
							
				
				statement.setString(1, url.getSite());
				statement.setInt(2, url.getNoPage());
				statement.setInt(3, url.getNoParent());
				statement.setString(4, url.getPage());
				statement.setInt(5, url.getDepth());
				
				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("A new url was inserted successfully!");
				}
				
				conn.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}

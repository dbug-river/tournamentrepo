package com.rivertech.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractRepository {

	private final String url;
	private final String username;
	private final String password;

	/***
	 * Abstract repository that contains common functionality for repositories.
	 * 
	 * @param url      Clickhouse URL.
	 * @param username Clickhouse connection username.
	 * @param password Clickhouse connection password.
	 */
	public AbstractRepository(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	/***
	 * Protected method to be used to get a connection instance.
	 * 
	 * @return Clickhouse connection
	 * @throws SQLException
	 */
	protected Connection GetConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(url, username, password);
		return connection;
	}

	/***
	 * Method used to execute an update query against the database.
	 * 
	 * @param query The query to execute.
	 */
	protected void ExecuteUpdate(String query) {
		Connection connection = null;
		try {
			connection = this.GetConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}

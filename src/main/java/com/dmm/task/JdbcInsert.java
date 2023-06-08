package com.dmm.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcInsert {
	
	private static final String URL = "jdbc:mysql://localhost:3306/database01?user=user01&"
			+ "password=password01&useSSL=false&allowPublicKeyRetrieval=true";

	public static void main(String[] args) throws ClassNotFoundException {
		//JDBCでのデータ登録
		// Connection（データベースとの接続を表す）、PreparedStatement（発行するSQLを表す）を、それぞれ生成します。
		try (Connection connection = DriverManager.getConnection(URL);
				PreparedStatement statement = connection
						.prepareStatement("insert into tasks (title, name, text, date, done) values (?, ?, ?, ? ,?)")) {

			// プレースホルダにパラメータをセット
			statement.setString(1, "タイトル");
			statement.setString(2, "玉川世那");
			statement.setString(3, "テキストダミー");
			statement.setString(4, "yyyy-MM-dd");
			statement.setBoolean(5, true);
			
			// SQLの実行
			int count = statement.executeUpdate();
			System.out.println("tasksテーブルに、新しく" + count + "件のデータが挿入されました");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
}

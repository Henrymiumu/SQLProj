import org.h2.Driver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class creatingfata {
    //public static void dataInsert() throws SQLException {
    public static void main(String[] args) throws SQLException {

        Connection connection;
        String username = "test";
        String password = "123";
        String url = "jdbc:h2:./h2/src";
        DriverManager.registerDriver(new org.h2.Driver());
        connection = DriverManager.getConnection(url,username,password);
        Statement statement = connection.createStatement();
        //statement.executeUpdate("create table products (id INT, category VARCHAR(20), price Decimal(12),brand VARCHAR(50), name VARCHAR(70))");
        ResultSet resultSet = statement.executeQuery("select * from products");
        /*
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount=resultSetMetaData.getColumnCount();
        System.out.println(colCount);

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] colNames = new String[colCount];
        for (int i = 1; i < colCount+1; i++){
            tableModel.addColumn(resultSetMetaData.getColumnName(i));
            colNames[i-1] = resultSetMetaData.getColumnName(i);
        }
        JTable table = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(table);

        JFrame jFrame = new JFrame();
        jFrame.setTitle("Test");
        jFrame.setSize(500,600);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);
*/


    }
}

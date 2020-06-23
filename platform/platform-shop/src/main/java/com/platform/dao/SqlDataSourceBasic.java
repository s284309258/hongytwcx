package com.platform.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

public class SqlDataSourceBasic {
    @Autowired
    private static DataSource dataSource;

    private static Connection connection;

//    public SqlDataSourceBasic(){
//        try{
//            connection = dataSource.getConnection();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    public static Connection getConnection() {
        if(connection==null){
            try{
                connection = dataSource.getConnection();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return connection;
    }
}

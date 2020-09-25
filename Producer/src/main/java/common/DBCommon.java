package common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCommon {
    private static final Logger LOG = LogManager.getLogger(DBCommon.class);

    public static boolean close(Connection connection, CallableStatement callableStatement, ResultSet rs){
        try {
            if(connection != null){
                connection.close();
            }
            if(callableStatement != null){
                callableStatement.close();
            }
            if(rs != null){
                rs.close();
            }
        } catch (SQLException throwables) {
            LOG.error("Error close connection: "+throwables.getMessage());
            return false;
        }
        return true;
    }

    public static boolean close(Connection connection, CallableStatement callableStatement){
        try {
            if(connection != null){
                connection.close();
            }
            if(callableStatement != null){
                callableStatement.close();
            }
        } catch (SQLException throwables) {
            LOG.error("Error close connection: "+throwables.getMessage());
            return false;
        }
        return true;
    }
}

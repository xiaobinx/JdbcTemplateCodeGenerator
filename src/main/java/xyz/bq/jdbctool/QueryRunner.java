package xyz.bq.jdbctool;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

public class QueryRunner {
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    public QueryRunner(Config config) {
        this.url = config.getUrl();
        this.username = config.getUsername();
        this.password = config.getPassword();
        this.driverClassName = config.getDriverClassName();
    }

    public QueryRunner(String url, String username, String password, String driverClassName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
    }


    public Connection getConn() throws SQLException, ClassNotFoundException {
        Class.forName(driverClassName);
        return DriverManager.getConnection(url, username, password);
    }

    public <R> R query(String sql, RSHandler<R> handler) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            return handler.handle(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (null != rs) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    if (null != ps) {
                        ps.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (null != conn) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public <R> R conn(ConnHandler<R> handler) {
        Connection conn = null;
        try {
            return handler.handle(getConn());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

package xyz.bq.jdbctool;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

    public Config() {
        try {
            var i = this.getClass().getClassLoader().getResourceAsStream("config.properties");
            load(i);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Config(String path) {
        try {
            load(new FileInputStream(path));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void load(InputStream in) {
        try {
            Properties p = new Properties();
            p.load(in);
            url = p.getProperty("url");
            username = p.getProperty("username");
            password = p.getProperty("password");
            driverClassName = p.getProperty("driverClassName");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Override
    public String toString() {
        return "Config{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                '}';
    }
}

package xyz.bq.jdbctool;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnHandler<R> {
    R handle(Connection rs) throws SQLException;
}

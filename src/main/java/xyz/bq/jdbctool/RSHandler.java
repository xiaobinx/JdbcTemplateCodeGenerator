package xyz.bq.jdbctool;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RSHandler<R> {
    R handle(ResultSet rs) throws SQLException;
}

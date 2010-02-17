package com.tproject;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Data Access Objectのインターフェース。
 * すべてのデータベースへアクセスするクラスはこのインターフェースを継承する。
 * @author no_known
 *
 */
public interface IDao {
	
	public ResultStruct read(String sql);
	public boolean insert(String sql);

}

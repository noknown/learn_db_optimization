package com.tproject;

/**
 * 外部コマンド実行部分のインタフェース
 * @author no_known
 *
 */
public interface Command {
	public AnalyticsInformation execCommand(String user, String pass, String sql);
}

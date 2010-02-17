package com.tproject;

import junit.framework.TestCase;
/**
 * 
 * @author no_known
 *
 */
public class NamingMatchTest extends TestCase {

	/**
	 * Oracleの命名規約にしたがって判断されているかをテストする。
	 */
	public void testStringMatch() {
		assertTrue(NamingMatch.matching("ffredsdf"));
		
		// 非引用識別子の先頭が数字
		assertFalse(NamingMatch.matching("23frsld"));
		// 30文字以下の文字列
		assertTrue(NamingMatch.matching("w12345678901234567890123456789"));
		// 30文字より多い文字列
		assertFalse(NamingMatch.matching("w123456789012345678901234567890"));
		
		// #,$,_を含む文字列
		assertTrue(NamingMatch.matching("sdeef$lslsideef#__sef_"));
		// #,$,_以外の特殊文字を含む非引用識別子
		assertFalse(NamingMatch.matching("ffeop%ooe@fffe d"));
		// #,$,_以外の特殊文字を含む引用識別子
		assertTrue(NamingMatch.matching("\"2User ? !! 3dd%\""));
		
		// 予約語を含む非引用識別子
		assertFalse(NamingMatch.matching("VARCHAR2"));
		// 予約語を含む引用識別子
		assertTrue(NamingMatch.matching("\"VARCHAR2\""));
		// 予約語が小文字の場合
		assertFalse(NamingMatch.matching("varchar2"));
		// 予約語が小文字で引用識別子のとき
		assertTrue(NamingMatch.matching("\"varchar2\""));
		
		// 日本語が入力された場合
		assertTrue(NamingMatch.matching("あいうえお"));
		assertTrue(NamingMatch.matching("社員情報"));
		// 日本語、英語の混合
		assertTrue(NamingMatch.matching("msさしすせそ"));
		
		// nullが渡された場合
		assertFalse(NamingMatch.matching(null));
	}
	
	/**
	 * コンストラクタのテスト
	 */
	@SuppressWarnings("static-access")
	public void testConstractor() {
		NamingMatch nm = new NamingMatch();
		
		assertTrue(nm.matching("ffredsdf"));
	}
	
}

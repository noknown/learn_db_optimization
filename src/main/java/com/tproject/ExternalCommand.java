package com.tproject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 外部コマンド(sqlplus)の実行を請け負うクラス
 * @author no_known
 *
 */
public class ExternalCommand implements Command, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 外部コマンドを実行し、その結果を返す
	 * @param user sqlplusのユーザー名
	 * @param pass パスワード
	 * @param sql 実行するsql文
	 * @return 実行結果 外部コマンドが実行でき、
	 * 適切な値が帰ってくればAnalyticsInformationオブジェクトを返す。それ以外はnull;
	 */
	public AnalyticsInformation execCommand(String user, String pass, String sql) {
		// TODO Auto-generated method stub
		
		String[] command = createCommand(user, pass, sql);
		String[] command2 = new String[]{"perl", "C:\\tkprof.pl", user, pass, sql};
		Runtime rt = Runtime.getRuntime();
		LinkedList<String> stlist = new LinkedList<String>();
		System.err.println(user); System.err.println(pass);
		try {
			Process pr = rt.exec(command);
			InputStream is = pr.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));

			String st;
			while((st = bf.readLine()) != null) {
				stlist.add(st);
				System.err.println(st);
			}
			
			Process pp = rt.exec(command2);
			InputStream ss = pp.getInputStream();
			BufferedReader bb = new BufferedReader(new InputStreamReader(ss));
			
			String sf;
			while((sf = bb.readLine()) != null) {
				stlist.add(sf);
				System.err.println(sf);
			}
		} catch(Exception e) {
			System.err.println("ERROR!");
			e.printStackTrace();
		}
		if(stlist.isEmpty() || stlist.get(0).equals("BUSY")) return null;
		return resulttoAnalysis(stlist);
	}

	/**
	 * コマンド実行のための文字列生成
	 * @param user sqlplusのユーザー名
	 * @param pass パスワード
	 * @param sql 実行するsql文
	 * @return 生成された命令
	 */
	public String[] createCommand(String user, String pass, String sql) {
		// TODO Auto-generated method stub
		
		String[] s = {"perl", "C:\\ora.pl", user, pass, sql};
		
		return s;
	}

	/**
	 * 実行結果をAnalyticsInformationオブジェクトに変換する。
	 * @param result 実行結果文字列のリスト
	 * @return 生成されたAnalyticsInformationオブジェクト。不正な値が渡されていた場合はnull。
	 */
	public AnalyticsInformation resulttoAnalysis(List<String> result) {
		// TODO Auto-generated method stub
		try {
		
			AnalyticsInformation ai = new AnalyticsInformation();
			List<ExecutionPlan> exelist = new ArrayList<ExecutionPlan>();
			int exeindex = 1;
			ai.setStatistics(new Statistics());

			boolean flag = false;
			boolean tkflag = false;
			for(int i = 0; i < result.size(); i++) {
				String str = result.get(i);
				
				//
//				if(str.matches("経過: .*")) {
//					ai.setResponseTime(str.substring(4));
//					continue;
//				}
				//
				if(flag && str.length() > 0 && str.charAt(0) == '|') {
					ExecutionPlan ep = new ExecutionPlan();
					String[] param = str.split("\\|");

					ep.setId(param[1]);
					ep.setOperation(param[2]);
					ep.setName(param[3]);
					ep.setRows(param[4]);
					ep.setBytes(param[5]);
					ep.setCost(param[6]);
					ep.setTime(param[7]);

					exelist.add(ep);
					continue;
				}
				if(str.length() > 0 && str.charAt(0) == '|') flag = true;
				
				//
				String sepa = str.trim();
				if(str.matches("(?i:\\(([\\s　]+|select.*))")) {
					System.err.println("(");
					tkflag = true;
				}
				if(tkflag && sepa.matches("total.*")) {
					System.err.println("total");
					String[] tkprof = sepa.split("[\\s　]+");
					Double d = Double.parseDouble(tkprof[2]);
					
					Formatter format = new Formatter();
					format.format("%02d:%02d:%05.2f", (int)(d/3600.0), (int)(d/60.0%60.0),d%60.0);
					ai.setResponseTime(format.toString());
					
				}
				if(tkflag && sepa.matches(".*time=[0-9]+.*")) {
					System.err.println("time");
					int start = sepa.lastIndexOf('=');
					start++;
					String time = sepa.substring(start, sepa.length()-1);
					exelist.get(exeindex).setTime(exelist.get(exeindex).getTime()+"("+time+")");
					
					exeindex++;
				}
				if(tkflag && sepa.matches("\\*+")) {
					System.err.println("**");
					tkflag = false;
				}
				
				String[] statis = sepa.split("[\\s　][\\s　]+");
				
				if(statis.length <= 1) continue;
				
				if(statis[1].equals("recursive calls")) {
					ai.getStatistics().setRecursiveCalls(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("db block gets")) {
					ai.getStatistics().setDbBlockGets(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("consistent gets")) {
					ai.getStatistics().setConsistentGets(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("physical reads")) {
					ai.getStatistics().setPhysicalReads(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("redo size")) {
					ai.getStatistics().setRedoSize(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("bytes sent via SQL*Net to client")) {
					ai.getStatistics().setBytesSentViaSQL(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("bytes received via SQL*Net from client")) {
					ai.getStatistics().setBytesReceivedViaSQL(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("SQL*Net roundtrips to/from client")) {
					ai.getStatistics().setSQLRoundtrips(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("sorts (memory)")) {
					ai.getStatistics().setSortsMemory(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("sorts (disk)")) {
					ai.getStatistics().setSortsDisk(Integer.parseInt(statis[0]));
				} else if(statis[1].equals("rows processed")) {
					ai.getStatistics().setRowsProcessed(Integer.parseInt(statis[0]));
				}
			}
			
			for(ExecutionPlan ep : exelist) {
				ai.addExecutionPlan(ep);
			}
			
			return ai;
		
		} catch(Exception e) {
			System.err.println("External Command Error");
			e.printStackTrace();
			return null;
		}
	}

}

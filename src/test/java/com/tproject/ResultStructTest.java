package com.tproject;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
/**
 * 
 * @author no_known
 *
 */
public class ResultStructTest extends TestCase {

	ResultStruct rs;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		rs = new ResultStruct();
	}
	
	/**
	 * setで与えられた値が、getできちんと得られるかどうかのテスト
	 */
	public void testResultStruct() {
		
		List<ResultData> result = Arrays.asList(new ResultData[]{
				new ResultData(new String[]{"100", "str", "90:00"}),
				new ResultData(new String[]{"200", "ssd", "80:00"}),
				new ResultData(new String[]{"300", "rdd", "70:00"})
		});
		ResultData schema = new ResultData(new String[]{"head1", "head2", "head3"});
		
		rs.setSchema(schema);
		rs.setResult(result);
		
		ResultData rd = rs.getSchema();
		List<ResultData> rda = rs.getResult();
		
		for(int i = 0; i < rd.getDataList().size(); i++) {
			assertEquals(rd.getDataList().get(i), schema.getDataList().get(i));
		}
		
		for(int i = 0; i < rda.size(); i++) {
			ResultData rr = rda.get(i);
			for(int j = 0; j < rr.getDataList().size(); j++) {
				assertEquals(rr.getDataList().get(j), result.get(i).getDataList().get(j));
			}
		}
		
	}
	
	public void testEmptyData() {
		List<ResultData> result = Arrays.asList(new ResultData[]{
				new ResultData(new String[]{})
		});
		ResultData schema = new ResultData(new String[]{});

		rs.setResult(result);
		rs.setSchema(schema);
		
		assertTrue(rs.isEmpty());
		
		result = Arrays.asList(new ResultData[]{
				new ResultData(new String[]{"100", "str", "90:00"}),
				new ResultData(new String[]{"200", "ssd", "80:00"}),
				new ResultData(new String[]{"300", "rdd", "70:00"})
		});
		
		rs.setResult(result);
		
		assertFalse(rs.isEmpty());

	}
}
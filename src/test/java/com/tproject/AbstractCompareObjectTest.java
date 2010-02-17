package com.tproject;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class AbstractCompareObjectTest extends TestCase {
	
	AbstractCompareObject aco;
	List<MockIdentity> oldList, newList;
	
	public static class MockIdentity implements Identity{
		private static long number = 0;
		private String id, name;
		public MockIdentity(String name) {
			// TODO Auto-generated constructor stub
			this.id = "number" + number;
			this.name = name;
			number++;
		}
		public String getId() {
			return this.id;
		}
		public String getName() {
			return this.name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Object clone() {
			MockIdentity mi = new MockIdentity(this.name);
			mi.id = this.id;
			return mi;
		}
	}
	
	@Override
	protected void setUp()  {
		// TODO Auto-generated method stub
		aco = new AbstractCompareObject() {

			@Override
			public List<AbstractDifferenceObject> compare(
					List<? extends Identity> oldList,
					List<? extends Identity> newList) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setIdentityInformation(AbstractDifferenceObject object,
					Identity id) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public AbstractDifferenceObject generateDifferenceInformation(
					Identity id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AbstractDifferenceObject generateSubtraction(Identity oldid,
					Identity newid) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AbstractDifferenceObject createDifferenceObject() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		oldList = new ArrayList<MockIdentity>();
		oldList.add(new MockIdentity("table1"));
		oldList.add(new MockIdentity("table2"));
		oldList.add(new MockIdentity("table3"));
		
		newList = new ArrayList<MockIdentity>();
		newList.add((MockIdentity)oldList.get(0).clone());
		newList.add((MockIdentity)oldList.get(1).clone());
		newList.add((MockIdentity)oldList.get(2).clone());
		
	}

	/**
	 * Identity Id用のcontainsのテスト
	 */
	public void testContains() {
		MockIdentity mden = newList.get(1);
		
		assertTrue(aco.containsObject(newList, mden));
		
		mden = null;
		mden = new MockIdentity("geogeo");
		
		assertFalse(aco.containsObject(newList, mden));
	}
	
}

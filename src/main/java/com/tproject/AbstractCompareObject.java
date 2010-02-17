package com.tproject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCompareObject {

	/**
	 * カラム情報のリストから与えられたカラム情報のIDと
	 * 同一のものがあればtrue, それ以外はfalseを返す。
	 * @param newlist カラム情報のリスト
	 * @param identity 存在を確認するカラム情報
	 * @return mdenがnewList中に存在すればtrue,存在しなければfalse
	 */
	public boolean containsObject(List<? extends Identity> newlist, Identity identity) {
		// TODO Auto-generated method stub
		
		boolean bool = false;
		for(Identity mden2 : newlist) {
			if(identity.getId().equals(mden2.getId())) {
				bool = true;
				break;
			}
		}
		return bool;
	}
	
	public Identity getContainsObject(List<? extends Identity> newlist, Identity identity) {
		Identity ident = null;
		for(Identity mden2 : newlist) {
			if(identity.getId().equals(mden2.getId())) {
				ident = mden2;
				break;
			}
		}
		return ident;
	}
	
	/**
	 * 一意情報を持つIdentityのリストを２つ受け取り、リスト間の違いを
	 * AbstractDifferenceObjectオブジェクトのリストとして返す。
	 * @param oldlist 元のカラム情報のリスト
	 * @param newlist 更新されたカラム情報のリスト
	 * @return 変更箇所を表す情報のリスト
	 */
	public List<? extends AbstractDifferenceObject> compare(List<? extends Identity> oldlist,
															List<? extends Identity> newlist) {
		
		List<AbstractDifferenceObject> difcolumns = new ArrayList<AbstractDifferenceObject>();
		
		List<Identity> ollist = new ArrayList<Identity>();
		List<Identity> nelist = new ArrayList<Identity>();
		for(int i = 0; i < oldlist.size(); i++) {
			Identity tcm = oldlist.get(i);
			ollist.add((Identity) tcm.clone());
		}
		for(int i = 0; i < newlist.size(); i++) {
			Identity tcm = newlist.get(i);
			nelist.add((Identity) tcm.clone());
		}
				
		// add
		List<Identity> deletelist = new ArrayList<Identity>();
		deletelist.clear();
		for(Identity tcm : nelist) {
			if(!containsObject(ollist, tcm)) {
				difcolumns.add(generateDifferenceInformation(tcm));
				//
				deletelist.add(tcm);
			}
		}
		for(Identity tcm : deletelist) nelist.remove(tcm);
		
		// delete
		Collections.reverse(ollist);
		for(Identity tcm : ollist) {
			if(!containsObject(nelist, tcm)) {
				AbstractDifferenceObject dc = createDifferenceObject();
				dc.setDifferentChar('D');
//				dc.setObjectId(tcm.getId());
				setIdentityInformation(dc, tcm);
				
				
				difcolumns.add(dc);

				//
				deletelist.add(tcm);
			}
		}
		for(Identity tcm : deletelist) ollist.remove(tcm);
		Collections.reverse(ollist);
		
		// update
		assert ollist.size() == nelist.size();
//		for(int i = 0; i < ollist.size(); i++) {
//			if(ollist.get(i).getId().equals(nelist.get(i).getId())) {
//				AbstractDifferenceObject dc = generateSubtraction(ollist.get(i), nelist.get(i));
//				
//				if(dc != null) difcolumns.add(dc);
//			}
//		}
		for(Identity tcm : ollist) {
			Identity temp;
			if(null != (temp = getContainsObject(nelist, tcm))) {
				AbstractDifferenceObject dc = generateSubtraction(tcm, temp);
				
				if(dc != null) difcolumns.add(dc);
			}
		}
		
		return difcolumns;
	}
	
	/**
	 * 削除するべきデータをAbstractDifferenceObjectに格納するためのメソッド
	 * @param object 削除するデータを保存するAbstractDifferenceObject
	 * @param id 削除されるidentityオブジェクト
	 */
	public abstract void setIdentityInformation(AbstractDifferenceObject object, Identity id);
	
	
	/**
	 * Identityオブジェクトに対応するAbstractDifferenceObjectを生成する。
	 * @param id 変換するIdentity情報
	 * @return 変更された変更箇所情報
	 */
	public abstract AbstractDifferenceObject generateDifferenceInformation(Identity id);
	
	
	/**
	 * 二つのIdentityオブジェクトを比較して違う場合は違いを表す
	 * AbstractDifferenceObjectを返す
	 * @param oldid 元のIdentity情報
	 * @param newid 更新されたIdentity情報
	 * @return 変更箇所を表す情報
	 */
	public abstract AbstractDifferenceObject generateSubtraction(Identity oldid, Identity newid);
	
	/**
	 * 対応する比較オブジェクトを生成する。
	 * @return 対応する比較オブジェクト
	 */
	public abstract AbstractDifferenceObject createDifferenceObject();
}

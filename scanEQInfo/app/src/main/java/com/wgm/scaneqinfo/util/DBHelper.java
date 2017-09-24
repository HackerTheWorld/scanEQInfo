package com.wgm.scaneqinfo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper{
	private static String dbname = "asso.db";
	private static int version = 1;
	public DBHelper(Context context){
		super(context,dbname,null,version);
	}
	public DBHelper(Context context,String dbname,CursorFactory factory,int version){
		super(context,dbname,factory,version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//������Ĵ���
		db.execSQL("create table if not exists product(productCode varchar(20) primary key,productname varchar(50),wholeSalePrice float,relailPrice float)");
		db.execSQL("create table if not exists customer(cno varchar(15) primary key,customername varchar(40),addr varchar(100)," +
				"ceo varchar(12),tel varchar(20))");
		db.execSQL("create table if not exists loginedInfo(mobile varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		//���ݿ���������
		db.execSQL("drop table if not exists product");
		db.execSQL("drop table if not exists customer");
		db.execSQL("drop table if not exists loginedInfo");
		onCreate(db);
	}
	
}

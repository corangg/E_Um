package com.example.appointment.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AddressDBHelper(context: Context,dbName:String?) : SQLiteOpenHelper(context, dbName,null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table ADDRESS_TB("+"_id integer primary key autoincrement,"+"name not null,"+"address not null)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun deleteRow(rowNumber: Long): Boolean {
        val db = writableDatabase

        val result = db.delete("ADDRESS_TB", "_id IN (SELECT _id FROM ADDRESS_TB ORDER BY _id LIMIT 1 OFFSET ?)", arrayOf(rowNumber.toString()))

        db.close()
        return result != -1
    }


}

package com.example.climbingdiary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.climbingdiary.MainActivity;
import com.example.climbingdiary.interfaces.State;
import com.example.climbingdiary.models.Route;

public class TaskRepository {

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    public TaskRepository ()
    {
        this.mContext = MainActivity.getAppContext();
        mDbHelper = new DatabaseHelper(mContext);
    }

    public TaskRepository open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getWritableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public Cursor getAllRoutes(String _order,String _filter)
    {
        String order_value =  "r.level";
        String filter = "";
        if(_order != null && !_order.isEmpty()){
            order_value = "r."+_order;
        }
        if(_filter != null && !_filter.isEmpty()){
            filter = "and "+_filter;
        }
        try
        {
            String sql ="SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor "+filter+" group by r.id Order By "+order_value+" DESC";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getAllRoutes >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor getRoute(int _id){
        try
        {
            String sql ="SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor AND r.id="+_id;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            throw mSQLException;
        }
    }
    public Cursor getAllAreas(){
        try
        {
            String sql ="SELECT * FROM gebiete GROUP BY id";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            throw mSQLException;
        }
    }
    public Cursor getSectorByAreaName(String _name){
        try
        {
            String sql ="SELECT s.name,s.koordinaten as koordinaten_sektor,a.koordinaten as koordinaten_area,s.gebiet,s.id FROM sektoren s, gebiete a where a.name Like '"+_name+"%' and s.gebiet=a.id GROUP BY s.id";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getSectorByAreaName >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor getLineChartValues(){
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SUM(cast(r.level as int)*(25*(INSTR('abc',substr(replace(r.level,'+',''),-1)))")
                    .append(" +INSTR('+',substr(r.level,-1))")
                    .append(" +INSTR('rpflashos',r.stil))) as stat,")
                    .append(" COUNT(cast(r.level as int)) as anzahl,")
                    .append(" strftime('%Y',r.date) as date")
                    .append(" FROM routen r")
                    .append(" GROUP BY strftime('%Y',r.date)");
            Log.d("query LineChartValues",sql.toString());
            Cursor mCur = mDb.rawQuery(sql.toString(),null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;

        }catch(SQLException mSQLExeption){
            Log.e(TAG, "getBarChartValues >>"+ mSQLExeption.toString());
            throw mSQLExeption;
        }
    }
    public Cursor getBarChartValues(){
        try{
            String sql = "select r.level,sum(r.stil='RP') as rp, sum(r.stil='OS') as os,sum(r.stil='FLASH') as flash from routen r group by r.level";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;


        }catch(SQLException mSQLExeption)
        {
            Log.e(TAG, "getBarChartValues >>"+ mSQLExeption.toString());
            throw mSQLExeption;
        }
    }
    public void inserRoute(Route route){
        //create the transaction
        String [] tasks = {
            "INSERT OR IGNORE INTO gebiete (name) VALUES ('"+route.getArea()+"')",
            "INSERT OR IGNORE INTO sektoren (name,gebiet) "+
            "SELECT '"+route.getArea()+"',id FROM gebiete WHERE name='"+route.getArea()+"'",
            "INSERT OR IGNORE INTO routen (date,name,level,stil,rating,kommentar,gebiet,sektor) "+
            "SELECT '"+route.getDate()+"','"+route.getName()+"','"+route.getLevel()+"','"+route.getStyle()+"','"+route.getRating()+"','"+route.getComment()+"',a.id,s.id " +
            "FROM gebiete a, sektoren s " +
            "WHERE a.name = '"+route.getArea()+"'" +
            "AND s.name='"+route.getSector()+"'"
        };
        mDb.beginTransaction();
       try {
           for (String x : tasks) {
               Log.d("insert",x);
               mDb.execSQL(x);
           }
           mDb.setTransactionSuccessful();
       }finally {
           mDb.endTransaction();
       }
    }
    public boolean deleteRoute(int id){
        final String sql = "DELETE FROM routen WHERE id="+id;
        State state = new State() {
            @Override
            public boolean getState() {
                try{
                    mDb.execSQL(sql);
                    return true;
                }catch(SQLiteException exception){
                    return false;
                }
            }
        };
        return state.getState();
    }
}

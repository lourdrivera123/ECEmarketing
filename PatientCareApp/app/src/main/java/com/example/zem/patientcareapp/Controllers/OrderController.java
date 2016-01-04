package com.example.zem.patientcareapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 11/23/2015.
 */
public class OrderController extends DbHelper {

    DbHelper dbhelper;
    SQLiteDatabase sql_db;

     //ORDERS TABLE
    public static final String TBL_ORDERS = "orders",
            SERVER_ORDERS_ID = "orders_id",
            ORDERS_RECIPIENT_NAME = "recipient_name",
            ORDERS_RECIPIENT_ADDRESS = "recipient_address",
            ORDERS_RECIPIENT_NUMBER = "recipient_contactNumber",
            ORDERS_DELIVERY_SCHED = "delivery_sched",
            ORDERS_ECE_BRANCH = "branch_id",
            ORDERS_MODE_OF_DELIVERY = "modeOfDelivery",
            ORDERS_STATUS = "status",
            ORDERS_CREATED_AT = "created_at";

            public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT," +
                        " %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_ORDERS, AI_ID, SERVER_ORDERS_ID, PATIENT_ID, ORDERS_RECIPIENT_NAME, ORDERS_RECIPIENT_ADDRESS,
                ORDERS_RECIPIENT_NUMBER, ORDERS_DELIVERY_SCHED, ORDERS_ECE_BRANCH, ORDERS_MODE_OF_DELIVERY, ORDERS_STATUS, CREATED_AT,
                UPDATED_AT, DELETED_AT);

    public OrderController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        sql_db = dbhelper.getWritableDatabase();
    }

    public boolean saveOrders(JSONObject object) {
        long rowID = 0;
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(SERVER_ORDERS_ID, object.getInt("id"));
            values.put(PATIENT_ID, object.getInt(PATIENT_ID));
            values.put(ORDERS_RECIPIENT_NAME, object.getString(ORDERS_RECIPIENT_NAME));
            values.put(ORDERS_RECIPIENT_ADDRESS, object.getString(ORDERS_RECIPIENT_ADDRESS));
            values.put(ORDERS_RECIPIENT_NUMBER, object.getString(ORDERS_RECIPIENT_NUMBER));
            values.put(ORDERS_DELIVERY_SCHED, object.getString(ORDERS_DELIVERY_SCHED));
            values.put(ORDERS_ECE_BRANCH, object.getInt(ORDERS_ECE_BRANCH));
            values.put(ORDERS_MODE_OF_DELIVERY, object.getString(ORDERS_MODE_OF_DELIVERY));
            values.put(ORDERS_STATUS, object.getString(ORDERS_STATUS));
            values.put(CREATED_AT, object.getString(CREATED_AT));
            values.put(UPDATED_AT, object.getString(UPDATED_AT));
            values.put(DELETED_AT, object.getString(DELETED_AT));

            rowID = sql_db.insert(TBL_ORDERS, null, values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sql_db.close();
        return rowID > 0;
    }

    public ArrayList<HashMap<String, String>> getAllOrderItems() {
        ArrayList<HashMap<String, String>> order_items = new ArrayList<>();

        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
        String sql = "SELECT count(od.order_details_id) as num_of_items, o.recipient_name, b.total, o.created_at as date_ordered, o.orders_id as order_id, o.status from orders as o inner join billings as b on o.orders_id = b.order_id inner join order_details as od on b.order_id = od.order_id where o.patient_id = " + SidebarActivity.getUserID() + " order by o.created_at DESC";
        Cursor cur = sql_db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("recipient_name", cur.getString(cur.getColumnIndex(ORDERS_RECIPIENT_NAME)));
            map.put("num_of_items", cur.getString(cur.getColumnIndex("num_of_items")));
            map.put("total", cur.getString(cur.getColumnIndex(BillingController.BILLINGS_TOTAL)));
            map.put("date_ordered", cur.getString(cur.getColumnIndex("date_ordered")));
            map.put("order_id", cur.getString(cur.getColumnIndex("order_id")));
            map.put("order_status", cur.getString(cur.getColumnIndex("status")));
            order_items.add(map);
        }

        cur.close();
        sql_db.close();
        return order_items;
    }


}
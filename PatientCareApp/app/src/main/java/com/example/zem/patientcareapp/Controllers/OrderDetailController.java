package com.example.zem.patientcareapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 11/23/2015.
 */
public class OrderDetailController extends DbHelper {

    DbHelper dbhelper;
    SQLiteDatabase sql_db;

    //ORDERS TABLE
    public static final String TBL_ORDER_DETAILS = "order_details",
            SERVER_ORDER_DETAILS_ID = "order_details_id",
            ORDER_DETAILS_ORDER_ID = "order_id",
            ORDER_DETAILS_PRODUCT_ID = "product_id",
            ORDER_DETAILS_PRESCRIPTION_ID = "prescription_id",
            ORDER_DETAILS_QUANTITY = "quantity",
            ORDER_DETAILS_TYPE = "type",
            ORDER_DETAILS_QTY_FULFILLED = "qty_fulfilled",
            ORDER_DETAILS_PRICE = "price";

            public static final String CREATE_TABLE = String.format("CREATE TABLE %s" +
                        " (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s DOUBLE, %s TEXT, %s TEXT, %s TEXT)",
                TBL_ORDER_DETAILS, AI_ID, SERVER_ORDER_DETAILS_ID, ORDER_DETAILS_ORDER_ID, ORDER_DETAILS_PRODUCT_ID, ORDER_DETAILS_PRESCRIPTION_ID, ORDER_DETAILS_QUANTITY, ORDER_DETAILS_TYPE, ORDER_DETAILS_QTY_FULFILLED, ORDER_DETAILS_PRICE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public OrderDetailController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        sql_db = dbhelper.getWritableDatabase();
    }

    public boolean saveOrderDetails(JSONObject object) {
        long rowID = 0;
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.d("json_objects", object+"");
        try {
            values.put(SERVER_ORDER_DETAILS_ID, object.getInt("id"));
            values.put(ORDER_DETAILS_ORDER_ID, object.getInt(ORDER_DETAILS_ORDER_ID));
            values.put(ORDER_DETAILS_PRODUCT_ID, object.getInt(ORDER_DETAILS_PRODUCT_ID));
            values.put(ORDER_DETAILS_PRESCRIPTION_ID, object.getInt(ORDER_DETAILS_PRESCRIPTION_ID));
            values.put(ORDER_DETAILS_QUANTITY, object.getInt(ORDER_DETAILS_QUANTITY));
            values.put(ORDER_DETAILS_TYPE, object.getString(ORDER_DETAILS_TYPE));
            values.put(ORDER_DETAILS_QTY_FULFILLED, object.getInt(ORDER_DETAILS_QTY_FULFILLED));
            values.put(ORDER_DETAILS_PRICE, object.getDouble(ORDER_DETAILS_PRICE));
            values.put(CREATED_AT, object.getString(CREATED_AT));
            values.put(UPDATED_AT, object.getString(UPDATED_AT));
            values.put(DELETED_AT, object.getString(DELETED_AT));

            rowID = sql_db.insert(TBL_ORDER_DETAILS, null, values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sql_db.close();
        return rowID > 0;
    }

    public ArrayList<HashMap<String, String>> getOrderDetailsFromOrder(int order_id) {
        ArrayList<HashMap<String, String>> order_details = new ArrayList<>();
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
        String sql = "SELECT od.order_details_id, p.name as product_name, od.price, od.quantity, o.created_at as ordered_on, o.status,  p.packing, p.qty_per_packing, p.unit from order_details as od inner join orders as o on od.order_id = o.orders_id inner join products as p on od.product_id = p.product_id inner join branches as br on o.branch_id = br.branches_id where od.order_id = " + order_id + " order by od.created_at DESC";
        Cursor cur = sql_db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            double item_subtotal = cur.getInt(cur.getColumnIndex(ORDER_DETAILS_QUANTITY)) * cur.getDouble(cur.getColumnIndex(ORDER_DETAILS_PRICE));
            HashMap<String, String> map = new HashMap<>();
            map.put(SERVER_ORDER_DETAILS_ID, cur.getString(cur.getColumnIndex(SERVER_ORDER_DETAILS_ID)));
            map.put(ProductController.PRODUCT_NAME, cur.getString(cur.getColumnIndex("product_name")));
            map.put(ORDER_DETAILS_PRICE, cur.getString(cur.getColumnIndex(ORDER_DETAILS_PRICE)));
            map.put(ORDER_DETAILS_QUANTITY, cur.getString(cur.getColumnIndex("quantity")));
            map.put(OrderController.ORDERS_CREATED_AT, cur.getString(cur.getColumnIndex("ordered_on")));
            map.put(OrderController.ORDERS_STATUS, cur.getString(cur.getColumnIndex("status")));
            map.put(ProductController.PRODUCT_PACKING, cur.getString(cur.getColumnIndex(ProductController.PRODUCT_PACKING)));
            map.put(ProductController.PRODUCT_QTY_PER_PACKING, cur.getString(cur.getColumnIndex(ProductController.PRODUCT_QTY_PER_PACKING)));
            map.put(ProductController.PRODUCT_UNIT, cur.getString(cur.getColumnIndex(ProductController.PRODUCT_UNIT)));
            map.put("item_subtotal", String.valueOf(item_subtotal));
            order_details.add(map);
        }

        cur.close();
        sql_db.close();
        return order_details;
    }
}

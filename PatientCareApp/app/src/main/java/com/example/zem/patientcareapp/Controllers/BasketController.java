package com.example.zem.patientcareapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.zem.patientcareapp.Model.Basket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Zem on 11/23/2015.
 */
public class BasketController extends DbHelper {

    DbHelper dbhelper;
    SQLiteDatabase sql_db;
    //    Context c;
    PatientController patient_controller;

    // BASKET TABLE
    public static final String TBL_BASKETS = "baskets",
            SERVER_BASKET_ID = "basket_id",
            BASKET_PRODUCT_ID = "product_id",
            BASKET_QUANTITY = "quantity",
            BASKET_PRESCRIPTION_ID = "prescription_id",
            BASKET_IS_APPROVED = "is_approved";

    // SQL to create table "baskets"
    public static final String CREATE_TABLE = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s INTEGER, %s INTEGER, %s DOUBLE, %s INTEGER, %s INTEGER, %s  TEXT , %s  TEXT , %s  TEXT  )",
            TBL_BASKETS, AI_ID, SERVER_BASKET_ID, PATIENT_ID, BASKET_PRODUCT_ID, BASKET_QUANTITY, BASKET_PRESCRIPTION_ID, BASKET_IS_APPROVED, CREATED_AT, UPDATED_AT, DELETED_AT);


    public BasketController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        sql_db = dbhelper.getWritableDatabase();
        patient_controller = new PatientController(context);
    }

    /////////////////////////////INSERT METHODS///////////////////////////////////////
    public boolean insertBasket(Basket basket) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String datenow = dateFormat.format(date);

        int patient_id = patient_controller.getCurrentLoggedInPatient().getServerID();
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SERVER_BASKET_ID, basket.getBasketId());
        values.put(PATIENT_ID, patient_id);
        values.put(BASKET_PRODUCT_ID, basket.getProductId());
        values.put(BASKET_QUANTITY, basket.getQuantity());
        values.put(BASKET_PRESCRIPTION_ID, basket.getPrescriptionId());
        values.put(BASKET_IS_APPROVED, basket.getIsApproved());
        values.put(CREATED_AT, datenow);

        long row = sql_db.insert(TBL_BASKETS, null, values);
        sql_db.close();
        return row > 0;
    }

    public Basket getBasket(int productId) {
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();

        Basket basket = new Basket();
        String sql = "Select * from " + TBL_BASKETS + " where product_id=" + productId + " and patient_id=" + patient_controller.getCurrentLoggedInPatient().getServerID();
        System.out.println("\ngetBasket: " + sql);
        Cursor cur = sql_db.rawQuery(sql, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            basket.setId(cur.getInt(0));
            basket.setBasketId(cur.getInt(cur.getColumnIndex(SERVER_BASKET_ID)));
            basket.setPatienId(cur.getInt(cur.getColumnIndex(PATIENT_ID)));
            basket.setProductId(cur.getInt(cur.getColumnIndex(BASKET_PRODUCT_ID)));
            basket.setQuantity(cur.getInt(cur.getColumnIndex(BASKET_QUANTITY)));
            basket.setCreatedAt(cur.getString(cur.getColumnIndex(CREATED_AT)));
            basket.setUpdatedAt(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            cur.moveToNext();
        }
        cur.close();
        sql_db.close();
        return basket;
    }

    public ArrayList<HashMap<String, String>> convertFromJson(Context context, JSONArray json_array) {
        ArrayList<HashMap<String, String>> basketItems = new ArrayList();

        try {
            for (int x = 0; x < json_array.length(); x++) {
                JSONObject obj = json_array.getJSONObject(x);

                HashMap<String, String> map = new HashMap();
                map.put(ProductController.SERVER_PRODUCT_ID, String.valueOf(obj.getInt("id")));
                map.put(SERVER_BASKET_ID, String.valueOf(obj.getInt("basket_id")));
                map.put(ProductController.PRODUCT_NAME, obj.getString("name"));
                map.put(ProductController.PRODUCT_PRICE, String.valueOf(obj.getDouble("price")));
                map.put(BASKET_QUANTITY, String.valueOf(obj.getInt("quantity")));
                map.put(ProductController.PRODUCT_UNIT, obj.getString("unit"));
                map.put(ProductController.PRODUCT_SKU, obj.getString("sku"));
                map.put(ProductController.PRODUCT_PACKING, obj.getString("packing"));
                map.put(ProductController.PRODUCT_QTY_PER_PACKING, String.valueOf(obj.getInt("qty_per_packing")));
                map.put(ProductController.PRODUCT_PRESCRIPTION_REQUIRED, String.valueOf(obj.getInt("prescription_required")));
                map.put(BASKET_PRESCRIPTION_ID, String.valueOf(obj.getInt("prescription_id")));
                map.put(BASKET_IS_APPROVED, String.valueOf(obj.getInt("is_approved")));

                basketItems.add(map);
            }
        } catch (Exception e) {
            Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
        }

        return basketItems;
    }

    public boolean saveBasket(HashMap<String, String> map) {
        int patient_id = patient_controller.getCurrentLoggedInPatient().getServerID();
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
        ContentValues val = new ContentValues();
        long row;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String datenow = dateFormat.format(date);

        if (map.get("action").equals("insert")) {
            val.put(SERVER_BASKET_ID, map.get("server_id"));
            val.put(PATIENT_ID, patient_id);
            val.put(BASKET_PRODUCT_ID, map.get("product_id"));
            val.put(BASKET_QUANTITY, map.get("quantity"));
            val.put(BASKET_PRESCRIPTION_ID, map.get("prescription_id"));
            val.put(BASKET_IS_APPROVED, map.get("is_approved"));
            val.put(CREATED_AT, datenow);

            row = sql_db.insert(TBL_BASKETS, null, val);
        } else {
            val.put(BASKET_QUANTITY, map.get("quantity"));
            val.put(UPDATED_AT, datenow);

            row = sql_db.update(TBL_BASKETS, val, SERVER_BASKET_ID + "=" + map.get("id"), null);
        }

        Log.d("row", row + "");

        sql_db.close();
        return row > 0;
    }

    public boolean emptyBasket(int patient_id) {
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
        long row = sql_db.delete(TBL_BASKETS, PATIENT_ID + "=" + patient_id, null);
        sql_db.close();
        return row > 0;

    }
}

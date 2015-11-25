package com.example.zem.patientcareapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zem.patientcareapp.Model.Basket;

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

//        SQLiteDatabase db = getWritableDatabase();
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
        Basket basket = new Basket();

        String sql = "Select * from " + TBL_BASKETS + " where product_id=" + productId + " and patient_id=" + patient_controller.getCurrentLoggedInPatient().getServerID();
        System.out.println("\ngetBasket: " + sql);
//        SQLiteDatabase db = getWritableDatabase();
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

    public ArrayList<HashMap<String, String>> getAllBasketItems(boolean onlyApprovedItems) {
        ArrayList<HashMap<String, String>> items = new ArrayList<>();

        String additionalWhere, sql;
        additionalWhere = "";
        double total = 0;

        if (onlyApprovedItems) {
            additionalWhere = " and b.is_approved=1";
        }

        sql = "Select b.id, b.basket_id, b.is_approved, b.prescription_id, p.product_id, p.name, p.price, p.packing, p.qty_per_packing," +
                " p.prescription_required, p.sku, b.quantity, p.unit from " + TBL_BASKETS + " as b " +
                "inner join " + ProductController.TBL_PRODUCTS + " as p on p.product_id = b.product_id where " +
                "b.patient_id=" + patient_controller.getCurrentLoggedInPatient().getServerID() + additionalWhere;

        System.out.println("basket sql: " + sql);

        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            double item_subtotal = cur.getInt(cur.getColumnIndex(BASKET_QUANTITY)) * cur.getDouble(cur.getColumnIndex(ProductController.PRODUCT_PRICE));
            HashMap<String, String> map = new HashMap<>();
            map.put(AI_ID, cur.getString(cur.getColumnIndex(AI_ID)));
            map.put(ProductController.SERVER_PRODUCT_ID, cur.getString(cur.getColumnIndex(ProductController.SERVER_PRODUCT_ID)));
            map.put(SERVER_BASKET_ID, cur.getString(cur.getColumnIndex(SERVER_BASKET_ID)));
            map.put(ProductController.PRODUCT_NAME, cur.getString(cur.getColumnIndex(ProductController.PRODUCT_NAME)));
            map.put(ProductController.PRODUCT_PRICE, String.valueOf(cur.getDouble(cur.getColumnIndex(ProductController.PRODUCT_PRICE))));
            map.put(BASKET_QUANTITY, String.valueOf(cur.getInt(cur.getColumnIndex(BASKET_QUANTITY))));
            map.put(ProductController.PRODUCT_UNIT, cur.getString(cur.getColumnIndex(ProductController.PRODUCT_UNIT)));
            map.put(ProductController.PRODUCT_SKU, cur.getString(cur.getColumnIndex(ProductController.PRODUCT_SKU)));
            map.put(ProductController.PRODUCT_PACKING, cur.getString(cur.getColumnIndex(ProductController.PRODUCT_PACKING)));
            map.put(ProductController.PRODUCT_QTY_PER_PACKING, cur.getString(cur.getColumnIndex(ProductController.PRODUCT_QTY_PER_PACKING)));
            map.put(ProductController.PRODUCT_PRESCRIPTION_REQUIRED, cur.getString(cur.getColumnIndex(ProductController.PRODUCT_PRESCRIPTION_REQUIRED)));
            map.put(BASKET_PRESCRIPTION_ID, cur.getString(cur.getColumnIndex(BASKET_PRESCRIPTION_ID)));
            map.put(BASKET_IS_APPROVED, cur.getString(cur.getColumnIndex(BASKET_IS_APPROVED)));
            map.put("item_subtotal", String.valueOf(item_subtotal));
            items.add(map);
            cur.moveToNext();
        }

        cur.close();
        db.close();
        return items;
    }

    public boolean updateBasket(Basket basket) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String datenow = dateFormat.format(date);

//        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BASKET_QUANTITY, basket.getQuantity());
        values.put(UPDATED_AT, datenow);

        long row = sql_db.update(TBL_BASKETS, values, SERVER_BASKET_ID + "=" + basket.getBasketId(), null);
        sql_db.close();
        return row > 0;
    }

    public boolean emptyBasket(int patient_id) {
//        SQLiteDatabase db = getWritableDatabase();
        long row = sql_db.delete(TBL_BASKETS, PATIENT_ID + "=" + patient_id, null);
        sql_db.close();
        return row > 0;

    }
    
}

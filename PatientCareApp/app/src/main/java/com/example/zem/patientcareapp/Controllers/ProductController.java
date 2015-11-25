package com.example.zem.patientcareapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zem.patientcareapp.Model.Medicine;
import com.example.zem.patientcareapp.Model.Product;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Zem on 11/23/2015.
 */
public class ProductController extends DbHelper {

    DbHelper dbhelper;
    SQLiteDatabase sql_db;

     // PRODUCTS TABLE
    public static final String TBL_PRODUCTS = "products",
            PRODUCT_SUBCATEGORY_ID = "subcategory_id",
            SERVER_PRODUCT_ID = "product_id",
            PRODUCT_NAME = "name",
            PRODUCT_GENERIC_NAME = "generic_name",
            PRODUCT_DESCRIPTION = "description",
            PRODUCT_PRESCRIPTION_REQUIRED = "prescription_required",
            PRODUCT_PRICE = "price",
            PRODUCT_UNIT = "unit",
            PRODUCT_PACKING = "packing",
            PRODUCT_QTY_PER_PACKING = "qty_per_packing",
            PRODUCT_SKU = "sku",
            PRODUCT_CRITICAL_STOCK = "critical_stock";

    public static final String CREATE_TABLE = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, %s TEXT,  %s TEXT, %s TEXT , %s INTEGER, %s DOUBLE, %s TEXT, %s TEXT, %s INTEGER, %s  TEXT , %s  INTEGER , %s  TEXT,  %s  TEXT , %s  TEXT  )",
                TBL_PRODUCTS, AI_ID, SERVER_PRODUCT_ID, PRODUCT_SUBCATEGORY_ID, PRODUCT_NAME, PRODUCT_GENERIC_NAME, PRODUCT_DESCRIPTION, PRODUCT_PRESCRIPTION_REQUIRED, PRODUCT_PRICE, PRODUCT_UNIT, PRODUCT_PACKING, PRODUCT_QTY_PER_PACKING, PRODUCT_SKU, PRODUCT_CRITICAL_STOCK, CREATED_AT, UPDATED_AT, DELETED_AT);

    public ProductController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        sql_db = dbhelper.getWritableDatabase();
    }

    public boolean saveProduct(Product product, String request) {
//        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        values.put(SERVER_PRODUCT_ID, product.getProductId());
        values.put(PRODUCT_SUBCATEGORY_ID, product.getSubCategoryId());
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_GENERIC_NAME, product.getGenericName());
        values.put(PRODUCT_DESCRIPTION, product.getDescription());
        values.put(PRODUCT_PRESCRIPTION_REQUIRED, product.getPrescriptionRequired());
        values.put(PRODUCT_CRITICAL_STOCK, product.getPhoto());
        values.put(PRODUCT_PRICE, product.getPrice());
        values.put(PRODUCT_SKU, product.getSku());
        values.put(PRODUCT_UNIT, product.getUnit());
        values.put(PRODUCT_PACKING, product.getPacking());
        values.put(PRODUCT_QTY_PER_PACKING, product.getQtyPerPacking());
        values.put(CREATED_AT, product.getCreatedAt());
        values.put(UPDATED_AT, product.getUpdatedAt());
        values.put(DELETED_AT, product.getDeletedAt());

        long rowID = 0;

        if (request.equals("insert")) {
            rowID = sql_db.insert(TBL_PRODUCTS, null, values);
        } else if (request.equals("update")) {
            rowID = sql_db.update(TBL_PRODUCTS, values, AI_ID + "=" + product.getProductId(), null);
        }

        sql_db.close();
        return rowID > 0;
    }

    public ArrayList<HashMap<String, String>> getProductsBySubCategory(int subCategoryId) {
        ArrayList<HashMap<String, String>> products = new ArrayList();
//        SQLiteDatabase db = getWritableDatabase();
        String sql;

        if (subCategoryId == 0) {
            sql = "SELECT * FROM " + TBL_PRODUCTS;
        } else {
            sql = "SELECT * FROM " + TBL_PRODUCTS + " WHERE subcategory_id='" + subCategoryId + "'";
        }

        Cursor cur = sql_db.rawQuery(sql, null);
        cur.moveToFirst();

        while (!cur.isAfterLast()) {
            HashMap<String, String> map = new HashMap();
            map.put(AI_ID, cur.getString(cur.getColumnIndex(AI_ID)));
            map.put(PRODUCT_SUBCATEGORY_ID, cur.getString(cur.getColumnIndex(PRODUCT_SUBCATEGORY_ID)));
            map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            map.put(PRODUCT_GENERIC_NAME, cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            map.put(PRODUCT_DESCRIPTION, cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            map.put(PRODUCT_PRESCRIPTION_REQUIRED, cur.getString(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            map.put(PRODUCT_PRICE, cur.getString(cur.getColumnIndex(PRODUCT_PRICE)));
            map.put(PRODUCT_UNIT, cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            map.put(PRODUCT_CRITICAL_STOCK, cur.getString(cur.getColumnIndex(PRODUCT_CRITICAL_STOCK)));
            map.put(CREATED_AT, cur.getString(cur.getColumnIndex(CREATED_AT)));
            map.put(UPDATED_AT, cur.getString(cur.getColumnIndex(UPDATED_AT)));
            map.put(DELETED_AT, cur.getString(cur.getColumnIndex(DELETED_AT)));

            products.add(map);
            cur.moveToNext();
        }
        cur.close();
        sql_db.close();
        return products;
    }

    public Product getProductById(int id) {
        Product prod = new Product();
//        SQLiteDatabase db = getWritableDatabase();
        String sql = "Select * from " + TBL_PRODUCTS + " where product_id='" + id + "'";

        Cursor cur = sql_db.rawQuery(sql, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            prod.setId(cur.getInt(cur.getColumnIndex(AI_ID)));
            prod.setProductId(cur.getInt(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            prod.setSubCategoryId(cur.getInt(cur.getColumnIndex(AI_ID)));
            prod.setName(cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            prod.setGenericName(cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            prod.setDescription(cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            prod.setPrescriptionRequired(cur.getInt(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            prod.setPrice(cur.getDouble(cur.getColumnIndex(PRODUCT_PRICE)));
            prod.setUnit(cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            prod.setPacking(cur.getString(cur.getColumnIndex(PRODUCT_PACKING)));
            prod.setQtyPerPacking(cur.getInt(cur.getColumnIndex(PRODUCT_QTY_PER_PACKING)));
            prod.setPhoto(cur.getString(cur.getColumnIndex(PRODUCT_CRITICAL_STOCK)));
            prod.setCreatedAt(cur.getString(cur.getColumnIndex(CREATED_AT)));
            prod.setUpdatedAt(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            prod.setDeletedAt(cur.getString(cur.getColumnIndex(DELETED_AT)));
            cur.moveToNext();
        }
        cur.close();
        sql_db.close();
        return prod;
    }

    public int getProductServerIdById(int id) {
//        SQLiteDatabase db = getWritableDatabase();
        String sql = "Select * from " + TBL_PRODUCTS + " where id='" + id + "'";

        Cursor cur = sql_db.rawQuery(sql, null);
        cur.moveToFirst();
        int pID = 0;
        while (!cur.isAfterLast()) {
            pID = cur.getInt(cur.getColumnIndex(AI_ID));
            cur.moveToNext();
        }
        cur.close();
        sql_db.close();
        return pID;
    }

    public ArrayList<HashMap<String, String>> getAllProducts() {
        ArrayList<HashMap<String, String>> products = new ArrayList();
//        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PRODUCTS;
        Cursor cur = sql_db.rawQuery(sql, null);

        while (cur.moveToNext()) {

            HashMap<String, String> map = new HashMap();
            map.put(AI_ID, cur.getString(cur.getColumnIndex(AI_ID)));
            map.put(SERVER_PRODUCT_ID, cur.getString(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            map.put(PRODUCT_GENERIC_NAME, cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            map.put(PRODUCT_DESCRIPTION, cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            map.put(PRODUCT_PRICE, cur.getString(cur.getColumnIndex(PRODUCT_PRICE)));
            map.put(PRODUCT_CRITICAL_STOCK, cur.getString(cur.getColumnIndex(PRODUCT_CRITICAL_STOCK)));
            map.put(PRODUCT_SKU, cur.getString(cur.getColumnIndex(PRODUCT_SKU)));
            map.put(PRODUCT_UNIT, cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            map.put(PRODUCT_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_PACKING)));
            map.put(PRODUCT_QTY_PER_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_QTY_PER_PACKING)));
            map.put(PRODUCT_PRESCRIPTION_REQUIRED, cur.getString(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            products.add(map);
        }
        cur.close();
        sql_db.close();
        return products;
    }

    public ArrayList<String> getMedicine() {
        ArrayList<String> medicine = new ArrayList();
        String sql = "SELECT p.name, generic_name, d.name FROM products as p LEFT OUTER JOIN dosage_format_and_strength as d ON d.product_id = p.product_id";
//        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = sql_db.rawQuery(sql, null);
        String med, name, joinedMedicine = "";

        while (cur.moveToNext()) {
            med = cur.getString(0);
            if (cur.getString(2) == null) {
                joinedMedicine = med + "";
            } else {
                name = cur.getString(2);
                joinedMedicine = med + " (" + name + ")";
            }
            medicine.add(joinedMedicine);
        }
        cur.close();
        sql_db.close();
        return medicine;
    }

    public Medicine getSpecificMedicine(String med_name) {
        Medicine medicine = new Medicine();
//        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PRODUCTS + " WHERE " + PRODUCT_NAME + " = '" + med_name + "'";
        Cursor cur = sql_db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            medicine.setId(cur.getInt(cur.getColumnIndex(AI_ID)));
            medicine.setServerID(cur.getInt(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            medicine.setMedicine_name(cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            medicine.setGeneric_name(cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            medicine.setDescription(cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            medicine.setPrice(cur.getDouble(cur.getColumnIndex(PRODUCT_PRICE)));
            medicine.setUnit(cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            medicine.setPhoto(cur.getString(cur.getColumnIndex(PRODUCT_CRITICAL_STOCK)));
        }
        cur.close();
        sql_db.close();

        return medicine;
    }
}

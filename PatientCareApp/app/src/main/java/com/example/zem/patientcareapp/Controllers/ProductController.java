package com.example.zem.patientcareapp.Controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zem.patientcareapp.Model.Medicine;
import com.example.zem.patientcareapp.Model.Product;

import java.util.ArrayList;

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

    public ArrayList<String> getMedicine() {
        ArrayList<String> medicine = new ArrayList();
        String sql = "SELECT p.name, generic_name, d.name FROM products as p LEFT OUTER JOIN dosage_format_and_strength as d ON d.product_id = p.product_id";
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
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
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
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

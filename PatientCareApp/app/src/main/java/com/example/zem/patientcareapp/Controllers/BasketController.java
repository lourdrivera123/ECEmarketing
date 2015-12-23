package com.example.zem.patientcareapp.Controllers;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 11/23/2015.
 */

public class BasketController {

    public BasketController() {

    }

    public ArrayList<HashMap<String, String>> convertFromJson(Context context, JSONArray json_array) {
        ArrayList<HashMap<String, String>> basketItems = new ArrayList();

        try {
            for (int x = 0; x < json_array.length(); x++) {
                JSONObject obj = json_array.getJSONObject(x);

                HashMap<String, String> map = new HashMap();
                map.put(ProductController.SERVER_PRODUCT_ID, String.valueOf(obj.getInt("id")));
                map.put("basket_id", String.valueOf(obj.getInt("basket_id")));
                map.put(ProductController.PRODUCT_NAME, obj.getString("name"));
                map.put(ProductController.PRODUCT_PRICE, String.valueOf(obj.getDouble("price")));
                map.put("quantity", String.valueOf(obj.getInt("quantity")));
                map.put(ProductController.PRODUCT_UNIT, obj.getString("unit"));
                map.put(ProductController.PRODUCT_SKU, obj.getString("sku"));
                map.put(ProductController.PRODUCT_PACKING, obj.getString("packing"));
                map.put(ProductController.PRODUCT_QTY_PER_PACKING, String.valueOf(obj.getInt("qty_per_packing")));
                map.put(ProductController.PRODUCT_PRESCRIPTION_REQUIRED, String.valueOf(obj.getInt("prescription_required")));
                map.put("prescription_id", String.valueOf(obj.getInt("prescription_id")));
                map.put("is_approved", String.valueOf(obj.getInt("is_approved")));

                basketItems.add(map);
            }
        } catch (Exception e) {
            Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
        }

        return basketItems;
    }
}

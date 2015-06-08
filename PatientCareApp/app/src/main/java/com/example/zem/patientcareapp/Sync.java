package com.example.zem.patientcareapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dexter B. on 5/11/2015.
 */
public class Sync {

    JSONArray json_array_mysql = null;
    JSONArray json_array_sqlite = null;
    JSONArray json_array_final = null;
    JSONArray json_array_final_update = null;

    RequestQueue queue;
    String url, tableName, tableId;
    DbHelper dbHelper;
    Context context;

    public void init(Context c, String request, String table_name, String table_id, JSONObject response){
        tableName = table_name;
        tableId = table_id;
        context = c;

        dbHelper = new DbHelper(context);
        queue = Volley.newRequestQueue(context);
        url = "http://vinzry.0fees.us/db/get.php?q="+request;

                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        json_array_mysql = response.getJSONArray(tableName);
                        json_array_sqlite = dbHelper.getAllJSONArrayFrom(tableName);

                        Log.d("jsonarraymysql", ""+json_array_mysql);
                        Log.d("jsonarraysqlite", ""+json_array_sqlite);

                        json_array_final = checkWhatToInsert(json_array_mysql, json_array_sqlite, tableId);

                        json_array_final_update = checkWhatToUpdate(json_array_mysql, tableName);
//

                        if (json_array_final != null){
                            for (int i = 0; i < json_array_final.length(); i++) {
                                JSONObject json_object = json_array_final.getJSONObject(i);

                                System.out.print("GWAPO LAGI KO: ");
                                System.out.println(json_object);


                                if(json_object != null){
                                    switch (tableName) {
                                        case "products":
                                            if (dbHelper.saveProduct(setProduct(json_object), "insert")) {
                                                Toast.makeText(context, "successfully saved ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "doctors":
                                            if (dbHelper.saveDoctor(setDoctor(json_object), "insert")) {
                                                Toast.makeText(context, "successfully saved ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                                            }
                                            break;

                                        case "specialties":
                                            if (dbHelper.saveSpecialty(setSpecialty(json_object), "insert")) {
                                                Toast.makeText(context, "successfully saved ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "sub_specialties":
                                            if (dbHelper.saveSubSpecialty(setSubSpecialty(json_object), "insert")) {
                                                Toast.makeText(context, "successfully saved ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "product_categories":
                                            try {
                                                if (dbHelper.insertProductCategory(setProductCategory(json_object))) {
                                                    Toast.makeText(context, "successfully saved ", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "product_subcategories":
                                            if (dbHelper.insertProductSubCategory(setProductSubCategory(json_object))) {
                                                Toast.makeText(context, "successfully saved ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "dosage_format_and_strength":
                                            if (dbHelper.insertDosage(setDosage(json_object))) {
                                                Toast.makeText(context, "successfully saved ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                    }
                                }
                            }

                           json_array_final = null;
                        } else {
                            Toast.makeText(context, "the final list is empty", Toast.LENGTH_SHORT).show();
                        }

                        if (json_array_final_update != null) {
                            for (int i = 0; i < json_array_final_update.length(); i++) {
                                JSONObject json_object = json_array_final_update.getJSONObject(i);
                                if (!json_object.equals("null") && !json_object.equals(null)) {
                                    if (tableName == "doctors") {
                                        if (dbHelper.saveDoctor(setDoctor(json_object), "update")) {
                                            Toast.makeText(context, "successfully saved ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if(tableName == "products") {
                                        if (dbHelper.saveProduct(setProduct(json_object), "update")) {
                                            Toast.makeText(context, "successfully saved ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }


                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "general error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

    public Dosage setDosage(JSONObject json_object){
        Dosage dosage = new Dosage();
        try {
            dosage.setDosage_id(json_object.getInt("id"));
            dosage.setProduct_id(json_object.getInt("product_id"));
            dosage.setName(json_object.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dosage;
    }

    public JSONArray checkWhatToInsert(JSONArray json_array_mysql, JSONArray json_array_sqlite, String server_id) throws JSONException {
        JSONArray json_array_final_storage = new JSONArray();
        try {
            for (int i = 0; i < json_array_mysql.length(); i++) {
                JSONObject product_json_object_mysql = json_array_mysql.getJSONObject(i);
                Boolean flag = false;

                if(json_array_sqlite == null){
                   json_array_final_storage.put(product_json_object_mysql);
                } else {

                    for (int x = 0; x < json_array_sqlite.length(); x++) {
                        JSONObject json_object_sqlite = json_array_sqlite.getJSONObject(x);

                        if (product_json_object_mysql.getInt("id") == json_object_sqlite.getInt(server_id)) {
                            flag = true;
                        }
                    }

                    if (!flag) {
                       json_array_final_storage.put(product_json_object_mysql);
                    }

                }
            }

        } catch (JSONException e) {
            Toast.makeText(context, "error in check what to insert" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return json_array_final_storage;
    }

    public JSONArray checkWhatToUpdate(JSONArray json_array_mysql, String tblname) {
        JSONArray doctors_json_array_final_storage = new JSONArray();
        try {

            for (int i = 0; i < json_array_mysql.length(); i++) {

                JSONObject json_object_mysql = json_array_mysql.getJSONObject(i);

                if(!json_object_mysql.getString("updated_at").equals("null")){

                if (checkDateTime(dbHelper.getLastUpdate(tblname), json_object_mysql.getString("updated_at"))) { //to be repared
                    //the sqlite last update is lesser than from mysql
                    //put your json object into final array here.

                    doctors_json_array_final_storage.put(json_object_mysql);
                    Log.d("Updated at Compare", "the updated_at column in mysql is greater than in sqlite");

                } else {
                    Log.d("Updated at Compare", "the updated_at column in sqlite is greater than in mysql");

                }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doctors_json_array_final_storage;
    }

    public boolean checkDateTime(String str1, String str2) {
        Boolean something = false;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            /*String str1 = "12/10/2013";*/
            Date date1 = formatter.parse(str1);

            /*String str2 = "13/10/2013";*/
            Date date2 = formatter.parse(str2);

            if (date1.compareTo(date2) < 0) {
                something = true;
                System.out.println("date2 is Greater than my date1");
//            Log.d("date2 is Greater than my date1", "adsasd");
            }


        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return something;
    }

    // SETTERS

    public Doctor setDoctor(JSONObject json) {
        Doctor doctor = new Doctor();
        try {

            doctor.setDoc_id(json.getInt("id"));
            doctor.setFullname(json.getString("fname"), json.getString("mname"), json.getString("lname"));
            doctor.setPrc_no(json.getInt("prc_no"));
            doctor.setSub_specialty_id(json.getInt("sub_specialty_id"));
            doctor.setCell_no(json.getString("cell_no"));
            doctor.setTel_no(json.getString("tel_no"));
            doctor.setPhoto(json.getString("photo"));
            doctor.setAffiliation(json.getString("affiliation"));
            doctor.setEmail(json.getString("email"));
            doctor.setCreated_at(json.getString("created_at"));
            doctor.setUpdated_at(json.getString("updated_at"));
            doctor.setDeleted_at(json.getString("deleted_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doctor;
    }

    public Specialty setSpecialty(JSONObject json) {
        Specialty specialty = new Specialty();
        try {

            specialty.setSpecialty_id(json.getInt("id"));
            specialty.setName(json.getString("name"));
            specialty.setCreated_at(json.getString("created_at"));
            specialty.setUpdated_at(json.getString("updated_at"));
            specialty.setDeleted_at(json.getString("deleted_at"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return specialty;
    }

    public SubSpecialty setSubSpecialty(JSONObject json) {
        SubSpecialty sub_specialty = new SubSpecialty();
        try {

            sub_specialty.setSub_specialty_id(json.getInt("id"));
            sub_specialty.setSpecialty_id(json.getInt("specialty_id"));
            sub_specialty.setName(json.getString("name"));
            sub_specialty.setCreated_at(json.getString("created_at"));
            sub_specialty.setUpdated_at(json.getString("updated_at"));
            sub_specialty.setDeleted_at(json.getString("deleted_at"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sub_specialty;
    }

    public ProductCategory setProductCategory(JSONObject json)throws JSONException{
        ProductCategory pc = new ProductCategory();
        try{
            pc.setName(json.getString("name"));
            pc.setCategoryId(Integer.parseInt(json.getString("id")));
            pc.setCreatedAt(json.getString("created_at"));
            pc.setUpdatedAt(json.getString("updated_at"));
            pc.setDeletedAt(json.getString("deleted_at"));
        }catch(JSONException e){
            e.printStackTrace();
        }
        return pc;
    }

    public ProductSubCategory setProductSubCategory(JSONObject json)throws JSONException {
        ProductSubCategory sc = new ProductSubCategory();
        try {
            sc.setName(json.getString("name"));
            sc.setId(Integer.parseInt(json.getString("id")));
            sc.setCategoryId(Integer.parseInt(json.getString("category_id")));
            sc.setCreatedAt(json.getString("created_at"));
            sc.setUpdatedAt(json.getString("updated_at"));
            sc.setDeletedAt(json.getString("deleted_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sc;
    }

    public Patient setPatient(JSONObject json){
        Patient patient = new Patient();
        try {
            patient.setServerID(json.getInt("id"));
            patient.setUsername(json.getString("username"));
            patient.setPassword(json.getString("password"));
            patient.setOccupation(json.getString("occupation"));
            patient.setBirthdate(json.getString("birthdate"));
            patient.setSex(json.getString("sex"));
            patient.setCivil_status(json.getString("civil_status"));
            patient.setHeight(json.getString("height"));
            patient.setWeight(json.getString("weight"));
            patient.setFullname(json.getString("fname"), json.getString("mname"), json.getString("lname"));
            patient.setFullAddress(json.getInt("unit_floor_room_no"), json.getString("building"),
                    json.getInt("lot_no"), json.getInt("block_no"), json.getInt("phase_no"),
                    json.getInt("address_house_no"), json.getString("address_street"),
                    json.getString("address_barangay"), json.getString("address_city_municipality"),
                    json.getString("address_province"), json.getString("address_region"),
                    json.getString("address_zip"));
            patient.setTel_no(json.getString("tel_no"));
            patient.setMobile_no(json.getString("mobile_no"));
            patient.setEmail(json.getString("email_address"));
            patient.setPhoto(json.getString("photo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patient;
    }
    public Product setProduct(JSONObject json) {
        Product product = new Product();

        try {
            product.setProductId(json.getInt("id"));
            product.setName(json.getString("name"));
            product.setSubCategoryId(Integer.parseInt(json.getString("subcategory_id")));
            product.setGenericName(json.getString("generic_name"));
            product.setDescription(json.getString("description"));
            product.setPrescriptionRequired(Integer.parseInt(json.getString("prescription_required")));
            product.setPrice(json.getDouble("price"));
            product.setUnit(json.getString("unit"));
            product.setCreatedAt(json.getString("created_at"));
            product.setUpdatedAt(json.getString("updated_at"));
            product.setDeletedAt(json.getString("deleted_at"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return product;
    }
}

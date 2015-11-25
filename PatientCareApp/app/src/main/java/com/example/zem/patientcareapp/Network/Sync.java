package com.example.zem.patientcareapp.Network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.zem.patientcareapp.Controllers.BasketController;
import com.example.zem.patientcareapp.Controllers.ClinicController;
import com.example.zem.patientcareapp.Controllers.ClinicDoctorController;
import com.example.zem.patientcareapp.Controllers.DiscountsFreeProductsController;
import com.example.zem.patientcareapp.Controllers.DoctorController;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Controllers.DosageController;
import com.example.zem.patientcareapp.Controllers.FreeProductsController;
import com.example.zem.patientcareapp.Controllers.PatientPrescriptionController;
import com.example.zem.patientcareapp.Controllers.PatientRecordController;
import com.example.zem.patientcareapp.Controllers.PatientTreatmentsController;
import com.example.zem.patientcareapp.Controllers.ProductCategoryController;
import com.example.zem.patientcareapp.Controllers.ProductController;
import com.example.zem.patientcareapp.Controllers.ProductSubCategoryController;
import com.example.zem.patientcareapp.Controllers.PromoController;
import com.example.zem.patientcareapp.Controllers.SpecialtyController;
import com.example.zem.patientcareapp.Controllers.SubSpecialtyController;
import com.example.zem.patientcareapp.Model.DiscountsFreeProducts;
import com.example.zem.patientcareapp.Model.Basket;
import com.example.zem.patientcareapp.Model.Clinic;
import com.example.zem.patientcareapp.Model.ClinicDoctor;
import com.example.zem.patientcareapp.Model.Consultation;
import com.example.zem.patientcareapp.Model.Doctor;
import com.example.zem.patientcareapp.Model.Dosage;
import com.example.zem.patientcareapp.Model.FreeProducts;
import com.example.zem.patientcareapp.Model.Patient;
import com.example.zem.patientcareapp.Model.PatientRecord;
import com.example.zem.patientcareapp.Model.Product;
import com.example.zem.patientcareapp.Model.ProductCategory;
import com.example.zem.patientcareapp.Model.ProductSubCategory;
import com.example.zem.patientcareapp.Model.Specialty;
import com.example.zem.patientcareapp.Model.SubSpecialty;
import com.example.zem.patientcareapp.Model.Promo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Dexter B. on 5/11/2015.
 */
public class Sync {

    JSONArray json_array_mysql = null;
    JSONArray json_array_sqlite = null;
    JSONArray json_array_final = null;
    JSONArray json_array_final_update = null;

    String tableName, tableId;
    DbHelper dbHelper;
    ProductController pc;
    DoctorController doctor_controller;
    SpecialtyController sp;
    SubSpecialtyController ssp;
    ProductCategoryController pcc;
    ProductSubCategoryController pscc;
    DosageController dc;
    BasketController bc;
    PatientRecordController prc;
    PatientTreatmentsController ptc;
    DiscountsFreeProductsController dfpc;
    FreeProductsController fpc;
    PromoController prmc;
    ClinicController clinic_controller;
    ClinicDoctorController cdc;
    PatientPrescriptionController ppc;
    Context context;

    public void init(Context c, String request, String table_name, String table_id, JSONObject response) {
        tableName = table_name;
        tableId = table_id;
        context = c;

        dbHelper = new DbHelper(context);
        pc = new ProductController(context);
        sp = new SpecialtyController(context);
        ssp = new SubSpecialtyController(context);
        pcc = new ProductCategoryController(context);
        pscc = new ProductSubCategoryController(context);
        dc = new DosageController(context);
        bc = new BasketController(context);
        prc = new PatientRecordController(context);
        ptc = new PatientTreatmentsController(context);
        doctor_controller = new DoctorController(context);
        dfpc = new DiscountsFreeProductsController(context);
        fpc = new FreeProductsController(context);
        prmc = new PromoController(context);
        clinic_controller = new ClinicController(context);
        cdc = new ClinicDoctorController(context);
        ppc = new PatientPrescriptionController(context);
        try {
            int success = response.getInt("success");
            if (success == 1) {
                json_array_mysql = response.getJSONArray(tableName);
                json_array_sqlite = dbHelper.getAllJSONArrayFrom(tableName);

                json_array_final = checkWhatToInsert(json_array_mysql, json_array_sqlite, tableId);
                json_array_final_update = checkWhatToUpdate(json_array_mysql, tableName, response.getString("latest_updated_at"));

                if (json_array_final != null) {
                    for (int i = 0; i < json_array_final.length(); i++) {
                        JSONObject json_object = json_array_final.getJSONObject(i);

                        if (json_object != null) {
                            if (tableName.equals("products")) {
                                if (!pc.saveProduct(setProduct(json_object), "insert"))
                                    Log.d("sync_22", "wala na save");
                            } else if (tableName.equals("doctors")) {
                                if (!doctor_controller.saveDoctor(setDoctor(json_object), "insert"))
                                    Log.d("sync_21", "wala na save");
                            } else if (tableName.equals("specialties")) {
                                if (!sp.saveSpecialty(setSpecialty(json_object), "insert"))
                                    Log.d("sync_20", "wala na save");
                            } else if (tableName.equals("sub_specialties")) {
                                if (!ssp.saveSubSpecialty(setSubSpecialty(json_object), "insert"))
                                    Log.d("sync_19", "wala na save");
                            } else if (tableName.equals("product_categories")) {
                                try {
                                    if (!pcc.insertProductCategory(setProductCategory(json_object)))
                                        Log.d("sync_18", "wala na save");
                                } catch (Exception e) {
                                    Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            } else if (tableName.equals("product_subcategories")) {
                                if (!pscc.insertProductSubCategory(setProductSubCategory(json_object)))
                                    Log.d("sync_17", "wala na save");
                            } else if (tableName.equals("dosage_format_and_strength")) {
                                if (!dc.insertDosage(setDosage(json_object)))
                                    Log.d("sync_16", "wala na save");
                            } else if (tableName.equals("baskets")) {
                                if (!bc.insertBasket(setBasket(json_object)))
                                    Log.d("sync_15", "wala na save");
                            } else if (tableName.equals("patient_records")) {
                                if (prc.savePatientRecord(setPatientRecord(json_object), "insert"))
                                    Log.d("sync_14", "wala na save");
                            } else if (tableName.equals("patient_treatments")) {
                                if (!ptc.savePatientTreatments(setTreatments(json_object), "insert"))
                                    Log.d("sync_13", "wala na save");
                            } else if (tableName.equals("discounts_free_products")) {
                                if (!dfpc.saveDiscountsFreeProducts(setDiscountsFreeProducts(json_object), "insert"))
                                    Log.d("sync_12", "wala na save");
                            } else if (tableName.equals("free_products")) {
                                if (!fpc.saveFreeProducts(setFreeProducts(json_object), "insert"))
                                    Log.d("sync_11", "wala na save");
                            } else if (tableName.equals("promo")) {
                                if (!prmc.savePromo(setPromo(json_object), "insert"))
                                    Log.d("sync_10", "wala na save");
                            } else if (tableName.equals("clinics")) {
                                if (!clinic_controller.saveClinic(setClinic(json_object), "insert"))
                                    Log.d("sync_9", "wala na save");
                            } else if (tableName.equals("clinic_doctor")) {
                                if (!cdc.saveClinicDoctor(setClinicDoctor(json_object), "insert"))
                                    Log.d("sync_8", "wala na save");
                            } else if (tableName.equals("patient_prescriptions")) {
                                if (!ppc.savePrescription(json_object))
                                    Log.d("sync_7", "wala na save");
                            } else if (tableName.equals("settings")) {
                                if (!dbHelper.saveSettings(json_object, "insert"))
                                    Log.d("sync_6", "wala na save");
                            } else if (tableName.equals("branches")) {
                                if (!dbHelper.saveBranches(json_object))
                                    Log.d("sync_5", "wala na save");
                            } else if (tableName.equals("orders")) {
                                if (!dbHelper.saveOrders(json_object))
                                    Log.d("sync_4", "wala na save");
                            } else if (tableName.equals("order_details")) {
                                if (!dbHelper.saveOrderDetails(json_object))
                                    Log.d("sync_3", "wala na save");
                            } else if (tableName.equals("messages")) {
                                if (!dbHelper.saveMessages(json_object, "insert"))
                                    Log.d("sync_2", "wala na save");
                            } else if (tableName.equals("consultations")) {
                                if (!dbHelper.savePatientConsultation(setConsultation(json_object), "add"))
                                    Log.d("sync_1", "wala na save");
                            }
                        }
                    }
                    json_array_final = null;
                } else
                    Toast.makeText(context, "the final list is empty", Toast.LENGTH_SHORT).show();

                if (json_array_final_update != null) {
                    for (int i = 0; i < json_array_final_update.length(); i++) {
                        JSONObject json_object = json_array_final_update.getJSONObject(i);
                        if (!json_object.equals("null") && !json_object.equals(null)) {
                            if (tableName.equals("doctors")) {
                                if (!doctor_controller.saveDoctor(setDoctor(json_object), "update"))
                                    Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                            } else if (tableName.equals("products")) {
                                if (!dbHelper.saveProduct(setProduct(json_object), "update"))
                                    Toast.makeText(context, "failed to save ", Toast.LENGTH_SHORT).show();
                            } else if (tableName.equals("settings")) {
                                if (!dbHelper.saveSettings(json_object, "update"))
                                    System.out.print("referral_settings FAILED TO SAVE <src: Sync.java>");
                            } else if (tableName.equals("messages")) {
                                if (!dbHelper.saveMessages(json_object, "update"))
                                    System.out.print("messages FAILED TO SAVE <src: Sync.java>");
                            } else if (tableName.equals("consultations")) {
                                if (!dbHelper.savePatientConsultation(setConsultation(json_object), "update"))
                                    System.out.print("consultations FAILED TO SAVE <src: Sync.java>");
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

    public Consultation setConsultation(JSONObject json) {
        Consultation consult = new Consultation();

        try {
            consult.setServerID(json.getInt("id"));
            consult.setPatientID(json.getInt("patient_id"));
            consult.setDoctorID(json.getInt("doctor_id"));
            consult.setClinicID(json.getInt("clinic_id"));
            consult.setDate(json.getString("date"));
            consult.setTime(json.getString("time"));
            consult.setIsAlarmed(json.getInt("is_alarm"));
            consult.setAlarmedTime(json.getString("alarm_time"));
            consult.setIs_approved(json.getInt("is_approved"));
            consult.setIs_read(json.getInt("isRead"));
            consult.setComment_doctor(json.getString("comment_doctor"));
            consult.setPtnt_is_approved(json.getInt("patient_is_approved"));
            consult.setComment_patient(json.getString("comment_patient"));
            consult.setCreated_at(json.getString("created_at"));
            consult.setUpdated_at(json.getString("updated_at"));
        } catch (Exception e) {
            Log.d("sync1", e + "");
        }

        return consult;
    }

    public Dosage setDosage(JSONObject json_object) {
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

    public PatientRecord setPatientRecord(JSONObject json_object) {
        PatientRecord patient_record = new PatientRecord();
        try {
            patient_record.setRecordID(json_object.getInt("id"));
            patient_record.setPatientID(json_object.getInt("patient_id"));
            patient_record.setComplaints(json_object.getString("complaints"));
            patient_record.setFindings(json_object.getString("findings"));
            patient_record.setDate(json_object.getString("record_date"));
            patient_record.setDoctorID(json_object.getInt("doctor_id"));
            patient_record.setClinicID(json_object.getInt("clinic_id"));
            patient_record.setDoctorName(json_object.getString("doctor_name"));
            patient_record.setClinicName(json_object.getString("clinic_name"));
            patient_record.setCreated_at(json_object.getString("created_at"));
            patient_record.setUpdated_at(json_object.getString("updated_at"));
            patient_record.setDeleted_at(json_object.getString("deleted_at"));
        } catch (Exception e) {

        }
        return patient_record;
    }

    public ArrayList<HashMap<String, String>> setTreatments(JSONObject json) {
        ArrayList<HashMap<String, String>> listOfTreatments = new ArrayList();

        try {
            HashMap<String, String> map = new HashMap();
            map.put("treatmentts_id", String.valueOf(json.getInt("id")));
            map.put("patient_records_id", String.valueOf(json.getInt("patient_records_id")));
            map.put("medicine_name", json.getString("medicine_name"));
            map.put("generic_name", json.getString("generic_name"));
            map.put("dosage", json.getString("dosage"));
            listOfTreatments.add(map);
        } catch (Exception e) {

        }
        return listOfTreatments;
    }

    public JSONArray checkWhatToInsert(JSONArray json_array_mysql, JSONArray json_array_sqlite, String server_id) throws JSONException {
        JSONArray json_array_final_storage = new JSONArray();
        try {
            for (int i = 0; i < json_array_mysql.length(); i++) {
                JSONObject product_json_object_mysql = json_array_mysql.getJSONObject(i);
                Boolean flag = false;

                if (json_array_sqlite == null) {
                    json_array_final_storage.put(product_json_object_mysql);
                } else {
                    //checking each row in sqlite if the mysql id exists
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

    public JSONArray checkWhatToUpdate(JSONArray json_array_mysql, String tblname, String latest_updated_at) {
        JSONArray doctors_json_array_final_storage = new JSONArray();
        try {
            if (!dbHelper.getLastUpdate(tblname).equals(latest_updated_at)) {

                for (int i = 0; i < json_array_mysql.length(); i++) {

                    JSONObject json_object_mysql = json_array_mysql.getJSONObject(i);

                    if (!json_object_mysql.getString("updated_at").equals("null") && json_object_mysql.getString("updated_at") != null) {

                        if (checkDateTime(dbHelper.getLastUpdate(tblname), json_object_mysql.getString("updated_at"))) { //to be repared
                            //the sqlite last update is lesser than from mysql
                            //put your json object into final array here.

                            doctors_json_array_final_storage.put(json_object_mysql);
//                        Log.d("Updated at Compare", "the updated_at column in mysql is greater than in sqlite");

                        } else {
//                        Log.d("Updated at Compare", "the updated_at column in sqlite is greater than in mysql");

                        }
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
            if (!str1.equals("") || str2.equals("")) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            /*String str1 = "12/10/2013";*/
                Date date1 = formatter.parse(str1);

            /*String str2 = "13/10/2013";*/
                Date date2 = formatter.parse(str2);

                if (date1.compareTo(date2) < 0) {
                    something = true;
                    System.out.println("date2 is Greater than my date1");
                }
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

            doctor.setServer_doc_id(json.getInt("id"));
            doctor.setFullname(json.getString("fname"), json.getString("mname"), json.getString("lname"));
            doctor.setPrc_no(json.getInt("prc_no"));
            doctor.setSub_specialty_id(json.getInt("sub_specialty_id"));
            doctor.setAffiliation(json.getString("affiliation"));
            doctor.setEmail(json.getString("email"));
            doctor.setReferral_id(json.getString("referral_id"));
            doctor.setCreated_at(json.getString("created_at"));
            doctor.setUpdated_at(json.getString("updated_at"));
            doctor.setDeleted_at(json.getString("deleted_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doctor;
    }

    public Clinic setClinic(JSONObject json) {
        Clinic clinic = new Clinic();
        try {
            clinic.setName(json.getString("name"));
            clinic.setClinicsId(json.getInt("id"));
            clinic.setContactNumber(json.getString("contact_no"));
            clinic.setFullAddress(json.getString(DbHelper.CLINIC_UNIT_NO), json.getString(DbHelper.CLINIC_BUILDING),
                    json.getString(DbHelper.CLINIC_LOT_NO), json.getString(DbHelper.CLINIC_BLOCK_NO), json.getString(DbHelper.CLINIC_PHASE_NO),
                    json.getString(DbHelper.CLINIC_HOUSE_NO), json.getString(DbHelper.CLINIC_STREET),
                    json.getString(DbHelper.CLINIC_BARANGAY), json.getString(DbHelper.CLINIC_CITY),
                    json.getString(DbHelper.CLINIC_PROVINCE), json.getString(DbHelper.CLINIC_REGION),
                    json.getString(DbHelper.CLINIC_ZIP));
            clinic.setCreatedAt(json.getString("created_at"));
            clinic.setUpdatedAt(json.getString("updated_at"));
            clinic.setDeletedAt(json.getString("deleted_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return clinic;
    }

    public ClinicDoctor setClinicDoctor(JSONObject json) {
        ClinicDoctor cd = new ClinicDoctor();

        try {
            cd.setServerID(json.getInt("id"));
            cd.setDoctorID(json.getInt("doctor_id"));
            cd.setClinicID(json.getInt("clinic_id"));
            cd.setSchedule(json.getString("clinic_sched"));
            cd.setIsActive(json.getInt("is_active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cd;
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

    public ProductCategory setProductCategory(JSONObject json) throws JSONException {
        ProductCategory pc = new ProductCategory();
        try {
            pc.setName(json.getString("name"));
            pc.setCategoryId(Integer.parseInt(json.getString("id")));
            pc.setCreatedAt(json.getString("created_at"));
            pc.setUpdatedAt(json.getString("updated_at"));
            pc.setDeletedAt(json.getString("deleted_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pc;
    }

    public ProductSubCategory setProductSubCategory(JSONObject json) throws JSONException {
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

    public Patient setPatient(JSONObject json) {
        Patient patient = new Patient();
        try {
            patient.setServerID(json.getInt("id"));
            patient.setUsername(json.getString(DbHelper.PTNT_USERNAME));
            patient.setPassword(json.getString(DbHelper.PTNT_PASSWORD));
            patient.setOccupation(json.getString(DbHelper.PTNT_OCCUPATION));
            patient.setBirthdate(json.getString(DbHelper.PTNT_BIRTHDATE));
            patient.setSex(json.getString(DbHelper.PTNT_SEX));
            patient.setCivil_status(json.getString(DbHelper.PTNT_CIVIL_STATUS));
            patient.setHeight(json.getString(DbHelper.PTNT_HEIGHT));
            patient.setWeight(json.getString(DbHelper.PTNT_WEIGHT));
            patient.setFullname(json.getString(DbHelper.PTNT_FNAME), json.getString(DbHelper.PTNT_MNAME), json.getString(DbHelper.PTNT_LNAME));
            patient.setAddress_street(json.getString(DbHelper.PTNT_STREET));
            patient.setBarangay_id(json.getInt("address_barangay_id"));
            patient.setBarangay(json.getString("address_barangay"));
            patient.setMunicipality(json.getString("address_city_municipality"));
            patient.setProvince(json.getString("address_province"));
            patient.setRegion(json.getString("address_region"));
            patient.setTel_no(json.getString(DbHelper.PTNT_TEL_NO));
            patient.setMobile_no(json.getString(DbHelper.PTNT_MOBILE_NO));
            patient.setEmail(json.getString(DbHelper.PTNT_EMAIL));
            patient.setPhoto(json.getString(DbHelper.PTNT_PHOTO));
            patient.setReferral_id(json.getString(DbHelper.PTNT_REFERRAL_ID));
            patient.setReferred_byUser(json.getString(DbHelper.PTNT_REFERRED_BY_USER));
            patient.setReferred_byDoctor(json.getString(DbHelper.PTNT_REFERRED_BY_DOCTOR));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patient;
    }

    public Product setProduct(JSONObject json) {
        Product product = new Product();

        try {
            product.setProductId(json.getInt("id"));
            product.setName(json.getString(DbHelper.PRODUCT_NAME));
            product.setSubCategoryId(Integer.parseInt(json.getString(DbHelper.PRODUCT_SUBCATEGORY_ID)));
            product.setGenericName(json.getString(DbHelper.PRODUCT_GENERIC_NAME));
            product.setDescription(json.getString(DbHelper.PRODUCT_DESCRIPTION));
            product.setPrescriptionRequired(Integer.parseInt(json.getString("prescription_required")));
            product.setPrice(Double.parseDouble(json.getString("price")));
            product.setUnit(json.getString("unit"));
            product.setSku(json.getString("sku"));
            product.setPacking(json.getString("packing"));
            product.setQtyPerPacking(Integer.parseInt(json.getString("qty_per_packing")));
            product.setCreatedAt(json.getString("created_at"));
            product.setUpdatedAt(json.getString("updated_at"));
            product.setDeletedAt(json.getString("deleted_at"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return product;
    }

    public Basket setBasket(JSONObject json) throws JSONException {
        Basket basket = new Basket();
        basket.setBasketId(json.getInt("id"));
        basket.setQuantity(Integer.parseInt(json.getString(DbHelper.BASKET_QUANTITY)));
        basket.setUpdatedAt(json.getString(DbHelper.UPDATED_AT));
        basket.setPatienId(json.getInt(DbHelper.PATIENT_ID));
        basket.setProductId(json.getInt(DbHelper.BASKET_PRODUCT_ID));
        basket.setPrescriptionId(json.getInt(DbHelper.BASKET_PRESCRIPTION_ID));
        basket.setIsApproved(json.getInt(DbHelper.BASKET_IS_APPROVED));
        return basket;
    }

    public DiscountsFreeProducts setDiscountsFreeProducts(JSONObject json) throws JSONException {
        DiscountsFreeProducts discountsFreeProducts = new DiscountsFreeProducts();

        discountsFreeProducts.setDfpId(json.getInt("id"));
        discountsFreeProducts.setProductId(json.getInt(DbHelper.DFP_PRODUCT_ID));
        discountsFreeProducts.setPromoId(json.getInt(DbHelper.DFP_PROMO_ID));
        discountsFreeProducts.setLess(json.getDouble(DbHelper.DFP_LESS));
        discountsFreeProducts.setQuantityRequired(json.getInt(DbHelper.DFP_QUANTITY_REQUIRED));
        discountsFreeProducts.setType(json.getInt(DbHelper.DFP_TYPE));
        discountsFreeProducts.setCreatedAt(json.getString(DbHelper.CREATED_AT));
        discountsFreeProducts.setUpdatedAt(json.getString(DbHelper.UPDATED_AT));
        discountsFreeProducts.setDeletedAt(json.getString(DbHelper.DELETED_AT));

        return discountsFreeProducts;
    }

    public FreeProducts setFreeProducts(JSONObject json) throws JSONException {
        FreeProducts freeProducts = new FreeProducts();

        freeProducts.setFreeProductsId(json.getInt("id"));
        freeProducts.setDfpId(json.getInt(DbHelper.FP_DFP_ID));
        freeProducts.setQuantityFree(json.getInt(DbHelper.FP_QTY_FREE));
        freeProducts.setCreatedAt(json.getString(DbHelper.FP_CREATED_AT));
        freeProducts.setUpdatedAt(json.getString(DbHelper.FP_UPDATED_AT));
        freeProducts.setDeletedAt(json.getString(DbHelper.FP_DELETED_AT));

        return freeProducts;
    }

    public Promo setPromo(JSONObject json) throws JSONException {
        Promo promo = new Promo();

        promo.setServerPromoId(json.getInt("id"));
        promo.setName(json.getString(DbHelper.PROMO_NAME));
        promo.setStartDate(json.getString(DbHelper.PROMO_START_DATE));
        promo.setEndDate(json.getString(DbHelper.PROMO_END_DATE));
        promo.setCreatedAt(json.getString(DbHelper.PROMO_CREATED_AT));
        promo.setUpdatedAt(json.getString(DbHelper.PROMO_UPDATED_AT));
        promo.setDeletedAt(json.getString(DbHelper.PROMO_DELETED_AT));

        return promo;
    }
}

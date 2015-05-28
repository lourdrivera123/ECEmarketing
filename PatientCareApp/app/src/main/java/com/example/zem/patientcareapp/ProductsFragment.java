package com.example.zem.patientcareapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by User PC on 5/5/2015. Updated 5/5/15
 */
public class ProductsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, AdapterView.OnItemSelectedListener {
    // XML node keys
    static final String KEY_PRODUCT_NAME = "name"; // parent node
    static final String KEY_PRODUCT_DESCRIPTION = "description";
    static final String KEY_PRODUCT_PHOTO = "photo";
    static final String KEY_PRODUCT_ID = "id";
    static final String KEY_PRODUCT_PRICE = "price";
    static final String KEY_PRODUCT = "entry";

    Button add_to_cart_btn;
    EditText qty;
    ListView list_of_products, lv_subcategories;
    Spinner lv_categories;
    LazyAdapter adapter;
    ArrayAdapter category_list_adapter;
    DbHelper dbHelper;
    Helpers helpers;
    Sync sync;
    List<String> category_list;

    RequestQueue queue;
    Dialog loc_dialog;
    ProgressDialog pDialog;
    View root_view;
    public static ArrayList<HashMap<String, String>> products_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.products_layout, container, false);
        root_view = rootView;
        lv_categories = (Spinner) rootView.findViewById(R.id.categories);

        dbHelper = new DbHelper(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        helpers = new Helpers();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();



        if (helpers.isNetworkAvailable(getActivity())) {
//            sync = new Sync();
//            sync.init(getActivity(), "get_products", "products", "product_id");
//            queue = sync.getQueue();
//
//            sync = new Sync();
//            sync.init(getActivity(), "get_product_categories", "product_categories", "id");
//            queue = sync.getQueue();
//
//            sync = new Sync();
//            sync.init(getActivity(), "get_product_subcategories&cat=all", "product_subcategories", "id");
//            queue = sync.getQueue();
//
//            sync = new Sync();
//            sync.init(getActivity(), "get_dosages", "dosage_format_and_strength", "dosage_id");
//            queue = sync.getQueue();


            rootView.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 3 seconds

                    dbHelper.getAllProducts();
                    String xml = dbHelper.getProductsStringXml();

                    populateProductsListView(rootView, xml);
                    category_list = dbHelper.getAllProductCategoriesArray();
                    populateListView(rootView, category_list);
                    pDialog.hide();
                }

            }, 3000);

        } else {
            Log.d("Connected to internet", "no");
            dbHelper.getAllProducts();
            String xml = dbHelper.getProductsStringXml();

            populateProductsListView(rootView, xml);
            pDialog.hide();
        }


        return rootView;
    }

    public void populateProductsListView(View rootView, String xml) {
        products_list = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
//        String xml = parser.getXmlFromUrl("http://localhost/db/get.php?q=get_products"); // getting XML from URL

        Document doc = parser.getDomElement(xml); // getting DOM element

        NodeList nl = doc.getElementsByTagName(KEY_PRODUCT);
        // looping through all song nodes &lt;song&gt;
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key =&gt; value
            map.put(KEY_PRODUCT_ID, parser.getValue(e, KEY_PRODUCT_ID));
            map.put(KEY_PRODUCT_NAME, parser.getValue(e, KEY_PRODUCT_NAME));
            map.put(KEY_PRODUCT_DESCRIPTION, parser.getValue(e, KEY_PRODUCT_DESCRIPTION));
            map.put(KEY_PRODUCT_PHOTO, parser.getValue(e, KEY_PRODUCT_PHOTO));
            map.put(KEY_PRODUCT_PRICE, parser.getValue(e, KEY_PRODUCT_PRICE));

            // adding HashList to ArrayList
            products_list.add(map);
        }

        list_of_products = (ListView) rootView.findViewById(R.id.product_lists);

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(getActivity(), products_list, "product_lists");
        list_of_products.setAdapter(adapter);

        // Click event for single list row
        list_of_products.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product prod;
        prod = dbHelper.getProductById(Integer.parseInt(view.getTag().toString()));

        Dialog dialog = new Dialog(getActivity());
        loc_dialog = dialog;
        dialog.setTitle(prod.getName());
        dialog.setContentView(R.layout.dialog_products_layout);
        dialog.show();

        TextView name, price, description;
        name = (TextView) dialog.findViewById(R.id.product_name);
        price = (TextView) dialog.findViewById(R.id.product_price);
        description = (TextView) dialog.findViewById(R.id.product_description);
        add_to_cart_btn = (Button) dialog.findViewById(R.id.add_to_cart_btn);

        name.setText("(" + prod.getGenericName() + ")\n" + prod.getName());
        price.setText("Php " + prod.getPrice() + " / " + prod.getUnit());

        description.setText(prod.getDescription());
        add_to_cart_btn.setText("Add to Cart | Php "+prod.getPrice());
        qty = (EditText) dialog.findViewById(R.id.qty);

        add_to_cart_btn.setTag(prod.getProductId());
        qty.setTag(prod.getPrice());


        add_to_cart_btn.setOnClickListener(this);
        qty.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_to_cart_btn :
                if(helpers.isNetworkAvailable(getActivity())){
                    Basket basket;

                    int productId, patientId;
                    double new_qty;
                    productId = Integer.parseInt(add_to_cart_btn.getTag().toString());

                    new_qty = Double.parseDouble(qty.getText().toString());

                    boolean res = false;

                    /* let's check if the product already exists in our basket */
                    basket = dbHelper.getBasket(productId);
                    System.out.println("BASKETID: "+basket.getId());
                    //  if(basket.getBasketId() > 0  ){
                    if(basket.getId() > 0  ){  /* Replace this with the line above, when it's connected to the server  */
                        double old_qty = basket.getQuantity();
                        basket.setQuantity(new_qty+old_qty);

                        res = dbHelper.updateBasket(basket);
                    }else{
                    /* since, we can't find the product in baskets table, let's insert a new one */
                        basket.setProductId(productId);
                        basket.setQuantity(new_qty);

                        res = dbHelper.insertBasket(basket);
                    }

                    if( res ){
                        Toast.makeText(getActivity(), "Successfully added to your cart.", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getActivity(), "Sorry, we can't process your request right now.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(), "Sorry, please connect to the internet.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String qty_str = qty.getText().toString();
        Double price = Double.parseDouble(qty.getTag().toString());

        if(!qty_str.isEmpty()) {
            try {
                Double qty_int = Double.parseDouble(qty_str);
                Double product = qty_int * price;

                add_to_cart_btn.setText("Add to Cart | Php "+product);
            } catch(Exception e) {

            }
        }
        else {
            add_to_cart_btn.setText("Add to Cart | Php ");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void populateListView(View rootView, List<String> categories){
        lv_categories = (Spinner) rootView.findViewById(R.id.categories);
        lv_subcategories = (ListView) rootView.findViewById(R.id.subcategories);
        categories.add(0, "Select Category");

        category_list_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories);
        lv_categories.setAdapter(category_list_adapter);
        lv_subcategories.setVisibility(View.GONE);
        lv_categories.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            String item = ((TextView)view).getText().toString();

            final int categoryId = dbHelper.categoryGetIdByName(item);
            String []arr = dbHelper.getAllProductSubCategoriesArray(categoryId);

            final Dialog dialog = new Dialog(getActivity());
            dialog.setTitle("subcategories");
            dialog.setContentView(R.layout.categories_layout);

            dialog.show();

            TextView browseBy = (TextView) dialog.findViewById(R.id.browse_by);
            browseBy.setText("");
            lv_subcategories = (ListView) dialog.findViewById(R.id.subcategories);
            lv_categories = (Spinner) dialog.findViewById(R.id.categories);

            category_list_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr);
            lv_subcategories.setAdapter(category_list_adapter);
            lv_categories.setVisibility(View.GONE);
            lv_subcategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String subCategoryName = ((TextView) view).getText().toString();
                    ProductSubCategory subCategory = dbHelper.getSubCategoryByName(subCategoryName, categoryId);

                    ArrayList<HashMap<String, String>> list = dbHelper.getProductsBySubCategory(subCategory.getId());

                    Toast.makeText(getActivity(), subCategoryName + " : " + subCategory.getName() + " : " + list.size()
                            , Toast.LENGTH_SHORT).show();
//
//                // Getting adapter by passing xml data ArrayList
                    if (list.size() > 0) {
                        products_list.clear();
                        products_list.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }
            });
        }else{
            ArrayList<HashMap<String, String>> prods = dbHelper.getProductsBySubCategory(0);
            products_list.clear();
            products_list.addAll(prods);
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

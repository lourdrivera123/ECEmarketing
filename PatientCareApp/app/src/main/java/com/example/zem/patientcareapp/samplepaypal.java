package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.ConvertCurrencyRequest;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zem on 6/16/2015.
 */
public class samplepaypal extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    RequestQueue queue;

    private static final int REQUEST_CODE_PAYMENT = 1;

    private List<PayPalItem> productsInCart = new ArrayList<PayPalItem>();

    DbHelper dbHelper;

    PayPalItem[] items;
    PayPalPaymentDetails paymentDetails;
    PayPalPayment payment;
    BigDecimal subtotal;
    BigDecimal shipping = new BigDecimal("0.0");

    // If you have tax, add it here
    BigDecimal tax = new BigDecimal("0.0");
    BigDecimal amount;

    // PayPal configuration
    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(Config.PAYPAL_ENVIRONMENT).clientId(
                    Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DbHelper(this);
        pDialog = new ProgressDialog(this);
        queue = Volley.newRequestQueue(this);

        PayPalConfiguration object = new PayPalConfiguration();
        object = object.acceptCreditCards(false);


        // Starting PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);

//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, object);
        startService(intent);

        populateProductsInCart();

        if (productsInCart.size() > 0) {
            items = new PayPalItem[productsInCart.size()];
            items = productsInCart.toArray(items);

            subtotal = PayPalItem.getItemTotal(items);

//            amount = subtotal.add(shipping).add(tax);
            HashMap<String, String> hashMap = new HashMap();

            hashMap.put("amount_in_php", String.valueOf(subtotal));
                                launchPayPalPayment();


//        ConvertCurrencyRequest.send(hashMap, new RespondListener<JSONObject>() {
//            @Override
//            public void getResult(JSONObject response) {
//                Log.d("response using interface <samplepaypal.java>", response + "");
//                try {
//                    subtotal = new BigDecimal(response.getString("amount_converted"));
//                    Log.d("amount converted", subtotal + "");
//                    launchPayPalPayment();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new ErrorListener<VolleyError>() {
//            public void getError(VolleyError error) {
//                Log.d("errror <samplepaypal>", error + "");
//                Toast.makeText(getBaseContext(), "Cannot proceed to checkout. Please check your Internet connection", Toast.LENGTH_LONG).show();
//            }
//        });

//            Log.d("subtotal in oncreate", subtotal+"");
        } else {
            Toast.makeText(getApplicationContext(), "Cart is empty! Please add few products to cart.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Launching PalPay payment activity to complete the payment
     * */
    private void launchPayPalPayment() {

        PayPalPayment thingsToBuy = prepareFinalCart();

        Intent intent = new Intent(samplepaypal.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * Receiving the PalPay payment response
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e(TAG, confirm.toJSONObject().toString(4));
                        Log.e(TAG, confirm.getPayment().toJSONObject().toString(4));

                        String paymentId = confirm.toJSONObject()
                                .getJSONObject("response").getString("id");

                        String payment_client = confirm.getPayment()
                                .toJSONObject().toString();

                        Log.e(TAG, "paymentId: " + paymentId
                                + ", payment_json: " + payment_client);

                        // Now verify the payment on the server side
                        verifyPaymentOnServer(paymentId, payment_client);

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e(TAG,
                        "An invalid Payment or PayPalConfiguration was submitted.");
            }
        }
    }

    public void populateProductsInCart() {

        ArrayList<HashMap<String, String>> items = dbHelper.getAllBasketItems();

        String name = "", sku = "";

        double price = 0;
        BigDecimal initial_price = null;
        for(HashMap<String, String> item : items){
            price = Double.parseDouble(item.get(DbHelper.PRODUCT_PRICE));
            initial_price = BigDecimal.valueOf(price);
            double quantity = Double.parseDouble(item.get(DbHelper.BASKET_QUANTITY));
            double total = price*quantity;
            name = item.get(DbHelper.PRODUCT_NAME);
            sku = item.get(DbHelper.PRODUCT_SKU);
//            TotalAmount+= total;

            PayPalItem paypal_item = new PayPalItem(name, 1, new BigDecimal(total), Config.DEFAULT_CURRENCY, sku);

            productsInCart.add(paypal_item);

            Toast.makeText(getApplicationContext(),
                    paypal_item.getName() + " added to cart!", Toast.LENGTH_SHORT).show();

        }


    }

    /**
     * Preparing final cart amount that needs to be sent to PayPal for payment
     * */
    private PayPalPayment prepareFinalCart() {

        paymentDetails = new PayPalPaymentDetails(
                shipping, subtotal, tax);

      amount = subtotal.add(shipping).add(tax);

        payment = new PayPalPayment(
                amount,
                Config.DEFAULT_CURRENCY,
                "You will be paying - ",
                Config.PAYMENT_INTENT);

        payment.items(items).paymentDetails(paymentDetails);

        return payment;

    }

    /**
     * Verifying the mobile payment on the server to avoid fraudulent payment
     * */
    private void verifyPaymentOnServer(final String paymentId,
                                       final String payment_client) {
        // Showing progress dialog before making request
        pDialog.setMessage("Verifying payment...");
        showpDialog();

        StringRequest verifyReq = new StringRequest(Request.Method.POST,
                Config.URL_VERIFY_PAYMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "verify payment: " + response.toString());

                try {
                    JSONObject res = new JSONObject(response);
                    boolean error = res.getBoolean("error");
                    String message = res.getString("message");

                    // user error boolean flag to check for errors

                    Toast.makeText(getApplicationContext(), message,
                            Toast.LENGTH_LONG).show();

                    Log.d("error sa response during verify", ""+message);

                    Log.d("gikan sa server", ""+res.toString() );

                    if (!error) {
                        // empty the cart
                        productsInCart.clear();
                    }

                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // hiding the progress dialog
                hidepDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Verify Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hiding the progress dialog
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("paymentId", paymentId);
                params.put("paymentClientJson", payment_client);

                return params;
            }
        };

        // Setting timeout to volley request as verification request takes sometime
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        verifyReq.setRetryPolicy(policy);

        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(verifyReq);
        queue.add(verifyReq);
    }
}
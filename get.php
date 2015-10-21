<?php

// array for JSON response
$response = array();
// include db connect class
require_once __DIR__ . '/db_connect.php';
// connecting to db
$db           = new DB_CONNECT();

$_cleanedGET = array();
foreach ($_GET as $key => $value) {
    $_cleanedGET[mysql_real_escape_string($key)] = mysql_real_escape_string($value);
}

$_GET = array();
$_GET = $_cleanedGET;

$request      = $_GET['q'];
$db_result    = 0;
$result       = 0;
$tbl          = isset($_GET['table']) ? $_GET['table'] : '';
date_default_timezone_set('Asia/Manila');
$datenow = date("Y-m-d H:i:s", time());
$pre_response = array(
    "success" => 1,
    "message" => ""
    );
switch ($request) {
    case 'check_if_username_exist':
        //this option is currently unused
    $username = $_GET['username'];
    $result = mysql_query("SELECT * FROM patients WHERE username = '" . $username . "' WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "patients";
    case 'get_dosages':
        // get all products from products table
    $result = mysql_query("SELECT * FROM dosage_format_and_strength WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "dosage_format_and_strength";
    break;
    case 'get_products':
        // get all products from products table
    $result = mysql_query("SELECT * FROM products WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "products";
    break;
    case 'get_doctors':
    $result = mysql_query("SELECT * FROM doctors WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "doctors";
    break;
    case 'get_patients':
    $result = mysql_query("SELECT * from patients where (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "patients";
    break;
    case 'get_clinics':
    $result = mysql_query("SELECT * FROM clinics WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "clinics";
    break;
    case 'get_clinic_doctor':
    $result = mysql_query("SELECT * FROM clinic_doctor WHERE is_active = 1") or returnError(mysql_error());
    $tbl = "clinic_doctor";
    break;
    case 'get_doctor_specialties':
    $result = mysql_query("SELECT * FROM specialties WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "specialties";
    break;
    case 'get_doctor_sub_specialties':
    $result = mysql_query("SELECT * FROM sub_specialties WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "sub_specialties";
    break;
    case 'get_product_categories':
    $result = mysql_query("SELECT * FROM product_categories WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "product_categories";
    break;
    case 'get_product_subcategories':
    if (isset($_GET['cat']) && $_GET['cat'] != "") {
        if ($_GET['cat'] == "all") {
            $result = mysql_query("SELECT * FROM product_subcategories WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
        } else {
            $result = mysql_query("SELECT * FROM product_subcategories WHERE category_id = '" . $_GET['cat'] . "' AND (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
        }
        $tbl = "product_subcategories";
    } else {
        $pre_response = array(
            "success" => 0,
            "message" => 'No category specified.'
            );
    }
    break;
    case 'get_basket_items':
    if (!isset($_GET['patient_id'])) {
        $pre_response = array(
            "success" => 0,
            "message" => 'No patient specified.'
            );
    } else {
        $patient_id = $_GET['patient_id'];
        $result     = mysql_query("SELECT * from baskets WHERE patient_id='" . $patient_id . "'");
        $tbl = "baskets";
    }
    break;
    case 'get_patient_records':
    $result = mysql_query("SELECT * FROM patient_records WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "patient_records";
    break;
    case 'get_treatments':
    $result = mysql_query("SELECT * FROM treatments WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00')") or returnError(mysql_error());
    $tbl = "treatments";
    break;

    case 'get_promo' :
    $sql = "SELECT *  FROM promo WHERE (deleted_at IS NULL OR deleted_at = '0000-00-00 00:00:00') AND start_date <= '".$datenow."' AND end_date >= '".$datenow."'";
    $result = mysql_query($sql) or returnError(mysql_error()); 
    $tbl = "promo";
    break;

    case 'get_discounts_free_products' :
    $sql = "SELECT dfp.*  FROM discounts_free_products as dfp inner join promo as p on p.id = dfp.promo_id 
    WHERE (p.deleted_at IS NULL OR p.deleted_at = '0000-00-00 00:00:00')  AND p.start_date <= '".$datenow."' AND p.end_date >= '".$datenow."'";
    $result = mysql_query($sql) or returnError(mysql_error());
    $tbl = "discounts_free_products";
    break;

    case 'get_free_products' :
    $sql = "SELECT fp.* FROM free_products as fp inner join discounts_free_products as dfp on fp.dfp_id = dfp.id inner join promo as p on dfp.promo_id = p.id
    WHERE (p.deleted_at IS NULL OR p.deleted_at = '0000-00-00 00:00:00') AND p.start_date <= '".$datenow."' AND p.end_date >= '".$datenow."'";
    $result = mysql_query($sql) or returnError(mysql_error());
    $tbl = "free_products";
    break;

    case 'get_referrals_by_user' :
    $result = mysql_query("SELECT * from patients WHERE referred_by = '".$_GET['referred_by']."' ORDER BY created_at DESC") or returnError(mysql_error());
    $tbl = "patients";
    break;


    case 'get_prescriptions' : 
    $result = mysql_query("SELECT * FROM patient_prescriptions WHERE patient_id = ".$_GET['patient_id']) or returnError(mysql_error());
    $tbl = "patient_prescriptions";
    break;

    case 'get_branches' : 
    $result = mysql_query("SELECT * FROM branches") or returnError(mysql_error());
    $tbl = "branches";
    break;

    case 'get_orders' : 
    $result = mysql_query("SELECT * FROM orders where patient_id = ".$_GET['patient_id']) or returnError(mysql_error());
    $tbl = "orders";
    break;

    case 'get_order_details' : 
    $result = mysql_query("SELECT * FROM order_details where order_id = (SELECT id from orders where patient_id = ".$_GET['patient_id'].")") or returnError(mysql_error());
    $tbl = "order_details";
    break;

    case 'get_notifications' :
    $result = mysql_query("SELECT * FROM notifications WHERE patient_ID = ".$_GET['patient_ID'].
        " AND table_name = '".$_GET['table_name']."' AND isRead = 0") or returnError(mysql_error());
    $tbl = "notifications";
    break;

    case 'get_settings' :
    $result = mysql_query("SELECT * FROM settings") or returnError(mysql_error());
    $tbl = "settings";
    break;

    case 'get_messages_by_user' :
    $result = mysql_query("SELECT * FROM messages WHERE patient_id = ".$_GET['patient_id']." order by created_at DESC") or returnError(mysql_error());
    $tbl = "messages";
    break;

    default:
        # code...
    break;
}

if ($pre_response["success"] == 0) {
    echo json_encode($pre_response);
    exit(0);
}

if ($result != 0)
    $db_result = mysql_num_rows($result);
// check for empty result
if ($db_result > 0) {
    $response[$tbl] = array();
    while ($row = mysql_fetch_assoc($result)) {
        // push single row into final response array
        foreach ($row as $key => $value) {
            // let's remove some special characters as it causes to return null when converted to json
            $row[$key] =  preg_replace('/[\x00-\x1F\x80-\xFF]/', '', $value);
        }
        array_push($response[$tbl], $row);
    }
    //get the original time from server
    date_default_timezone_set('Asia/Manila');
    $server_timestamp             = date('Y-m-d H:i:s', time());

    $result_latest_updated_at = mysql_query("SELECT * FROM ".$tbl." order by updated_at DESC limit 1") or returnError(mysql_error());
    
    if(mysql_num_rows($result_latest_updated_at) > 0){
        $result_latest_updated_at_array = mysql_fetch_assoc($result_latest_updated_at);
        $latest_updated_at = $result_latest_updated_at_array['updated_at'];
    }

    $response["success"]          = 1;
    $response["server_timestamp"] = "$server_timestamp";
    $response["latest_updated_at"] = "$latest_updated_at";
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No $tbl data found.";
}

// echo no users JSON
echo json_encode($response);

/* Custom functions */
function returnError($msg) {
    $pre_response = array(
        "success" => 0,
        "message" => $msg
        );
}

function pre($str){
 echo "<pre>";
 print_r($str);
 echo "</pre>";
}

function fetchRows($result, $tbl){
    $response[$tbl] = array();

    while ($row = mysql_fetch_assoc($result)) {
            // push single row into final response array
        foreach ($row as $key => $value) {
                // let's remove some special characters as it causes to return null when converted to json
            $row[$key] =  preg_replace('/[\x00-\x1F\x80-\xFF]/', '', $value);
        }
        array_push($response[$tbl], $row);
    }
    return $response;
}
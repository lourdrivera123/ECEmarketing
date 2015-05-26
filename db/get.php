<?php
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
$request = $_GET['q'];
$db_result = 0;
$result = 0;
$tbl = "";

$pre_response = array("success" => 1, "message" => "");
switch ($request) {
	case 'check_if_username_exist':
	//this option is currently unused
		$username = $_GET['username'];
		$result = mysql_query("SELECT * FROM patients WHERE username = '".$username."' WHERE deleted_at IS NULL") 
		or returnError(mysql_error());
		$tbl = "patients";


	case 'get_dosages':
		// get all products from products table
		$result = mysql_query("SELECT * FROM dosage_format_and_strength WHERE deleted_at IS NULL") or returnError(mysql_error());
		$tbl = "dosage_format_and_strength";
		break;

	case 'get_products':
		// get all products from products table
		$result = mysql_query("SELECT * FROM products WHERE deleted_at IS NULL") or returnError(mysql_error());
		$tbl = "products";
		break;
	
	case 'get_doctors':
		$result = mysql_query("SELECT * FROM doctors WHERE deleted_at IS NULL") or returnError(mysql_error());
		$tbl = "doctors";
		break;

	case 'get_product_categories':
		$result = mysql_query("SELECT * FROM product_categories WHERE deleted_at IS NULL") or returnError(mysql_error());
		$tbl = "product_categories";
		break;

	case 'get_product_subcategories':
		if(isset($_GET['cat']) && $_GET['cat'] != ""){
			if( $_GET['cat'] == "all" ){
				$result = mysql_query("SELECT * FROM product_subcategories WHERE deleted_at IS NULL")
			 		or returnError(mysql_error());
			}else{
				$result = mysql_query("SELECT * FROM product_subcategories WHERE category_id = '".$_GET['cat']."' AND deleted_at IS NULL")
			 		or returnError(mysql_error());
			}
			$tbl = "product_subcategories";
		}else{
			$pre_response = array("success" => 0, "message" => 'No category specified.');
		}
		
		break;


	default:
		# code...
		break;
}

if( $pre_response["success"] == 0 ){
	echo json_encode($pre_response); exit(0);
}


if( $result != 0 ) $db_result = mysql_num_rows($result);
 
// check for empty result
if ( $db_result > 0 ) {

    $response[$tbl] = array();
 
    while ($row = mysql_fetch_assoc($result)) {
 
        // push single doctor into final response array
        array_push($response[$tbl], $row);
    }
    // success
    $response["success"] = 1;
 
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No $tbl data found.";    
}

// echo no users JSON
echo json_encode($response);



/* Custom functions */
	function returnError($msg){
		$pre_response = array("success" => 0, "message" => $msg);
	}
?>
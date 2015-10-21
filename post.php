<?php
// array for JSON response
$response = array();
// include db connect class
require_once __DIR__ . '/db_connect.php';
// connecting to db
$db       = new DB_CONNECT();
$response = array();

$request  = $_POST['request'];
date_default_timezone_set('Asia/Manila');
$datenow = date("Y-m-d H:i:s", time());
$oldQty  = 0;

$_cleanedPOST = array();
foreach ($_POST as $key => $value) {
	$_cleanedPOST[mysql_real_escape_string($key)] = mysql_real_escape_string($value);
}

$_POST = array();
$_POST = $_cleanedPOST;

$basket_is_direct_update = "false";
if( isset($_POST['is_direct_update']) ){
	$basket_is_direct_update = $_POST['is_direct_update'];
	unset($_POST['is_direct_update']);
}

if ($request == 'register') {
	if (isset($_POST['cell_no'])) {
		$_POST['mobile_no'] = $_POST['cell_no'];
		unset($_POST['cell_no']);
	}
	if (isset($_POST['email'])) {
		$_POST['email_address'] = $_POST['email'];
		unset($_POST['email']);
	}
	$created_at             = date('Y-m-d H:i:s', time());
	$bdate_formated         = date('Y-m-d', strtotime($_POST['birthdate']));
	$_POST['birthdate']     = $bdate_formated;
	$_POST['created_at']    = $created_at;
    //check if uname exist
	$sql_for_checking_uname = "SELECT * FROM patients WHERE username = '" . $_POST['username'] . "'";
	$result_fetch_patient_checked_uname = mysql_query($sql_for_checking_uname) or die(mysql_error());
    // check for empty result
	if (mysql_num_rows($result_fetch_patient_checked_uname) > 0) {
		$response["success"] = 2;
		$response["message"] = "User Already Exist";
		echo json_encode($response);
		exit(0);
	} else {
		$cols    = "";
		$values  = "";
		$length  = count($_POST);
		$counter = 0;
		foreach ($_POST as $key => $value) {
			$counter++;
			if ($key != "request" && $key != "table" && $key != "action") {
				$cols .= $counter != $length ? $key . "," : $key . "";
				$values .= $counter != $length ? "'" . $value . "'," : "'" . $value . "'";
			}
		}
		$sql_insert_patient = "INSERT INTO patients(" . $cols . ") VALUES(" . $values . ")";
		$result_insert_patient = mysql_query($sql_insert_patient) or die(mysql_error());
		$last_patient_id   = mysql_insert_id();
		$sql_fetch_patient = "SELECT * FROM patients WHERE id = $last_patient_id";
		$result_fetch_patient = mysql_query($sql_fetch_patient) or die(mysql_error());
        // check for empty result
		if (mysql_num_rows($result_fetch_patient) > 0) {
			$response["patient"] = array();
			$row                 = mysql_fetch_assoc($result_fetch_patient);
			array_push($response["patient"], $row);
			$response["success"] = 1;
			echo json_encode($response);
			exit(0);
		} else {
			$response["success"] = 0;
			$response["message"] = "No patient found";
			echo json_encode($response);
			exit(0);
		}
	}
} else if ($request == 'login') {
	$username = $_POST['username'];
	$password = $_POST['password'];
	$sql      = "SELECT * FROM patients WHERE username = '$username' and password = '$password'";
	$result_fetch_patient = mysql_query($sql) or die(mysql_error());
    // check for empty result
	$response["patient"] = array();
	if (mysql_num_rows($result_fetch_patient) > 0) {
		$row = mysql_fetch_assoc($result_fetch_patient);
		array_push($response["patient"], $row);
		$response["success"] = 1;
		die(json_encode($response));

	} else {
		$response["success"] = 0;
		$response["message"] = "No patient found";
		$response["sql"] = $sql;
		die(json_encode($response));
		exit(0);
	}
} else if( $request == 'save_orders' ) {

	$user_id = $_POST['user_id'];
	$recipient_name = $_POST['recipient_name'];
	$recipient_address = $_POST['recipient_address'];
	$recipient_contactNumber = $_POST['recipient_contactNumber'];
	$branch_id = $_POST['branch_id'];
	$modeOfDelivery = $_POST['modeOfDelivery'];
	$payment_method = $_POST['payment_method'];
	$payment_status = "pending";
	$status = $_POST['status'];

	$sql = "SELECT b.*, p.*, b.id as basketID, pr.price FRom patients as p inner join baskets as b on p.id = b.patient_id inner join products as pr on b.product_id = pr.id WHERE p.id = ".$user_id." and b.is_approved = 1" ;

	$result = mysql_query($sql) or returnError(mysql_error());
	$counter = 0;
	$totalAmount = 0;
	$order_saved = false;
	$prescription_id = 0;
	if ($result != 0)
		$db_result = mysql_num_rows($result);
                // check for empty result
	if ($db_result > 0) {
		while ($row = mysql_fetch_assoc($result)) {
                    // push single row into final response array
			foreach ($row as $key => $value) {
				$quantity = $row['quantity'];
				$price = $row['price'];
				$product_id = $row['product_id'];
				$quantity = $row['quantity'];     
				$basket_id = $row['basketID'];           
				$prescription_id = $row['prescription_id'];
			}

			$totalAmount += $quantity * $price;


			if($counter == 0 ){

				$sql_orders_save = "INSERT INTO orders VALUES ('', $user_id, '$recipient_name', '$recipient_address', '$recipient_contactNumber', '', '$branch_id', '$modeOfDelivery', 'open', '$datenow', '', '')";

				if(mysql_query($sql_orders_save )){
					$order_id = mysql_insert_id(); 
					$response['order_message'] = "order saved on database";
					$order_saved = true;

				} else {
					$response["order_message"] = "Sorry, we can't process your request right now. ".mysql_error();
					echo json_encode($response);
					exit(0);
				}

			}

			$counter += 1;

			if($order_saved){
				$sql_order_details_save = "INSERT INTO order_details VALUES ('', $order_id, $product_id, $prescription_id, $quantity, 'type', 0, '$datenow', '', '')";

				if(mysql_query($sql_order_details_save)){
					$response['order_details_message_'.$counter] = "order detail saved on database";
					if(mysql_query("DELETE FROM baskets where ID=".$basket_id)){
						$response['basket_message'] = "basket/s deleted on database";
					} else {
						$response['basket_message'] = "basket/s  not deleted on database";
					}
				} else {
					$response['order_details_message_'.$counter] = "Sorry, we can't process your request right now. ".mysql_error();
					echo json_encode($response);
					exit(0);
				}   
			}
		}

		$sql_billings_save = "INSERT INTO billings VALUES ('', $order_id, $totalAmount, $totalAmount, '$payment_status', '$payment_method', '$datenow', '', '')";

		if(mysql_query($sql_billings_save)){
			$response['billing_message'] = "order saved on database";
			$response['billing_id'] = mysql_insert_id();
		} else {
			$response["billing_message"] = "Sorry, we can't process your request right now. ".mysql_error();
			echo json_encode($response);
			exit(0);
		}

		echo json_encode($response);
		exit(0);
	}

} else if ($request == "crud") {
    /**    
    * @param table: asks for table name to insert    
    **/

    // if( $_POST['table'] == "basket" )

    $action = $_POST['action'];
    $lent   = count($_POST);
    $x      = 0;
    if ($action == "insert") {
    	$cols   = "";
    	$values = "";
    	foreach ($_POST as $key => $value) {
    		if ($key != "request" && $key != "table" && $key != "action") {
    			$cols .= $key . ",";
    			$values .= "'" . $value . "',";
    		}
    		$x++;
    	}
    	$cols   = substr($cols, 0, strlen($cols) - 1);
    	$values = substr($values, 0, strlen($values) - 1);
    	$sql    = "INSERT INTO " . $_POST['table'] . "(created_at," . $cols . ") VALUES('" . $datenow . "'," . $values . ")";
    	if (mysql_query($sql)) {
    		$response["last_inserted_id"] = mysql_insert_id();
    		$response["success"]          = 1;
    	} else {
    		$response["success"] = 0;
    		$response["message"] = "Sorry, we can't process your request right now. " . mysql_error();
    	}
    } else if ($action == "update") {
    	$settings = "";
    	foreach ($_POST as $key => $value) {
    		if ($key != "request" && $key != "table" && $key != "id" && $key != "action") {
    			$settings .= $key . "='" . $value . "',";
            	// if( $_POST['table'] == "baskets" && $key == "quantity" && $basket_is_direct_update == "false" ){
            		// $settings .= $key . "= (" . $key . "+" . trim($value) . "),";
            	// }else{																																	
    			$settings .= $key . "='" . $value . "',";
            	// }
    		}
    		$x++;
    	}
    	$settings = substr($settings, 0, strlen($settings) - 1);
    	$sql      = "UPDATE " . $_POST['table'] . " SET " . $settings . ", updated_at='" . $datenow . "' WHERE id=" . $_POST['id'];
    	if (mysql_query( $sql )) {
    		$response["success"] = 1;
    	} else {
    		$response["success"] = 0;
    		$response["message"] = "Sorry, we can't process your request right now. " . mysql_error();
    		$response["query"] = $sql;
    	}
    } else if ($action == "delete") {
    	$sql = "DELETE FROM " . $_POST['table'] . " WHERE id=" . $_POST['id'];
    	if (mysql_query($sql)) {
    		$response["success"] = 1;
    	} else {
    		$response["success"] = 0;
    		$response["message"] = "Sorry, we can't process your request right now. " . mysql_error();
    	}
    } else if ($action == 'multiple_delete') {
    	$sql = "DELETE FROM ".$_POST['table']." WHERE id IN (".$_POST['serverID'].")";

    	if(mysql_query($sql)) {
    	    $response["success"] = 1;
        } else {
            $response["success"] = 0;
            $response["message"] = "Sorry, we can't process your request right now. " . mysql_error();
        }
    } else {
    	echo json_encode($response);
    	exit(0);
    }
    echo json_encode($response);
    exit(0);
}

function pre($str)
{
	echo "<pre>";
	print_r($str);
	echo "</pre>";
}
?>
<?php

// array for JSON response
$response = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$response = array();

$request = $_POST['request'];

if($request == 'register') {
	$fname = $_POST['fname'];
	$lname = $_POST['lname'];
	$mname = $_POST['mname'];
	$username = $_POST['username'];
	$password = $_POST['password'];
	$occupation = $_POST['occupation'];
	$birthdate = $_POST['birthdate'];
	$sex = $_POST['sex'];
	$civil_status = $_POST['civil_status'];
	$height = $_POST['height'];
	$weight = $_POST['weight'];
	$unit_floor_room_no = $_POST['unit_floor_room_no'];
	$building = $_POST['building'];
	$lot_no = $_POST['lot_no'];
	$block_no = $_POST['block_no'];
	$phase_no = $_POST['phase_no'];
	$address_house_no = $_POST['address_house_no'];
	$address_street = $_POST['address_street'];
	$address_barangay = $_POST['address_barangay'];
	$address_city_municipality = $_POST['address_city_municipality'];
	$address_province = $_POST['address_province'];
	$address_region = $_POST['address_region'];
	$address_zip = $_POST['address_zip'];
	$tel_no = $_POST['tel_no'];
	$cell_no = $_POST['cell_no'];
	$email = $_POST['email'];
	$photo = $_POST['photo'];

	date_default_timezone_set('Asia/Manila');
	$created_at = date('Y-m-d H:i:s', time());

	$bdate_formated = date('Y-m-d', strtotime($birthdate));


	//check if uname exist
		$sql_for_checking_uname = "SELECT * FROM patients WHERE username = '$username'";

	$result_fetch_patient_checked_uname = mysql_query($sql_for_checking_uname) or die(mysql_error());

// check for empty result
	if (mysql_num_rows($result_fetch_patient_checked_uname) > 0) {
		
		$response["success"] = 2;
		$response["message"] = "User Already Exist";
		echo json_encode($response); 

	} else {

	$sql_insert_patient = "INSERT INTO patients VALUES ('', '$fname', '$mname', '$lname', '$username', '$password','$occupation', '$bdate_formated', '$sex', '$civil_status', '$height', '$weight', $unit_floor_room_no, '$building', $lot_no, $block_no, $phase_no, $address_house_no, '$address_street', '$address_barangay', '$address_city_municipality', '$address_province', '$address_region', '$address_zip', '$tel_no', '$cell_no', '$email', '$photo', '$created_at', '', '')";

	$result_insert_patient = mysql_query($sql_insert_patient) or die(mysql_error());
	$last_patient_id = mysql_insert_id();

	$sql_fetch_patient = "SELECT * FROM patients WHERE id = $last_patient_id";

	$result_fetch_patient = mysql_query($sql_fetch_patient) or die(mysql_error());

// check for empty result
	if (mysql_num_rows($result_fetch_patient) > 0) {
		$response["patient"] = array();
		$row = mysql_fetch_assoc($result_fetch_patient);
		array_push($response["patient"], $row);

		$response["success"] = 1;
		echo json_encode($response);
	} else {

		$response["success"] = 0;
		$response["message"] = "No patient found";
		echo json_encode($response);
	}
}
} else if($request == 'login') {

	$username = $_POST['username'];
	$password = $_POST['password'];

	$sql = "SELECT * FROM patients WHERE username = '$username' and password = '$password'";

	$result_fetch_patient = mysql_query($sql) or die(mysql_error());

// check for empty result
	if (mysql_num_rows($result_fetch_patient) > 0) {
		$response["patient"] = array();
		$row = mysql_fetch_assoc($result_fetch_patient);
		array_push($response["patient"], $row);

		$response["success"] = 1;
		echo json_encode($response);
	} else {
		$response["success"] = 0;
		$response["message"] = "No patient found";
		echo json_encode($response);
	}
}


?>
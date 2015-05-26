<?php
	$dsn = 'mysql:host=sql208.0fees.us;dbname=0fe_15099609_ece_pharmacy_tree;';
	$user = '0fe_15099609';
	$password = 'trusted143';
	try
	{
		$dbh = new PDO($dsn, $user, $password);
	}
	catch (PDOException $e)
	{
		echo 'Connection failed: ' . $e->getMessage();
	}
?>
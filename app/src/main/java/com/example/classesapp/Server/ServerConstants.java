package com.example.classesapp.Server;

import android.provider.BaseColumns;


//constants used for database
public interface ServerConstants extends BaseColumns {
	//public static final String SERVER_ADDRESS = "http://erp.mdimembrane.com/HMS_API/";
//	String SERVER_ADDRESS = "http://192.51.10.101/HMS_API/";
	//String SERVER_ADDRESS = "http://116.206.99.84/HMS_API/";					//main IP
//	String SERVER_ADDRESS = "http://192.51.11.84/HMS_API/";					//main IP
//	String TEST= SERVER_ADDRESS + "testing.php";

	String SERVER_ADDRESS = "https://www.classestime.com/api/";					//main IP
	String TEST= SERVER_ADDRESS + "classesapi.php";
	
	String LOGIN= SERVER_ADDRESS + "loginfile.php";


}

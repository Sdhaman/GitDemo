package com.api.basics;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import com.api.files.Payload;
import com.api.files.ReUsableMethods;

public class Basics {

	public static void main(String[] args) throws IOException {

		
		// 1. Validate if Add Place API is working as expected
		// 2. Add Place -> Update place with new Address -> Get place to validate if new Address is present in the Response
		
		// given - All input details
		// When - Submit the API - 'Resource' and 'http methods' goes in this
		// Then - Validate the response
		// Convert the content of the file to String -> there is a method is JAVA which can read the Content of the File and 
		//                                              Convert to Bytes -> then converting the Byte Data to String
		
		
		// Set up Base URI
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		
		// log().all() - is used to log the details both input and output
		//.body(Payload.addPlace()).when().post("/maps/api/place/add/json")
		
		String response=given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\User\\Desktop\\API Testing\\AddPlace.json")))).when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
		
		// Removed log().all() from the response and store it in the String response and printing it seperately
		System.out.println(response);
		
		// Parsing JSoN 
		//JsonPath js=new JsonPath(response);
		JsonPath js=ReUsableMethods.rawToJson(response);
		String PlaceID=js.getString("place_id");
		System.out.println("PlaceID is: "+PlaceID);
		
		// Update Place
		String NewAddress="Summer Walk, Africa";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+PlaceID+"\",\r\n"
				+ "\"address\":\""+NewAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("/maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		// Get Place
		String getPlaceResponse=given().log().all().queryParam("key", "qaclick123").queryParam("place_id", PlaceID)
		.when().get("/maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath jsp=ReUsableMethods.rawToJson(getPlaceResponse);
		String actualAddress = jsp.getString("address");
		
		System.out.println("Actual Address is: "+actualAddress);
	
		// Assert 
		Assert.assertEquals(actualAddress, NewAddress);
	}

}

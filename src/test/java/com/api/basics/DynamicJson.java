package com.api.basics;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.api.files.Payload;
import com.api.files.ReUsableMethods;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {

	@Test(dataProvider="BooksData")
	public void addBook(String isbn, String aisle) {
		
		RestAssured.baseURI="http://216.10.245.166";
		
		String response = given().log().all().header("Content-Type","application/json")
		.body(Payload.addBook(isbn, aisle))
		.when().post("/Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js=ReUsableMethods.rawToJson(response);
		String id=js.get("ID");
		System.out.println("Add Book ID Generated is: "+id);
	}
	
	// DataProvider
	@DataProvider(name="BooksData")
	public Object[][] getData() {
		
		// Arrays = collection of elements
		// multidimensional Arrays = Collection of Arrays
		
		 return new Object[][] {{"aloptq","56012"},{"ghtrew","67890"},{"acfte","2134"}};
		
	}
}

package com.api.basics;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import com.api.pojo.AddPlace;
import com.api.pojo.Location;
public class SpecBuilderTest {

	public static void main(String[] args) {
		
		// Base URI
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		// Creating object of AddPlace class
		AddPlace p=new AddPlace();
		p.setAccuracy(50);
		p.setAddress("29, side layout, cohen 09");
		p.setLanguage("French-IN");
		p.setName("Frontline house");
		p.setPhone_number("(+91) 983 893 3937");
		p.setWebsite("http://google.com");
		
		// Creating ArrayList object 
		List<String> myList=new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		p.setTypes(myList);
		
		// Creating Location Class object
		Location l=new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
	
		p.setLocation(l);
		
		// Building Object of Response Specification
		ResponseSpecification ResSpec=new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		// Building object of Spec Builder
	RequestSpecification req=	new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
		.setContentType(ContentType.JSON).build();
	
	
		RequestSpecification Response=given().spec(req)
		.body(p);
		
	String Res=	Response.when().post("/maps/api/place/add/json")
		.then().spec(ResSpec).extract().response().asString();

		System.out.println("Response is: "+Res);
		
	}

}

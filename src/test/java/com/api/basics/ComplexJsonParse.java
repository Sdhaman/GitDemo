package com.api.basics;

import com.api.files.Payload;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {

		 
		JsonPath js=new JsonPath(Payload.coursePrice());
		
		// Print No of Courses returned by API
		int count=js.getInt("courses.size()");
		System.out.println("No. of Courses: "+count);

		// Print Purchase Amount
		int totalAmount = js.getInt("dashboard.purchaseAmount");
	System.out.println("Total Amount is: "+totalAmount);
	
	// Print title of the first Course
	String firstCourseTitle = js.getString("courses[0].title");
	System.out.println("First Course Title is: "+firstCourseTitle);
	
	// Print All course Titles and respective Prices
	
	for(int i=0;i<count;i++) {
		String Title= js.get("courses["+i+"].title");
		int Price=js.get("courses["+i+"].price");
		System.out.println("Title of the Course: "+Title +" And its Price is: "+Price);
		
		// Print No. of Copies sold by RPA course
		System.out.println("Print No. of Copies sold by RPA course");
		

		for(int j=0;j<count;j++) {
			String Title1= js.get("courses["+j+"].title");
			if(Title1.equalsIgnoreCase("RPA")) {
				// copies sold
				int copies = js.get("courses["+j+"].copies");
			System.out.println("No. of copies sold by RPA course is: "+copies);
			break;
			}
		}
	}
	}
	
	
	

}

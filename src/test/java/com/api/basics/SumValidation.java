package com.api.basics;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.files.Payload;

import io.restassured.path.json.JsonPath;

public class SumValidation {

	// Verify if Sum of all Course prices matches with Purchase
	@Test
		public void sumOfCourses() {
		int sum=0;
		JsonPath js=new JsonPath(Payload.coursePrice());
		int count=js.getInt("courses.size()");
		
		for(int i=0;i<count;i++) {
	
			String Title=js.getString("courses["+i+"].title");
			int price =js.getInt("courses["+i+"].price");
			int copies =js.getInt("courses["+i+"].copies");
			int Amount= price * copies;
			System.out.println("Total Amount of "+Title+" is: "+Amount);
			sum=sum+Amount;
			
		}
		System.out.println("Sum of All Course Prices is: "+sum);
		
		// Retrieving Purchase Amount
		int pAmount= js.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase Amount is: "+pAmount);
		Assert.assertEquals(sum, pAmount);
	}
}

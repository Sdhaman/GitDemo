package com.api.basics;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import com.api.pojo.Api;
import com.api.pojo.GetCourse;
import com.api.pojo.WebAutomation;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class OAuthTest {

	public static void main(String[] args) throws InterruptedException {

		String[] WACourseTitles= {"Selenium Webdriver Java","Cypress","Protractor"};
		/*
		// To invoke browser
	//	System.setProperty("webdriver.chrome.driver", "");
		WebDriverManager.chromedriver().setup();
		WebDriver driver=new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth/identifier?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&auth_url=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fv2%2Fauth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https%3A%2F%2Frahulshettyacademy.com%2FgetCourse.php&flowName=GeneralOAuthFlow");
		
		// To Enter Email ID
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("mogili.suman57");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		
		// To Enter Password
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("Sagar@1221");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);
		
		// To get the current URL
		String CurrentURL=driver.getCurrentUrl();
		*/
		
		// Manually login in and getting the URL and every time we execute the code we need to hit the URL and get the below value
		String CurrentURL="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWh5UWRy8Ufqrd68w5qsXxTpK1fOxtfs-7ZkuHoXxdhf0ReKoXSWjh9ch5k_kcNYZQ&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none"; 
		
		String partialURL=CurrentURL.split("code=")[1];
		
		// again Splitting the URL
		String requiredCode=partialURL.split("&scope")[0];
		
		
		System.out.println("Actual Required Code from URL: "+requiredCode);
		
		
		
		// TO get the Access token
		String accessTokenResponse = given().urlEncodingEnabled(false)
				.queryParams("code", requiredCode)
		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		// To retrieve the Access token from the JSON response
		JsonPath js=new JsonPath(accessTokenResponse);
		String accessToken= js.getString("access_token");
		
		System.out.println("Access Token is: "+accessToken);
		
		
		
		// 
		/*
		String Response = given().queryParam("access_token", accessToken)
		.when().log().all()
		.get("https://rahulshettyacademy.com/getCourse.php")
		.then().extract().response().asString();
		*/
		GetCourse Response = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
				.when()
				.get("https://rahulshettyacademy.com/getCourse.php")
				.then().extract().response().as(GetCourse.class);
		
	//	System.out.println("Response is: "+Response);
		
		String LinkedInValue=Response.getLinkedIn();
		System.out.println("Value stored in LinkedIn Object is: "+LinkedInValue);
		
		String InstructorValue=Response.getInstructor();
		System.out.println("Value stored in Instructor Object is: "+InstructorValue);
		
		// To get the particular course's price
		String CourseTitle=Response.getCourses().getApi().get(1).getCourseTitle();
		System.out.println("Course Title is: "+CourseTitle);
		
		// Iterating
		List<Api> ApiCourses= Response.getCourses().getApi();
				
		for(int i=0;i<ApiCourses.size();i++) {
			
			if(ApiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				String Price=ApiCourses.get(i).getPrice();
				System.out.println("Price of the required Course is: "+Price);
			}
			
		}
		
		// Get the Course Names of WebAutomation
		ArrayList<String> a=new ArrayList<String>();
		List<WebAutomation> w=Response.getCourses().getWebAutomation();
		
		for(int j=0;j<w.size();j++) {
			//String Title=w.get(j).getCourseTitle();
			//System.out.println("WebAutomation Course Title["+j+"] is: "+Title);
			
			a.add(w.get(j).getCourseTitle());
		}
		// Converting the given Array to Arraylist so that the caomparison will become easy
		List<String> expectedList=Arrays.asList(WACourseTitles);
		
		// Asserting both the values
		Assert.assertTrue(a.equals(expectedList));
	}

}

package com.api.basics;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

public class JiraTest {

	public static void main(String[] args) {
	
		// Initialising Base URI
		RestAssured.baseURI="http://localhost:8080";
		
	// Login Scenario
		
		// Session Filter
		SessionFilter session=new SessionFilter();
		
	String Response = 	given().relaxedHTTPSValidation().header("Content-Type","application/json")
		.body("{ \"username\": \"smogilis\", \"password\": \"Saregamapa@12\" }").log().all().filter(session).when()
		.post("/rest/auth/1/session").then().log().all().extract().response().asString();
		
	System.out.println("Response is: "+Response);
	
	// Add Comment
		// rest/api/2/issue/10006/comment
	String expectedMessage="Adding Comment through Automation";
	
	String AddCommentResponse=	given().pathParam("id", "10006").log().all().header("Content-Type","application/json").body("{\r\n"
				+ "    \"body\": \""+expectedMessage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat().statusCode(201)
		.extract().response().asString();

	// To retrieve the id from the response
	JsonPath js=new JsonPath(AddCommentResponse);
	String commentID = js.getString("id");
		
		// Add Attachment
		
		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("id", "10006")
		.header("Content-Type","multipart/form-data").multiPart("file", new File("Jira.txt"))
		.when().post("/rest/api/2/issue/{id}/attachments").then().log().all().assertThat().statusCode(200);

	// Get Issue Details
		String issueDetails=given().filter(session).pathParam("id", "10006")
				.queryParam("fields", "comment")
				.log().all().when().get("/rest/api/2/issue/{id}").then().log().all()
		.extract().response().asString();
		System.out.println("Response is: "+issueDetails);
	
		// To Retrieve the Comment that we added
		JsonPath js1=new JsonPath(issueDetails);
		int commentsCount= js1.getInt("fields.comment.comments.size()");
		
		for(int i=0;i<commentsCount;i++) {
			String eachcommentID=js1.get("fields.comment.comments["+i+"].id").toString();
			System.out.println("Comment["+i+"]: " +eachcommentID);
			
			// Comparing the ID
			if(eachcommentID.equalsIgnoreCase(commentID)) {
				String messageBody = js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println("MessageBody: "+messageBody);
				Assert.assertEquals(messageBody, expectedMessage);
			}
		}
		
	}

}

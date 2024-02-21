package com.herokuapp.restfulbookerassignment;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTest {
	
	protected RequestSpecification spec;
	
	//setting up the spec with the url
	@BeforeMethod
	public void setUp() {
		spec= new RequestSpecBuilder().
				setBaseUri("https://restful-booker.herokuapp.com").build();
	}
	
	//creating a booking
	protected Response createBooking() {
		
		//creating json object to send
		JSONObject body = new JSONObject();
		body.put("firstname","Vijay");
		body.put("lastname","Singhal");
		body.put("totalprice",543);
		body.put("depositpaid",false);
		
		JSONObject bookingdates = new JSONObject();
		bookingdates.put("checkin","2024-09-08");
		bookingdates.put("checkout","2024-09-10");
		body.put("bookingdates",bookingdates);
		
		
		body.put("additionalneeds","dryer");
		
		
		//sending the post request
		Response response=RestAssured.given(spec).contentType(ContentType.JSON).body(body.toString()).post("/booking");
		return response;
	}
}

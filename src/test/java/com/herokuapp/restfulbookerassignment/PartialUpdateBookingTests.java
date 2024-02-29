package com.herokuapp.restfulbookerassignment;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PartialUpdateBookingTests extends BaseTest{
	
	@Test
	public void partialUpdateBookingTest() {
		
		//getting booking id from single source
		int bookingid = SharedData.bookingid;
		
		//partially updating object
		JSONObject body = new JSONObject();
		body.put("firstname","Vijit");
		
		JSONObject bookingdates = new JSONObject();
		bookingdates.put("checkin","2024-09-10");
		bookingdates.put("checkout","2024-09-11");
		body.put("bookingdates", bookingdates);
		
		//sending patch request
		Response responseUpdate=RestAssured.given(spec).auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).body(body.toString()).patch("/booking/"+bookingid);
		responseUpdate.print();
		
		//verification of status code
		Assert.assertEquals(responseUpdate.getStatusCode(), 200,"Status code should be 200 but it is not");
		
		SoftAssert softAssert=new SoftAssert();
		
		//verifying fields
		String actualFirstName= responseUpdate.jsonPath().getString("firstname");
		softAssert.assertEquals(actualFirstName, "Vijit","firstname in response is not expected");
		
		String actualLastName= responseUpdate.jsonPath().getString("lastname");
		softAssert.assertEquals(actualLastName, "Singhal","lastname in response is not expected");
		
		int price= responseUpdate.jsonPath().getInt("totalprice");
		softAssert.assertEquals(price, 543,"totalprice in response is not expected");
		
		boolean depositPaid= responseUpdate.jsonPath().getBoolean("depositpaid");
		softAssert.assertFalse(depositPaid,"depositpaid should be false but it is not");
		
		String actualCheckin= responseUpdate.jsonPath().getString("bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2024-09-10","checkin response is not expected");
		
		String actualCheckout= responseUpdate.jsonPath().getString("bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2024-09-11","checkin response is not expected");
		
		String actualAdditionalNeeds=responseUpdate.jsonPath().getString("additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "dryer","additional needs in repsonse is not expected");
		
		softAssert.assertAll();
	}

	@Test(groups={"negativeTests"})
	public void partialUpdateBookingTestNegative() {
		// create booking explicitly and missing one field of booking dates
		RequestSpecification spec=new RequestSpecBuilder().
				setBaseUri("https://restful-booker.herokuapp.com").build();
		
		JSONObject body = new JSONObject();
		body.put("firstname","Nitin");
		
		//wrong username , it should 'admin' instead of 'user'
		Response response=RestAssured.given(spec).auth().preemptive().basic("user", "password123").contentType(ContentType.JSON).body(body.toString()).patch("/booking/1");
		response.print();
		
		// verification of status code- 403 because authentication failed
		Assert.assertEquals(response.getStatusCode(), 403, "Status code should be 500 but it is not");
	    Assert.assertTrue(response.asString().contains("Forbidden"), "Response should indicate Internal Server Error");

	}

}

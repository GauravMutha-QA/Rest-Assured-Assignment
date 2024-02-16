package com.herokuapp.restfulbookerassignment;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PartialUpdateBookingTests extends BaseTest{
	
	@Test
	public void partialUpdateBookingTest() {
		//create booking
		Response responseCreate = createBooking();
		
		responseCreate.print();
		int bookingId=responseCreate.jsonPath().getInt("bookingid");
		
		//update booking
		JSONObject body = new JSONObject();
		body.put("firstname","Olga");
		
		JSONObject bookingdates = new JSONObject();
		bookingdates.put("checkin","2020-04-25");
		bookingdates.put("checkout","2020-04-27");
		body.put("bookingdates", bookingdates);
		
		
		Response responseUpdate=RestAssured.given(spec).auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).body(body.toString()).patch("/booking/"+bookingId);
		responseUpdate.print();
		
		//verification
		Assert.assertEquals(responseUpdate.getStatusCode(), 200,"Status code should be 200 but it is not");
		
		SoftAssert softAssert=new SoftAssert();
		
		String actualFirstName= responseUpdate.jsonPath().getString("firstname");
		softAssert.assertEquals(actualFirstName, "Olga","firstname in response is not expected");
		
		String actualLastName= responseUpdate.jsonPath().getString("lastname");
		softAssert.assertEquals(actualLastName, "Shyshkin","lastname in response is not expected");
		
		int price= responseUpdate.jsonPath().getInt("totalprice");
		softAssert.assertEquals(price, 150,"totalprice in response is not expected");
		
		boolean depositPaid= responseUpdate.jsonPath().getBoolean("depositpaid");
		softAssert.assertFalse(depositPaid,"depositpaid should be false but it is not");
		
		String actualCheckin= responseUpdate.jsonPath().getString("bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2020-04-25","checkin response is not expected");
		
		String actualCheckout= responseUpdate.jsonPath().getString("bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2020-04-27","checkin response is not expected");
		
		String actualAdditionalNeeds=responseUpdate.jsonPath().getString("additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "breakfast","additional needs in repsonse is not expected");
		
		softAssert.assertAll();
	}

	

}

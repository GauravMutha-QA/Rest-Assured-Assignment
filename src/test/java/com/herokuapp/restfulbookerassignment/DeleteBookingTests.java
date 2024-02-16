package com.herokuapp.restfulbookerassignment;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DeleteBookingTests extends BaseTest{
	
	@Test
	public void partialUpdateBookingTest() {
		//create booking
		Response responseCreate = createBooking();
		
		responseCreate.print();
		int bookingId=responseCreate.jsonPath().getInt("bookingid");
		
		
		
		
		Response responseDelete=RestAssured.given(spec).auth().preemptive().basic("admin", "password123").delete("/booking/"+bookingId);
		responseDelete.print();
		
		//verification
		Assert.assertEquals(responseDelete.getStatusCode(), 201,"Status code should be 201 but it is not");
		
		Response responseGet=RestAssured.get("https://restful-booker.herokuapp.com/booking/4694");
		responseGet.print();
		
		Assert.assertEquals(responseGet.getBody().asString(), "Not Found","Body should not be 'Not Found' but it is not");
		
		
	
	}

	

}

package com.herokuapp.restfulbookerassignment;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DeleteBookingTests extends BaseTest{
	
	@Test
	public void partialUpdateBookingTest() {
		
		//getting booking id
		int bookingid = SharedData.bookingid;		
		
		
		//sending the delete request
		Response responseDelete=RestAssured.given(spec).auth().preemptive().basic("admin", "password123").delete("/booking/"+bookingid);
		responseDelete.print();
		
		//verification of status code
		Assert.assertEquals(responseDelete.getStatusCode(), 201,"Status code should be 201 but it is not");
		
		//sending get request
		Response responseGet=RestAssured.get("https://restful-booker.herokuapp.com/booking/4694");
		responseGet.print();
		
		//verifying the text response for the successful deletion
		Assert.assertEquals(responseGet.getBody().asString(), "Not Found","Body should not be 'Not Found' but it is not");
		
		
	
	}

	

}

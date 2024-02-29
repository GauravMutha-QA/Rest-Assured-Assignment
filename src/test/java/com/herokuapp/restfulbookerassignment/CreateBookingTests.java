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

public class CreateBookingTests extends BaseTest {

	@Test
	public void createBookingTest() {
		// create booking
		Response response = createBooking();
		response.print();

		int bookingid = response.jsonPath().getInt("bookingid");
		SharedData.bookingid = bookingid;

		// verification of status code
		Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200 but it is not");

		SoftAssert softAssert = new SoftAssert();

		//verification of fields
		String actualFirstName = response.jsonPath().getString("booking.firstname");
		softAssert.assertEquals(actualFirstName, "Vijay", "firstname in response is not expected");

		String actualLastName = response.jsonPath().getString("booking.lastname");
		softAssert.assertEquals(actualLastName, "Singhal", "lastname in response is not expected");

		int price = response.jsonPath().getInt("booking.totalprice");
		softAssert.assertEquals(price, 543, "totalprice in response is not expected");

		boolean depositPaid = response.jsonPath().getBoolean("booking.depositpaid");
		softAssert.assertFalse(depositPaid, "depositpaid should be false but it is not");

		String actualCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2024-09-08", "checkin response is not expected");

		String actualCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2024-09-10", "checkin response is not expected");

		String actualAdditionalNeeds = response.jsonPath().getString("booking.additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "dryer", "additional needs in repsonse is not expected");

		softAssert.assertAll();
	}
	
	@Test(groups={"negativeTests"})
	public void createBookingTestNegative() {
		// create booking explicitly and missing one field of booking dates
		RequestSpecification spec=new RequestSpecBuilder().
				setBaseUri("https://restful-booker.herokuapp.com").build();
		
		JSONObject body = new JSONObject();
		body.put("firstname","Vijay");
		body.put("lastname","Singhal");
		body.put("totalprice",543);
		body.put("depositpaid",false);
		body.put("additionalneeds","dryer");
		
		//sending request with incomplete body
		Response response=RestAssured.given(spec).contentType(ContentType.JSON).body(body.toString()).post("/booking");
		response.print();
		
		// verification of status code- 500 because server is not capable of processing such request
		Assert.assertEquals(response.getStatusCode(), 500, "Status code should be 500 but it is not");
	    Assert.assertTrue(response.asString().contains("Internal Server Error"), "Response should indicate Internal Server Error");

	}

}

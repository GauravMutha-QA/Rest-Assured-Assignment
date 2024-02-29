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

public class GetBookingTests extends BaseTest {
	@Test
	public void getBookingTest() {

		// getting booking id from single source
		int bookingid = SharedData.bookingid;
		if (bookingid == 0)
			bookingid = 1; // if no booking id generated
		// sending the get request
		Response response = RestAssured.given(spec).get("/booking/" + bookingid);
		response.print();
		// verifying status code
		Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200 but it is not");

		SoftAssert softAssert = new SoftAssert();

		// verifying fields
		String actualFirstName = response.jsonPath().getString("firstname");
		softAssert.assertEquals(actualFirstName, "Vijay", "firstname in response is not expected");

		String actualLastName = response.jsonPath().getString("lastname");
		softAssert.assertEquals(actualLastName, "Singhal", "lastname in response is not expected");

		int price = response.jsonPath().getInt("totalprice");
		softAssert.assertEquals(price, 543, "totalprice in response is not expected");

		boolean depositPaid = response.jsonPath().getBoolean("depositpaid");
		softAssert.assertFalse(depositPaid, "depositpaid should be false but it is not");

		String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2024-09-08", "checkin response is not expected");

		String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2024-09-10", "checkin response is not expected");

		String actualAdditionalNeeds = response.jsonPath().getString("additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "dryer", "additional needs in repsonse is not expected");

		softAssert.assertAll();

	}

	@Test(groups = { "negativeTests" })
	public void getBookingTestNegative() {
		// create booking explicitly and missing one field of booking dates
		RequestSpecification spec = new RequestSpecBuilder().setBaseUri("https://restful-booker.herokuapp.com").build();

		// sending the get request
		Response response = RestAssured.given(spec).get("/booking/-1");
		response.print();
	
		// verification of status code- should be 404 because the error is from sender side
		Assert.assertEquals(response.getStatusCode(), 404, "Status code should be 500 but it is not");
		Assert.assertTrue(response.asString().contains("Not Found"),
				"Response should indicate Internal Server Error");

	}
}

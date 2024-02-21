package com.herokuapp.restfulbookerassignment;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetBookingTests extends BaseTest {
	@Test
	public void getBookingTest() {

		//getting booking id from single source
		int bookingid = SharedData.bookingid;
		
		//sending the get request
		Response response = RestAssured.given(spec).get("/booking/"+bookingid);

		//verifying status code
		Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200 but it is not");

		SoftAssert softAssert = new SoftAssert();

		//verifying fields
		String actualFirstName = response.jsonPath().getString("firstname");
		softAssert.assertEquals(actualFirstName, "Eric", "firstname in response is not expected");

		String actualLastName = response.jsonPath().getString("lastname");
		softAssert.assertEquals(actualLastName, "Brown", "lastname in response is not expected");

		int price = response.jsonPath().getInt("totalprice");
		softAssert.assertEquals(price, 140, "totalprice in response is not expected");

		boolean depositPaid = response.jsonPath().getBoolean("depositpaid");
		softAssert.assertTrue(depositPaid, "depositpaid should be true but it is not");

		String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2019-05-29", "checkin response is not expected");

		String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2020-03-04", "checkin response is not expected");

		String actualAdditionalNeeds = response.jsonPath().getString("additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "Breakfast", "additional needs in repsonse is not expected");

		softAssert.assertAll();

	}
}

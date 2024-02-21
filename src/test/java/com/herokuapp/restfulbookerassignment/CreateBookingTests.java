package com.herokuapp.restfulbookerassignment;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;

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
		softAssert.assertEquals(actualFirstName, "Anuj", "firstname in response is not expected");

		String actualLastName = response.jsonPath().getString("booking.lastname");
		softAssert.assertEquals(actualLastName, "Kumar", "lastname in response is not expected");

		int price = response.jsonPath().getInt("booking.totalprice");
		softAssert.assertEquals(price, 150, "totalprice in response is not expected");

		boolean depositPaid = response.jsonPath().getBoolean("booking.depositpaid");
		softAssert.assertFalse(depositPaid, "depositpaid should be false but it is not");

		String actualCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2020-03-25", "checkin response is not expected");

		String actualCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2020-03-27", "checkin response is not expected");

		String actualAdditionalNeeds = response.jsonPath().getString("booking.additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "Breakfast", "additional needs in repsonse is not expected");

		softAssert.assertAll();
	}

}

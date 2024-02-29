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

public class UpdateBookingTests extends BaseTest {

	@Test
	public void updateBookingTest() {
		// getting booking id from single source
		int bookingid = SharedData.bookingid;

		// making an all new json object to replace
		JSONObject body = new JSONObject();
		body.put("firstname", "Nikhil");
		body.put("lastname", "Chauhan");
		body.put("totalprice", 555);
		body.put("depositpaid", true);

		JSONObject bookingdates = new JSONObject();
		bookingdates.put("checkin", "2024-03-25");
		bookingdates.put("checkout", "2024-03-27");
		body.put("bookingdates", bookingdates);

		body.put("additionalneeds", "breakfast");

		// sending the put request
		Response responseUpdate = RestAssured.given().auth().preemptive().basic("admin", "password123")
				.contentType(ContentType.JSON).body(body.toString())
				.put("https://restful-booker.herokuapp.com/booking/" + bookingid);
		responseUpdate.print();

		// verification of status code
		Assert.assertEquals(responseUpdate.getStatusCode(), 200, "Status code should be 200 but it is not");

		SoftAssert softAssert = new SoftAssert();

		// verifying fields
		String actualFirstName = responseUpdate.jsonPath().getString("firstname");
		softAssert.assertEquals(actualFirstName, "Nikhil", "firstname in response is not expected");

		String actualLastName = responseUpdate.jsonPath().getString("lastname");
		softAssert.assertEquals(actualLastName, "Chauhan", "lastname in response is not expected");

		int price = responseUpdate.jsonPath().getInt("totalprice");
		softAssert.assertEquals(price, 555, "totalprice in response is not expected");

		boolean depositPaid = responseUpdate.jsonPath().getBoolean("depositpaid");
		softAssert.assertTrue(depositPaid, "depositpaid should be true but it is not");

		String actualCheckin = responseUpdate.jsonPath().getString("bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2024-03-25", "checkin response is not expected");

		String actualCheckout = responseUpdate.jsonPath().getString("bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2024-03-27", "checkin response is not expected");

		String actualAdditionalNeeds = responseUpdate.jsonPath().getString("additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "breakfast", "additional needs in repsonse is not expected");

		softAssert.assertAll();
	}

	@Test(groups = { "negativeTests" })
	public void updateBookingTestNegative() {
		
		RequestSpecification spec = new RequestSpecBuilder().setBaseUri("https://restful-booker.herokuapp.com").build();

		// making an all new json object to replace
		JSONObject body = new JSONObject();
		body.put("firstname", "Nikhil");
		body.put("lastname", "Chauhan");
		body.put("totalprice", 555);
		body.put("depositpaid", true);

		JSONObject bookingdates = new JSONObject();
		bookingdates.put("checkin", "2024-03-25");
		bookingdates.put("checkout", "2024-03-27");
		body.put("bookingdates", bookingdates);

		body.put("additionalneeds", "breakfast");

		// sending the put request but with wrong authentication as username should be admin and not user
		Response response = RestAssured.given().auth().preemptive().basic("user", "password123")
				.contentType(ContentType.JSON).body(body.toString())
				.put("https://restful-booker.herokuapp.com/booking/1");
		response.print();

		// verification of status code- 403 because authentication failed
		Assert.assertEquals(response.getStatusCode(), 403, "Status code should be 500 but it is not");
		Assert.assertTrue(response.asString().contains("Forbidden"), "Response should indicate Internal Server Error");

	}

}

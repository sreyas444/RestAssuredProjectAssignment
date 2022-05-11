package scenario;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.testng.annotations.Test;



import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class get {

	String baseUrl="https://simple-tool-rental-api.glitch.me";
	
	
	@Test
	public void generate_token() throws ParseException {
		RestAssured.baseURI="https://simple-tool-rental-api.glitch.me";
		Header acceptheader=new Header("Accept","application/json");
		Header contentTypeheader=new Header("Content-Type","application/json");
		List<Header> headers=new ArrayList<>();
		headers.add(acceptheader);
		headers.add(contentTypeheader);
		Headers allheaders=new Headers(headers);
		
		RequestSpecification request=RestAssured.given().headers(allheaders);
		String payload="{\r\n"
				+ "   \"clientName\": \"Sreyas\",\r\n"
				+ "   \"clientEmail\": \"sreyasljj@yopmail.com\"\r\n"
				+ "}";
	
		Response generateTokenResponse=request.body(payload).post("/api-clients");
		generateTokenResponse.prettyPrint();
		String jsonString=generateTokenResponse.getBody().asString();
		String token=JsonPath.from(jsonString).get("accessToken");
		Header authHeader=new Header("Authorization",token);
		request.header(authHeader);
		String orderPayload="{\r\n"
				+ " \"toolId\": 2177,\r\n"
				+ " \"customerName\": \"Sreyas Jose\"\r\n"
				+ "}";
		Response orderResponse=request.body(orderPayload).post("/orders");
		Assert.assertEquals(201,orderResponse.getStatusCode());
		orderResponse.prettyPrint();
		JsonPath bodyEvaluator = orderResponse.jsonPath();
		Boolean createdStatus=bodyEvaluator.get("created");
		assertTrue(createdStatus);
		
		
		
	}
}

package first;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.io.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;


import static java.lang.System.out;

/**
 * httpService
 * This class has for purpose to provide all possible interaction with the API Rest from the First Server.
 */
public class httpService {
	public String apiUrl; // The Url of the First Server
	public String apiKey; // The Token of the user to contact the First Server

	/**
	 * Class Builder
	 * @param apiUrl Url of the First Server.
	 * @param apiKey User Token to contact the First Server.
	 */
	public httpService(String apiUrl, String apiKey) {
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
	}

	/**
	 * noSSLVerification allow the plugin to contact the First Server with a non valid Certificate.
	 */
	public void noSSLVerification() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {	return null;}

				public void checkClientTrusted(X509Certificate[] certs, String authType) { }

				public void checkServerTrusted(X509Certificate[] certs, String authType) { }
			}};

			// Install the all-trusting trust manager
			SSLContext sc = null;
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {	e.printStackTrace();}

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) { return true;}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	/**
	 * testConnection is calling the First Server to check if the User Token is correct.
	 * @return Return the Json replied by the First Server.
	 */
	public JSONObject testConnection() throws JSONException {
		try {
			// Set the API Rest Contact
			URL url = new URL(this.apiUrl + "test_connection/" + this.apiKey);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

			// Setup GET Request
			connection.setRequestMethod("GET");

			// Return the answer from the Server in a JSON Format
			return returnResponse(connection);
		} catch (Exception e) {
			e.printStackTrace();

			// In case of exception, return the Error JSON
			JSONObject error = new JSONObject("{\"failed\": true}");
			return error;
		}
	}

	/**
	 * checkInSample is sending a CRC32 and a MD5 Hashes to the First Server. It check its existence and in case of a new couple, create it.
	 * @param md5 MD5 Hash from the function.
	 * @param crc32 CRC32 Hash from the function.
	 * @return Return the Json replied by the First Server.
	 * @throws JSONException 
	 */
	public JSONObject checkInSample(String md5, String crc32) throws JSONException {
		try {
			// Set the API Rest Contact
			URL url = new URL(this.apiUrl + "sample/checkin/" + this.apiKey);

			// HTML body builder
			String body = "md5=" + md5 + "&crc32=" + crc32;
			HttpsURLConnection conn = this.configPost(url, body);

			// Return the answer from the Server in a JSON Format
			return returnResponse(conn);
		} catch (Exception e) {
			e.printStackTrace();
			// In case of exception, return the Error JSON
			JSONObject error = new JSONObject("{\"failed\": true}");
			return error;
		}
	}

	/**
	 * addFunctionMetadata Add a Metadata to a Sample.
	 * @param md5 MD5 Hash from the function.
	 * @param crc32 CRC32 Hash from the function.
	 * @param clientId Contains the metadata to add to the function.
	 * @return Return the Json replied by the First Server.
	 * @throws JSONException 
	 */
	public JSONObject addFunctionMetadata(String md5, String crc32, functionMetadata clientId) throws JSONException {
		try {
			// Set the API Rest Contact
			URL url = new URL(this.apiUrl + "metadata/add/" + this.apiKey);
			Charset charset = StandardCharsets.UTF_8;

			// HTML body builder
			String body = "md5=" + md5 + "&crc32=" + crc32 + "&functions=" + clientId.jsonObject.toString();
			HttpsURLConnection conn = this.configPost(url, body);

			// Return the answer from the Server in a JSON Format
			return returnResponse(conn);
		} catch (Exception e) {
			e.printStackTrace();

			// In case of exception, return the Error JSON
			JSONObject error = new JSONObject("{\"failed\": true}");
			return error;
		}
	}
	
	/**
	 * addFunctionMetadata Queries FIRST for matches.
	 * @param md5 MD5 Hash from the function.
	 * @param crc32 CRC32 Hash from the function.
	 * @param clientId Contains the metadata to scan for.
	 * @return Return the Json replied by the First Server.
	 * @throws JSONException 
	 */
	public JSONObject scanForSimilarFunction(String md5, String crc32, functionMetadata clientId) throws JSONException {
		try {
			// Set the API Rest Contact
			URL url = new URL(this.apiUrl + "metadata/scan/" + this.apiKey);
			Charset charset = StandardCharsets.UTF_8;

			// HTML body builder
			String body = "md5=" + md5 + "&crc32=" + crc32 + "&functions=" + clientId.jsonObject.toString();
			HttpsURLConnection conn = this.configPost(url, body);

			// Return the answer from the Server in a JSON Format
			return returnResponse(conn);
		} catch (Exception e) {
			e.printStackTrace();

			// In case of exception, return the Error JSON
			JSONObject error = new JSONObject("{\"failed\": true}");
			return error;
		}
	}

	/**
	 *
	 * @param url URL with the token of the First Server
	 * @param body Body to add to the post
	 * @return Return the connection to treat the server reply
	 * @throws IOException
	 */
	private HttpsURLConnection  configPost(URL url, String body) throws IOException {
		// Set the API Rest Contact
		Charset charset = StandardCharsets.UTF_8;
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		// Setup POST request
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept-Charset", charset.toString());
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset.toString());

		// Adding HTML body to the POST
		OutputStream os = conn.getOutputStream();
		os.write(body.getBytes());

		return conn;
	}

	/**
	 * returnResponse interprets the response from the firstServer and Format it in JSON.
	 * It is used in all other API request.
	 * @param connection The Http(s) connection to the first Server, after the request sent.
	 * @return Return the formatted Json replied by the First Server.
	 * @throws Exception Managed by Function calling it.
	 */
	private JSONObject returnResponse(HttpsURLConnection connection) throws IOException, JSONException {
	    // Check the HTTP Response Code from the server
		if (connection.getResponseCode() != 200) {
			// If not Succeeded, read the reply and print it
			Reader errorStream = new InputStreamReader(connection.getErrorStream());
			String err = "";
			while (true) {
				int ch = errorStream.read();
				if (ch == -1) {
					break;
				}
				err += (char) ch;
			}
			System.err.println(err);

			// Return an the Error JSON with info on the error code retrieved
			JSONObject error = new JSONObject("{\"failed\": true, \"error_code\":" + String.valueOf(connection.getResponseCode()) + "}");
			return error;
		} else {
			// If 200(=Success), read the reply and add it into a JSON Object
			Reader reader = new InputStreamReader(connection.getInputStream());
			String obj = "";
			while (true) {
				int ch = reader.read();
				if (ch == -1) {	break;}
				obj += (char) ch;
			}

			// Return the First Server Reply in JSON Format
			JSONObject result = new JSONObject(obj);
			return result;
		}
	}

	public static void main(String[] args) throws Exception {
		httpService myService = new httpService("https://louishusson.com/api/", "BFBFC6FC-4C84-4299-B2F6-7335C479810D");
		String md5 = "1b3105ada011ed1053739d9c6028b3cc";
		String crc32 = "1272189608";
		myService.noSSLVerification();
		JSONObject result = myService.checkInSample(md5, crc32);
		out.println("CheckInSample: " + result.toString());;
		List<String> apis = Arrays.asList("tutu", "tata");
		functionMetadata testFunction = new functionMetadata("02281400000a2a1e", "architecture", ".ctor-6_8746", "prototype", "comment", apis, "id");
		result = myService.addFunctionMetadata(md5, crc32/*"1b3105ada011ed1053739d9c6028bfcc", "1272189678"*/, testFunction);
		out.println("AddFunctionMetadata: " + result.toString());
		functionMetadata testScanFunction = new functionMetadata("02281400000a2a1e", "architecture", apis);
		result = myService.scanForSimilarFunction(md5, crc32, testScanFunction);
		out.println("ScanForSimilarFunction: " + result.toString());
	}
}

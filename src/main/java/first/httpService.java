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

import org.json.JSONObject;


import static java.lang.System.out;

public class httpService {

	public String apiUrl;
	public String apiKey;

	public void initiate() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}};

			// Install the all-trusting trust manager
			SSLContext sc = null;
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	public httpService(String apiUrl, String apiKey) {
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
	}

	public JSONObject testConnection() {
		try {
			URL url = new URL(this.apiUrl + "test_connection/" + this.apiKey);
			System.out.println(url.toString());
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			return returnResponse(connection);
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject error = new JSONObject("{\"status\": \"notConnected\"}");
			return error;
		}
	}

	public JSONObject checkInFirst(String md5, String crc32) throws Exception {
		try {
			URL url = new URL(this.apiUrl + "sample/checkin/" + this.apiKey);
			Charset charset = StandardCharsets.UTF_8;
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept-Charset", charset.toString());
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset.toString());
			String body = "md5=" + md5 + "&crc32=" + crc32;

			OutputStream os = conn.getOutputStream();
			os.write(body.getBytes());

			return returnResponse(conn);
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject error = new JSONObject("{\"failed\": true}");
			return error;
		}

	}

	public JSONObject addFunctionMetadataToFirst(String md5, String crc32, functionMetadata clientId) throws Exception {
		try {
			URL url = new URL(this.apiUrl + "metadata/add/" + this.apiKey);
			Charset charset = StandardCharsets.UTF_8;
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept-Charset", charset.toString());
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset.toString());

			String body = "md5=" + md5 + "&crc32=" + crc32 + "&functions=" + clientId.jsonObject.toString();

			OutputStream os = conn.getOutputStream();
			os.write(body.getBytes());

			return returnResponse(conn);

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject error = new JSONObject("{\"failed\": true}");
			return error;
		}
	}

	private JSONObject returnResponse(HttpsURLConnection connection) throws IOException {
		if (connection.getResponseCode() != 200) {
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
			JSONObject error = new JSONObject("{\"failed\": true, \"Error Code\":" + String.valueOf(connection.getResponseCode()) + "}");
			return error;
		} else {
			InputStream s = connection.getInputStream();
			Reader reader = new InputStreamReader(s);
			String obj = "";
			while (true) {
				int ch = reader.read();
				if (ch == -1) {
					break;
				}
				obj += (char) ch;
			}
			JSONObject result = new JSONObject(obj);
			return result;
		}
	}

	public static void main(String[] args) throws Exception {
		httpService myService = new httpService("https://louishusson.com/api/", "BFBFC6FC-4C84-4299-B2F6-7335C479810D");
		myService.initiate();
		JSONObject result = myService.checkInFirst("1b3105ada011ed1053739d9c6028b3cc", "1272189608");
//		if(result.getBoolean("failed")==false) {
//			out.println("Request Success");
//			out.println("checkin = " + result.get("checkin"));
//		}
//		else
//			out.println("Request failed");
		List<String> apis = Arrays.asList("tutu", "tata");
		functionMetadata testFunction = new functionMetadata("02281400000a2a1e", "architecture", ".ctor-6_8746", "prototype", "comment", apis, "id");
		JSONObject resultat = myService.addFunctionMetadataToFirst("1b3105ada011ed1053739d9c6028bfcc", "1272189678", testFunction);
		out.println(resultat.toString());
	}
}

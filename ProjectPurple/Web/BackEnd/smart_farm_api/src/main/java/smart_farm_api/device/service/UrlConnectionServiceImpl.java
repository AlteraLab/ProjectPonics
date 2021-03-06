package smart_farm_api.device.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class UrlConnectionServiceImpl {
	
	RestTemplate restTemplate;
	
	public JSONObject request(String ip, String urlPath, String reqMethod, String sendData) throws IOException {
		
		HttpURLConnection conn;
		URL url = new URL("http://" + ip + urlPath);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setRequestMethod(reqMethod);
		conn.setConnectTimeout(6000); //6초간 최대 pending
		conn.setReadTimeout(6000);

		if (sendData != null) {
			OutputStream os = conn.getOutputStream();
			os.write(sendData.getBytes("UTF-8"));
			os.close();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		conn.disconnect();
		
		System.out.println(response.toString());
		try {
			return new JSONObject(response.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}// search.php의 응답 json스트링을 파싱
	}
}

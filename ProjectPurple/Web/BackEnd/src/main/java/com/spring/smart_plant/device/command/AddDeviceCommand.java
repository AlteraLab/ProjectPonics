package com.spring.smart_plant.device.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.spring.smart_plant.DAO.DAO;
import com.spring.smart_plant.common.domain.ResultDTO;
import com.spring.smart_plant.common.utills.ConstantJwtService;
import com.spring.smart_plant.device.domain.APInfoDTO;

public class AddDeviceCommand implements DeviceCommand {
	private final String PHP_SEARCH_URL = "/search.php";
	private final String PHP_ADD_URL = "/add.php";

	@SuppressWarnings("unchecked")
	@Override
	public ResultDTO execute(Object obj) {
		// TODO Auto-generated method stub
		String publicIP = (String) obj;
		System.out.println(publicIP);
		UrlConnectionCommand cmd = new UrlConnectionCommand();
		try {
			JSONObject json = cmd.request(publicIP, PHP_SEARCH_URL, "POST", null);
			if (json.get("state").equals("OK")) {// 사용가능한 공유기인 경우
				//JWT로 부터 usercode를 알아냄
				int userCode=(int)ConstantJwtService.getJwtService().get("member").get("userCode");
				DAO dao = new DAO();
				
				//add.php로 usercode를 전송
				String requestData="{\"user_code\":\"" + userCode + "\",\"submit_ip\":\""+publicIP+"\"}";
				if ((cmd.request(publicIP, PHP_ADD_URL, "POST", requestData)).get("result")
						.equals("OK")) {
					
					dao.insertAP(new APInfoDTO(publicIP, (String) json.get("ssid"),userCode));
					
					List<Object> list = ((JSONArray) json.get("inner_ip")).toList();// 연결된 수경재배기 IP들을 가져옴
					for (Object innerIp : list) {//수경재배기들을 등록
						dao.insertSmartFarmDevice(((HashMap<String, Object>)innerIp).get("INNER_IP").toString(),userCode,  publicIP);
					}
				}
				return ResultDTO.createInstance(true).setMsg("정상적으로 등록되었습니다.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultDTO.createInstance(false).setMsg("등록 오류입니다.");
	}
}
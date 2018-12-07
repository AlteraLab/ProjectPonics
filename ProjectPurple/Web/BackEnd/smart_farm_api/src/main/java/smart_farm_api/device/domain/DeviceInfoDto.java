package smart_farm_api.device.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeviceInfoDto {
	private int apCode;
	private int sfCode;
	private int stamp;
	private String ipInfo;
}

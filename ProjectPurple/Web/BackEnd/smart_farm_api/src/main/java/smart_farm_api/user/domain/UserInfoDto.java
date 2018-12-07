package smart_farm_api.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDto {
	private String email;
	private String pwd;
	private String firstName;
	private String secondName;
}

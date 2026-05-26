package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOMe {
    private Long userId;
    private String email;
    private String displayName;
    private String authorities;
    private Boolean active;
    private Boolean mustChangePassword;
    private Long workshopId;
    private String workshopName;
}

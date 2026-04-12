package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOToken {
    private String jwtToken;
    private Long userId;
    private String authorities;
}

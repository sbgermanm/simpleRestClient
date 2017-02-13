package client;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by sgerman on 14/02/2017.
 */
public interface RestPost {
    ResponseEntity<Dato> getDatoResponseEntity(MultiValueMap<String, String> params, RestTemplate restTemplate) ;


}

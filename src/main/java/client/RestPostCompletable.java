package client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by sgerman on 14/02/2017.
 */
public class RestPostCompletable implements RestPost {
    @Override
    public ResponseEntity<Dato> getDatoResponseEntity(MultiValueMap<String, String> params, RestTemplate restTemplate) {
        return restTemplate.postForEntity("http://localhost:8080/completable", params, Dato.class);
    }
}

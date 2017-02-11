package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        RestTemplate restTemplate = new RestTemplate();
        AtomicLong counter = new AtomicLong();

//        for (int i = 0; i < 500; i++) {
//            ResponseEntity<Dato> datoResponseEntity = restTemplate.postForEntity("http://localhost:8080/dameargo", params, Dato.class);
//            log.info("[" + datoResponseEntity.getBody().toString() + "]");
//        }



        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                    params.set("name", "sebas_" + counter.getAndIncrement());

//                    ResponseEntity<Dato> datoResponseEntity = restTemplate.postForEntity("http://localhost:8080/onlySendData", params, Dato.class);

                    ResponseEntity<Dato> datoResponseEntity = restTemplate.postForEntity("http://localhost:8080/deferred", params, Dato.class);

                    log.info("[" + datoResponseEntity.getBody().toString() + "]");
                }
            });
        }
        executorService.shutdown();
    }

}

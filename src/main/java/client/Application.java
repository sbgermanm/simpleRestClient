package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static client.Application.RestType.COMPLETABLE;
import static client.Application.RestType.DEFERED;

public class Application {
    enum RestType {ONLY_SEND_DATA(new RestPostOnlySendData()), COMPLETABLE(new RestPostCompletable()), DEFERED(new RestPostDeferred());
        private final RestPost restPost;

        RestType(RestPost restPost) {
            this.restPost = restPost;
        }

        public RestPost getRestPost() {
            return restPost;
        }
    }


    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static final int N_THREADS = 50000;
    public static final int ITEMS_TO_SEND = 50000;
    static RestPost restPost =  DEFERED.getRestPost();

    public static void main(String args[]) throws InterruptedException, ExecutionException {

        Application app = new Application();
        app.execute();
    }

    public Application() {

    }


    private void execute() throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        AtomicLong counter = new AtomicLong();


        AtomicLong procesados = new AtomicLong();

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        CompletionService completionService = new ExecutorCompletionService(executorService);

        Long nanosIni = System.nanoTime();

        for (int i = 0; i < ITEMS_TO_SEND; i++) {
            completionService.submit(new Callable() {
                public String call() {
                    String value = "sebas_" + counter.getAndIncrement();
                    String resultado = "Error procesando " + value;
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                    params.set("name", value);
                    log.info("Sending " + value);
                    try {
                        ResponseEntity<Dato> datoResponseEntity = restPost.getDatoResponseEntity(params, restTemplate);
                        resultado = datoResponseEntity.getBody().toString();
                        log.info("[" + resultado + "]");
                        procesados.getAndIncrement();
                    } catch (HttpServerErrorException e) {
                        log.info("Error procesando " + value);
                        e.printStackTrace();
                    }
                    finally {
                        return resultado;
                    }
                }
            });
        }
        for (int i = 0; i < ITEMS_TO_SEND; i++) {
            try {
                completionService.take().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();

        Long nanosFin = System.nanoTime();


        log.info("procesados totales : " + procesados);
        log.info("nanos = " + (nanosFin - nanosIni));
    }


}

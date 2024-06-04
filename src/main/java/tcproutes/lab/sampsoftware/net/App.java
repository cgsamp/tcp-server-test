package tcproutes.lab.sampsoftware.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class App implements CommandLineRunner {

    //private int port = 48556;
    private int port = 8080;
    
    private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class)
            .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
            .run(args);
     }
  
    @Override
    public void run(String... args) throws Exception {

        log.info("Starting APP");
        ServerSocket server = new ServerSocket(this.port);

        while(true) {
            log.info("Listening at {} on port {}", server.getInetAddress().getHostAddress(), server.getLocalPort());
            byte[] buffer = new byte[1000];

            Socket client = server.accept();
            client.setSoTimeout(5000);
            log.info("Received connection from {}", client.getRemoteSocketAddress().toString());
            log.info(client.toString());
            log.info(server.toString());

            try{
                InputStream stream = client.getInputStream();
                int bytesRead = 0;
                boolean end = false;
                
                 bytesRead += stream.read(buffer);
                 log.info("bytesRead={}",bytesRead);
                log.info("TThe incoming request is:[{}]", new String(buffer,0,106));
 
             } catch (IOException ioe) {
                 log.error("SocketChannel write error: ", ioe);
             }
 
        }
    }
}

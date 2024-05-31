package tcproutes.lab.sampsoftware.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class App implements CommandLineRunner {

    //private int port = 48556;
    private int port = 8080;
    
    private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    class Worker implements Runnable {

        Socket client;
        byte[] buffer = new byte[1000];

        Worker(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
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


    @Override
    public void run(String... args) throws Exception {

        final ExecutorService threadPool = Executors.newFixedThreadPool(50);
        this.log.info("Starting APP");
        ServerSocket server = new ServerSocket(this.port);

        while(true) {
            this.log.info("Listening at {} on port {}", server.getInetAddress().getHostAddress(), server.getLocalPort());
            Socket client = server.accept();
            client.setSoTimeout(5000);
            this.log.info("Received connection from {}", client.getRemoteSocketAddress().toString());

            threadPool.execute(new Worker(client));

        }
    }
}

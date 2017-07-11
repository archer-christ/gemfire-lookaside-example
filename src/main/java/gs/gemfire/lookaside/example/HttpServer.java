package gs.gemfire.lookaside.example;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpServer {

    private static SlowMusicRepository repository;

    private static int serverPort;

    public HttpServer(SlowMusicRepository repository) {
        this.repository = repository;
        this.serverPort = 8080;
    }

    public HttpServer(SlowMusicRepository repository, int port) {
        this.repository = repository;
        this.serverPort = port;
    }

    public static Server setupServer() {
        Server httpServer = new Server(serverPort);

        addHandler(httpServer);
        addShutdownHook(httpServer);

        return httpServer;
    }

    private static void addHandler(Server httpServer) {
        httpServer.setHandler(new AbstractHandler() {
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                if("GET".equals(baseRequest.getMethod()) && "/music".equals(request.getRequestURI())) {

                    String songTitle = processRequest(baseRequest.getParameterValues("artist")[0]);

                    response.getOutputStream().print(songTitle);
                    response.setStatus(200);

                    baseRequest.setHandled(true);
                } else {
                    response.setStatus(404);
                    baseRequest.setHandled(true);
                }
            }
        });
    }

    private static void addShutdownHook(Server httpServer) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (httpServer.isRunning()) {
                    httpServer.stop();
                }
            } catch (Exception e) {
                System.out.println("CAN'T STOP, WON'T STOP");
            }
        }));
    }

    private static String processRequest(String artist) {
        return repository.find(artist);
    }
}

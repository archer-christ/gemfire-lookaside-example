package gs.gemfire.lookaside.example;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class App {

    private static Server httpServer;

    public static void main(String[] args) throws Exception {
        setupServer();
        addShutdownHook();

        httpServer.start();
    }

    private static void setupServer() {
        httpServer = new Server(8080);

        httpServer.setHandler(new AbstractHandler() {
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                if("GET".equals(baseRequest.getMethod()) && "/music".equals(request.getRequestURI())) {
                    response.setStatus(200);
                    baseRequest.setHandled(true);
                } else {
                    response.setStatus(404);
                    baseRequest.setHandled(true);
                }
            }
        });
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (httpServer.isRunning()) {
                    httpServer.stop();
                }
            } catch (Exception e) {
                System.out.println("CAN'T STOP WON'T STOP");
            }
        }));
    }
}
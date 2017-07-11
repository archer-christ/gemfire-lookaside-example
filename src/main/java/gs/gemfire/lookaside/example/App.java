package gs.gemfire.lookaside.example;

import org.eclipse.jetty.server.Server;

public class App {

    private static Server httpServer;

    public static void main(String[] args) throws Exception {
        httpServer = new HttpServer().setupServer();

        httpServer.start();
    }
}

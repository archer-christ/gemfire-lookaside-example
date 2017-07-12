package gs.gemfire.lookaside.example;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpServer {

    private static CacheManager cacheManager;

    private static SlowMusicRepository repository;

    private static String regionName = "music";

    private static int serverPort;

    public HttpServer(CacheManager cacheManager, SlowMusicRepository repository) {
        this.cacheManager = cacheManager;
        this.repository = repository;
        this.serverPort = 8080;
    }

    public HttpServer(CacheManager cacheManager, SlowMusicRepository repository, int port) {
        this.cacheManager = cacheManager;
        this.repository = repository;
        this.serverPort = port;
    }

    public static Server setupServer() {
        cacheManager.createCache(regionName);
        Server httpServer = new Server(serverPort);

        addHandler(httpServer);
        addShutdownHook(httpServer);

        return httpServer;
    }

    private static void addHandler(Server httpServer) {
        httpServer.setHandler(new AbstractHandler() {
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                if("GET".equals(baseRequest.getMethod()) && "/music".equals(request.getRequestURI())) {

                    String artist = getParameterValue(baseRequest, "artist");
                    if (artist != null) {
                        response.getOutputStream().print(processRequest(artist));
                        response.setStatus(200);
                    } else {
                        response.setStatus(400);
                    }

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
                cacheManager.close();
                if (httpServer.isRunning()) {
                    httpServer.stop();
                }
            } catch (Exception e) {
                System.out.println("CAN'T STOP, WON'T STOP");
            }
        }));
    }

    private static String getParameterValue(Request request, String parameter) {
        if (request.getParameterValues(parameter) != null) {
            return request.getParameterValues(parameter)[0];
        }
        return null;
    }

    private static String processRequest(String artist) {
        String songTitle = cacheManager.getFromRegion(artist);

        if (songTitle == null) {
            songTitle = getFromRepo(artist);

            if (songTitle != null) {
                cacheManager.putIntoRegion(artist, songTitle);
                return songTitle;
            } else {
                return "";
            }
        }

        return songTitle;
    }

    private static String getFromRepo(String artist) {
        return repository.find(artist);
    }
}

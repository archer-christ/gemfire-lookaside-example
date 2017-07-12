package gs.gemfire.lookaside.example;

import org.eclipse.jetty.server.Server;

public class App {

    private static Server httpServer;

    private static SlowMusicRepository repository;

    private static GemfireCacheManager cacheManager;

    public static void main(String[] args) throws Exception {
        seedRepository();
        initCacheManager();

        httpServer = new HttpServer(cacheManager, repository).setupServer();

        httpServer.start();
    }

    private static void initCacheManager() {
        cacheManager = new GemfireCacheManager();
    }

    private static void seedRepository() {
        repository = new SlowMusicRepository();

        repository.save("Beethoven", "Fifth Symphony");
        repository.save("Bach", "Brandenburg Concertos");
        repository.save("Mozart", "Symphony in F major");
    }
}

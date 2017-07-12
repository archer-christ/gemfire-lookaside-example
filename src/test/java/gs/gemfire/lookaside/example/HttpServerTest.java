package gs.gemfire.lookaside.example;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class HttpServerTest {

    private String baseUrl = "http://localhost:8080";

    private Server server;

    private StubCacheManager cacheManager;

    private SlowMusicRepository repository = mock(SlowMusicRepository.class);

    @Before
    public void setup() throws Exception {
        cacheManager = new StubCacheManager();
        server = new HttpServer(cacheManager, repository).setupServer();
        server.start();
    }

    @After
    public void teardown() throws Exception {
        server.stop();
    }

    @Test
    public void makeGetRequestToNonExistentEndpoint_receiveStatus404() throws IOException {
        URL obj = new URL(baseUrl + "/nothere");
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        assertThat(httpURLConnection.getResponseCode(), is(404));
    }

    @Test
    public void makeGetRequestToEndpointWithoutParameter_receiveStatus400() throws IOException {
        URL obj = new URL(baseUrl + "/music");
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        assertThat(httpURLConnection.getResponseCode(), is(400));
    }

    @Test
    public void makeGetRequestToEndpointWithParameter_receiveStatus200() throws IOException {
        String artist = "someone";
        when(repository.find(artist)).thenReturn("somesong");

        URL obj = new URL(baseUrl + "/music?artist=" + artist);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        assertThat(httpURLConnection.getResponseCode(), is(200));
    }

    @Test
    public void makeGetRequestToEndpointWithParameter_andArtistExists_receiveSongTitle() throws IOException {
        String artist = "someone";
        String song = "somesong";
        when(repository.find(artist)).thenReturn(song);

        URL obj = new URL(baseUrl + "/music?artist=" + artist);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        String response = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())).readLine();

        assertThat(response, is(song));
    }
}
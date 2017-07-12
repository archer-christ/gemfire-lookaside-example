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
import static org.hamcrest.core.IsEqual.equalTo;
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
    public void makeGetRequest_toNonExistentEndpoint_receiveStatus404() throws IOException {
        URL obj = new URL(baseUrl + "/nothere");
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        assertThat(httpURLConnection.getResponseCode(), is(404));
    }

    @Test
    public void makeGetRequest_toEndpointWithoutParameter_receiveStatus400() throws IOException {
        URL obj = new URL(baseUrl + "/music");
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        assertThat(httpURLConnection.getResponseCode(), is(400));
    }

    @Test
    public void makeGetRequest_andArtistExistsInCache_receiveOkAndSongTitle_withoutCallToRepository() throws IOException {
        String artist = "someone";
        String song = "somesong";

        URL obj = new URL(baseUrl + "/music?artist=" + artist);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        String response = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())).readLine();

        assertThat(httpURLConnection.getResponseCode(), is(200));
        assertThat(response, is(song));

        verify(repository, never()).find(anyString());
    }

    @Test
    public void makeGetRequest_andArtistNotInCacheButInRepo_receiveOkAndSongTitle_withCallToRepository() throws IOException {
        String artist = "someotherartist";
        String song = "somesong";

        when(repository.find(artist)).thenReturn(song);

        URL obj = new URL(baseUrl + "/music?artist=" + artist);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        String response = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())).readLine();

        assertThat(httpURLConnection.getResponseCode(), is(200));
        assertThat(response, is(song));
    }

    @Test
    public void makeGetRequest_andArtistNotInCacheButInRepo_recordIsAddedToCache() throws IOException {
        String artist = "someartist";
        String song = "somesong";

        when(repository.find(artist)).thenReturn(song);

        URL obj = new URL(baseUrl + "/music?artist=" + artist);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        assertThat(httpURLConnection.getResponseCode(), is(200));

        assertThat(cacheManager.isPutIntoRegionCalled(), is(true));
    }

    @Test
    public void makeGetRequest_andArtistNotInCacheAndNotInRepo_receiveOkAndNoSongTitle() throws IOException {
        String artist = "someartist";

        when(repository.find(artist)).thenReturn(null);

        URL obj = new URL(baseUrl + "/music?artist=" + artist);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();

        httpURLConnection.setRequestMethod("GET");

        String response = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())).readLine();

        assertThat(httpURLConnection.getResponseCode(), is(200));
        assertThat(response, equalTo(null));
    }
}
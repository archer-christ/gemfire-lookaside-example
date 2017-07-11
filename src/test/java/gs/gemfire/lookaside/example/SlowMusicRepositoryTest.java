package gs.gemfire.lookaside.example;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SlowMusicRepositoryTest {

    private SlowMusicRepository repository;

    @Before
    public void setup() {
        repository = new SlowMusicRepository();
    }

    @Test
    public void addMusicRecordToTheRepository_canGetTheNumberOfMusicRecords() {
        String artist = "Artist1";
        String songTitle = "Song1";

        assertThat(repository.numberOfRecords(), is(0));

        repository.save(artist, songTitle);

        assertThat(repository.numberOfRecords(), is(1));
    }

    @Test
    public void addMusicRecordToTheRepository_canRetrieveThatRecord() {
        String artist = "Artist1";
        String songTitle = "Song1";

        repository.save(artist, songTitle);

        assertThat(repository.find(artist), is(songTitle));
    }
}
package gs.gemfire.lookaside.example;

import java.util.HashMap;
import java.util.Map;

public class SlowMusicRepository {

    private Map<String, String> records;

    public SlowMusicRepository() {
        records = new HashMap<>();
    }

    public int numberOfRecords() {
        return records.size();
    }

    public void save(String artist, String songTitle) {
        records.put(artist, songTitle);
    }

    public String find(String artist) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return records.get(artist);
    }
}

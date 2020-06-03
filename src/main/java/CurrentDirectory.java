import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class CurrentDirectory {
    private File file;


    // get path of directory where jar was been run
    public CurrentDirectory() throws CannotReadException, TagException, InvalidAudioFrameException, IOException {
        try {
            this.file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        new SongInspector().inspect(this.file);
    }


}

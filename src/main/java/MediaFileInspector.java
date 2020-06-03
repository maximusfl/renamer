import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.TagException;

import java.io.File;
import java.io.IOException;


public abstract class MediaFileInspector {

    abstract void renameFile(File file) throws TagException, CannotReadException, InvalidAudioFrameException, IOException;

    abstract boolean isMediaFile(File file);

    abstract void inspect(File file) throws IOException, TagException, InvalidAudioFrameException, CannotReadException;


}

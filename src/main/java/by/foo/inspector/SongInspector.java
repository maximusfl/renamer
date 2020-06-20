package by.foo.inspector;

import com.google.common.collect.ImmutableSet;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class SongInspector extends MediaFileInspector {

  public SongInspector() {
    try {
      inspect(getCurrentDirectory());
    } catch (IOException | CannotReadException | InvalidAudioFrameException | TagException e) {
      e.printStackTrace();
    }
  }

  @Override
  File getCurrentDirectory() {
    Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
    return new File(String.valueOf(path));
  }

  @Override
  public boolean isMediaFile(File file) {
    boolean isSong = false;
    if (file.isFile()) {
      String FLAC = "flac";
      String MP3 = "mp3";
      String extension = FilenameUtils.getExtension(file.getName().toLowerCase());
      if ((extension).equals(MP3) || (extension).equals(FLAC)) {
        isSong = true;
      }
    }
    return isSong;
  }

  @Override
  void renameFile(File file) throws TagException, CannotReadException, InvalidAudioFrameException, IOException {
    AudioFile audioFile = AudioFileIO.read(file);
    Tag tag = audioFile.getTag().or(NullTag.INSTANCE);
    final ImmutableSet<FieldKey> supportedFields = tag.getSupportedFields();
    if (!tag.isEmpty()) {
      if (supportedFields.contains(FieldKey.TITLE)) {
        String extension = FilenameUtils.getExtension(file.getName());
        char extensionSeparator = FilenameUtils.EXTENSION_SEPARATOR;
        String ordinal = "";
        String title = tag.getValue(FieldKey.TITLE).or("");
        if (supportedFields.contains(FieldKey.TRACK)) {
          ordinal = tag.getValue(FieldKey.TRACK).or("") + " - ";
        }
        System.out.println("   file: [" + file.getName() + "] will be renamed to: [" + title + extensionSeparator + extension + "]");
        file.renameTo(new File(
            file.getParentFile().getAbsoluteFile()
                + File.separator
                + ordinal
                + title + extensionSeparator + extension));
      }
    } else System.out.println(" file: [" + file.getName() + "] hasn't tags values");
  }

  @Override
  void inspect(File folder) throws IOException, TagException, InvalidAudioFrameException, CannotReadException {
    File[] files = folder.listFiles();
    for (File foldersFile : files) {
      if (isMediaFile(foldersFile)) {
        renameFile(foldersFile);
      } else {
        inspect(foldersFile);
      }
    }
  }
}

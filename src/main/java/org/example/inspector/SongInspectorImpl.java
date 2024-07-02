package org.example.inspector;

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

public class SongInspectorImpl implements SongInspector {
  private final String FLAC = "flac";
  private final String MP3 = "mp3";

  public static File getCurrentDirectory() {
    final Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
    return new File(String.valueOf(path));
  }

  private boolean isMediaFile(final File file) {
    if (file.isFile()) {
      final String extension = FilenameUtils.getExtension(file.getName()).toLowerCase();
      return (extension.equals(MP3) || extension.equals(FLAC));
    } else {
      return false;
    }
  }

  private void renameFile(final File file) {
    AudioFile audioFile = null;

    try {
      audioFile = AudioFileIO.read(file);
    } catch (CannotReadException | InvalidAudioFrameException | TagException | IOException e) {
      e.printStackTrace();
    }
    final Tag tag = audioFile.getTag().or(NullTag.INSTANCE);

    if (!tag.isEmpty()) {
      final String newFileName = extractNameFromTag(file, tag);
      System.out.println("file: [" + file.getName() + "] will be renamed to: [" + newFileName + "]");
      file.renameTo(new File(
          file.getParentFile().getAbsoluteFile()
              + File.separator
              + newFileName));
    } else {
      System.out.println(" file: [" + file.getName() + "] hasn't tags values");
    }
  }

  @Override
  public void inspect(final File folder) {
    final File[] files = folder.listFiles();

    for (File foldersFile : files) {
      if (isMediaFile(foldersFile)) {
        renameFile(foldersFile);
      } else if (foldersFile.isDirectory()) {
        inspect(foldersFile);
      }
    }
  }

  private String extractNameFromTag(final File file, final Tag tag) {
    final StringBuilder newFileName = new StringBuilder();
    final ImmutableSet<FieldKey> supportedFields = tag.getSupportedFields();

    // building a base name without an ordinal
    if (supportedFields.contains(FieldKey.TITLE)) {
      final String extension = FilenameUtils.getExtension(file.getName()).toLowerCase();
      final char extensionSeparator = FilenameUtils.EXTENSION_SEPARATOR;
      final String title = tag.getValue(FieldKey.TITLE).or("");
      newFileName.append(title).append(extensionSeparator).append(extension);
      // add to name a track's ordinal
      if (supportedFields.contains(FieldKey.TRACK)) {
        final String ordinal = tag.getValue(FieldKey.TRACK).or("");

        if (!ordinal.isEmpty()) {
          newFileName.insert(0, ordinal).insert(ordinal.length(), " - ");
        }
      }
    }
    return newFileName.toString();
  }
}
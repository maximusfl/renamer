package org.example.inspector;

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.TagException;
import java.io.File;
import java.io.IOException;

interface SongInspector {
  void inspect(File folder);
}

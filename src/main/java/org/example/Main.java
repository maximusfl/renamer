package org.example;

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.TagException;
import org.example.inspector.SongInspectorImpl;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    SongInspectorImpl songInspectorImpl = new SongInspectorImpl();
    songInspectorImpl.inspect(SongInspectorImpl.getCurrentDirectory());
  }
}





package org.example;

import org.example.inspector.SongInspectorImpl;

public class Main {
  public static void main(String[] args) {
    SongInspectorImpl songInspectorImpl = new SongInspectorImpl();
    songInspectorImpl.inspect(SongInspectorImpl.getCurrentDirectory());
  }
}





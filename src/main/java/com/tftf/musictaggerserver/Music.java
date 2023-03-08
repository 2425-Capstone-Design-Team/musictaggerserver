package com.tftf.musictaggerserver;

public record Music(int id, String title, String album, String artist, Long duration, String path, String artUri) { }
package com.tftf.musictaggerserver;

import java.util.HashMap;

public class PlaytimeHistoryDTO {
    String email;
    int musicId;
    String tagInfo;

    public PlaytimeHistoryDTO(String email, int musicId, String tagInfo) {
        this.email = email;
        this.musicId = musicId;
        this.tagInfo = tagInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getTagInfo() {
        return tagInfo;
    }

    public void setTagInfo(String tagInfo) {
        this.tagInfo = tagInfo;
    }
}
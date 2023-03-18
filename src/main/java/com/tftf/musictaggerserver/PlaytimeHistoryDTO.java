package com.tftf.musictaggerserver;

import java.util.HashMap;

public class PlaytimeHistoryDTO {
    String emailAndMusicId;
    String tagInfo;

    public PlaytimeHistoryDTO(String emailAndMusicId, String tagInfo) {
        this.emailAndMusicId = emailAndMusicId;
        this.tagInfo = tagInfo;
    }

    public String getEmailAndMusicId() {
        return emailAndMusicId;
    }

    public void setEmailAndMusicId(String emailAndMusicId) {
        this.emailAndMusicId = emailAndMusicId;
    }

    public String getTagInfo() {
        return tagInfo;
    }

    public void setTagInfo(String tagInfo) {
        this.tagInfo = tagInfo;
    }
}
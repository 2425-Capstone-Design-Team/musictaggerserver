package com.tftf.musictaggerserver;

public class metaDTO {

    private String title;
    private String singer;
    private String year;



//    File image;

    public metaDTO(String title, String singer, String year){
        this.title=title;
        this.singer=singer;
        this.year=year;
    }

    public String getTitle(){
        return this.title;
    }
    public String getSinger(){
        return this.singer;
    }
    public String getYear(){
        return this.year;
    }
}

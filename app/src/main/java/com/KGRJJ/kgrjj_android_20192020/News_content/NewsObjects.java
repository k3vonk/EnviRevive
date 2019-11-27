package com.KGRJJ.kgrjj_android_20192020.News_content;

public class NewsObjects {

    private String title;
    private String description;
    private String author;
    private String date;
    private String previewImg;
    private String URL;

    public NewsObjects(){

    }

    public NewsObjects(String mtitle, String mdescription, String mauthor, String mdate, String mpreviewImg, String mURL){

        title = mtitle;
        description = mdescription;
        author = mauthor;
        date = mdate;
        previewImg = mpreviewImg;
        URL = mURL;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getPreviewImg() {
        return previewImg;
    }

    public String getURL() {
        return URL;
    }


}

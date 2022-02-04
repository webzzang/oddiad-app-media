package com.exflyer.oddiad.network.model;

public class ContentsDetailInfo {

    public String content_id;
    public String download_url;
    public String play_url;
    public String file_name;
    public boolean download_yn;
    public long download_id;


    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public boolean isDownload_yn() {
        return download_yn;
    }

    public void setDownload_yn(boolean download_yn) {
        this.download_yn = download_yn;
    }

    public long getDownload_id() {
        return download_id;
    }

    public void setDownload_id(long download_id) {
        this.download_id = download_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    @Override
    public String toString() {
        return "ContentsDetailInfo{" +
                "content_id='" + content_id + '\'' +
                ", download_url='" + download_url + '\'' +
                ", play_url='" + play_url + '\'' +
                ", file_name='" + file_name + '\'' +
                ", download_yn=" + download_yn +
                ", download_id=" + download_id +
                '}';
    }
}

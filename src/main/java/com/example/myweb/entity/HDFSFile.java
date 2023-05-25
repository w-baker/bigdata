package com.example.myweb.entity;

public class HDFSFile {
    private String filename;
    private String fileurl;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFiledate() {
        return filedate;
    }

    public void setFiledate(String filedate) {
        this.filedate = filedate;
    }

    public String getFilememo() {
        return filememo;
    }

    public void setFilememo(String filememo) {
        this.filememo = filememo;
    }

    private String filesize;
    private String filedate;
    private String filememo;
}

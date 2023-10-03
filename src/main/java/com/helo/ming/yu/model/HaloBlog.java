package com.helo.ming.yu.model;

import lombok.Data;

import java.util.Date;

@Data
public class HaloBlog {
    private String title; //是啥
    private String slug; //shi-sha",
    private String cover; ///upload/3.png",
    private String name; //f9a192d7-3f46-4231-97cf-964732799941
    private String content;
    private String excerpt;
    private String tag;
    private Date createDate;
    private String pageUrl;
    private String checkSum;

    private String createStatus;
    private String publishStatus;
    private String hasError;
    private String errorMsg;
}

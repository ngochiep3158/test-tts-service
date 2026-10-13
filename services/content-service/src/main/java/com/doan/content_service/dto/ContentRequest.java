package com.doan.content_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContentRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề tối đa 255 ký tự")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String body;

    @Size(max = 255, message = "Địa điểm tối đa 255 ký tự")
    private String location;

    @NotBlank(message = "Ngôn ngữ gốc không được để trống")
    @Size(min = 2, max = 10, message = "Mã ngôn ngữ phải từ 2 đến 10 ký tự")
    private String sourceLanguage = "vi";

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getLocation() { return location; }
    public String getSourceLanguage() { return sourceLanguage; }

    public void setTitle(String title) { this.title = title; }
    public void setBody(String body) { this.body = body; }
    public void setLocation(String location) { this.location = location; }
    public void setSourceLanguage(String sourceLanguage) { this.sourceLanguage = sourceLanguage; }
}

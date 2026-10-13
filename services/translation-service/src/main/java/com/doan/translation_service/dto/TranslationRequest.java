package com.doan.translation_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TranslationRequest {
    @NotBlank(message = "Văn bản cần dịch không được để trống")
    @Size(max = 20000, message = "Văn bản tối đa 20000 ký tự")
    private String text;

    @NotBlank(message = "Ngôn ngữ nguồn là bắt buộc")
    @Size(min = 2, max = 10)
    private String source;

    @NotBlank(message = "Ngôn ngữ đích là bắt buộc")
    @Size(min = 2, max = 10)
    private String target;

    public String getText() { return text; }
    public String getSource() { return source; }
    public String getTarget() { return target; }
    public void setText(String text) { this.text = text; }
    public void setSource(String source) { this.source = source; }
    public void setTarget(String target) { this.target = target; }
}

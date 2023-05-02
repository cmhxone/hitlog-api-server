package com.ivr.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hitlog {

    private String filename;
    private String content;

    public Hitlog(String content, String... keys) {

        this.content = content;
        this.filename = generateFilename(filename);
    }

    private String generateFilename(String... args) {
        StringBuilder sb = new StringBuilder();

        for (var arg : args) {
            sb.append(arg);
            sb.append("_");
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        sb.append(format.format(new Date()));

        return sb.toString();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("{filename=");
        sb.append(filename);
        sb.append(", content=");
        sb.append(content);
        sb.append("}");

        return sb.toString();
    }
}

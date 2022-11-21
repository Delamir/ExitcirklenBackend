package kea.gruppe1.exitcirklenbackend.models;

import lombok.Data;

import java.util.Map;

@Data
public class Email {

    private String to, from, subject, text, template;
    private Map<String, Object> properties;






}

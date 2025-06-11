package com.grupo12.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FAQ {
    private String request;
    private String response;

    public FAQ(String request, String response) {
        this.request = request;
        this.response = response;
    }

    public String getRequest() { return request; }
    public String getResponse() { return response; }
}

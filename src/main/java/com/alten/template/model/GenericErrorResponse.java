package com.alten.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class GenericErrorResponse {
    private String error;
    private Date date;

    public GenericErrorResponse(String error) {
        this.error = error;
        this.date = new Date();
    }

}

package com.coredumpproject.coredump;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.StringWriter;

public class Message {

    private String type;
    private String message;

    public Message() {
    }

    public Message (String type, String message){
        this.type = type;
        this.message = message;
    }

    public String toJSON() throws IOException {
        String jsonString;

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("type",this.getType()).add("message", this.getMessage());
        JsonObject jsonObject = objectBuilder.build();

        try(StringWriter writer = new StringWriter()) {
            Json.createWriter(writer).write(jsonObject);
            jsonString = writer.toString();
        }
        return jsonString;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.hacklympics.api.message;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Message {
    
    private MessageData data;
    
    public Message(String username, int messageID) {
        initMessageData(username, messageID);
    }
    
    public Message(String username, int messageID, String content) {
        //this.data = new MessageData(username, messageID, content);
    }
    
    private void initMessageData(String username, int messageID) {
        String uri = String.format("user/%s/message/%d", username, messageID);
        Response get = new Response(Utils.get(uri));
        
        if (get.success()) {
            Map<String, Object> json = get.getContent();
            
            this.data = new MessageData();
        }
    }
    
}

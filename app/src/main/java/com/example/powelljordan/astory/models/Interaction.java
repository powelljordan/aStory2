package com.example.powelljordan.astory.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jorda on 12/26/2017.
 */

public interface Interaction {
    public String getInitiator();
    public List getResponders();
    public HashMap<String, String> getTopic();
    public Content getContent();
    public String getSummary();
}

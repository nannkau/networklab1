package homework1;

import java.util.Map;

public class MapDTO {
    private String notification;
    private Map<String,String> stringMap;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Map<String, String> getStringMap() {
        return stringMap;
    }

    public void setStringMap(Map<String, String> stringMap) {
        this.stringMap = stringMap;
    }
}

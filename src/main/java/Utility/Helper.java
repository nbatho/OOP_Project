package main.java.Utility;

import java.text.SimpleDateFormat;

public class Helper {

    public String formatTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(timestamp);
    }
}

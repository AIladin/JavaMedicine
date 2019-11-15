package Adapter;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class TimePeriod {
    private Timestamp begin;
    private Timestamp end;
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;


    public TimePeriod(Timestamp begin, Timestamp end) {
        assert begin.before(end);
        this.begin = begin;
        this.end = end;
    }

    public String getBegin() {

        return formatter.format(begin.toLocalDateTime());
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return formatter.format(end.toLocalDateTime());
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public String getTime() {
        return String.valueOf(begin.getTime()) + " - " + String.valueOf(end.getTime());
    }
}



package pw.react.backend.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import pw.react.backend.utils.JsonDateDeserializer;
import pw.react.backend.utils.JsonDateSerializer;

import java.time.LocalDate;

public class ExceptionDetails {
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private final LocalDate timestamp;
    private final HttpStatus status;
    private final String errorMessage;
    private String path;

    public ExceptionDetails(HttpStatus status, String errorMessage) {
        timestamp = LocalDate.now();
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

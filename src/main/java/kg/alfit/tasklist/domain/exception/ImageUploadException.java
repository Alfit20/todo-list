package kg.alfit.tasklist.domain.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageUploadException extends RuntimeException {
    public ImageUploadException(String message) {
        super(message);
    }
}

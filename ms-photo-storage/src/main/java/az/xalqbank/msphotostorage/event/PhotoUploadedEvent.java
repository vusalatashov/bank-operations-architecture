package az.xalqbank.msphotostorage.event;

import az.xalqbank.msphotostorage.model.Photo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PhotoUploadedEvent extends ApplicationEvent {

    private final Photo photoMetadata;

    public PhotoUploadedEvent(Object source, Photo photoMetadata) {
        super(source);
        this.photoMetadata = photoMetadata;
    }
}

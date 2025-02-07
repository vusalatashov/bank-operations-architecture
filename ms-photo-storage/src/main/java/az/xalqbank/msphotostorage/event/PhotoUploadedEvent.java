package az.xalqbank.msphotostorage.event;

import az.xalqbank.msphotostorage.model.PhotoMetadata;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PhotoUploadedEvent extends ApplicationEvent {

    private final PhotoMetadata photoMetadata;

    public PhotoUploadedEvent(Object source, PhotoMetadata photoMetadata) {
        super(source);
        this.photoMetadata = photoMetadata;
    }
}

package az.xalqbank.msfileupload.event;

import az.xalqbank.msfileupload.domain.model.Photo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered after a photo is uploaded.
 */
@Getter
public class PhotoUploadedEvent extends ApplicationEvent {

    private final Photo photoMetadata;

    public PhotoUploadedEvent(Object source, Photo photoMetadata) {
        super(source);
        this.photoMetadata = photoMetadata;
    }
}

package cz.cvut.fel.bp.leisureportalbackend.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ParticipationKey implements Serializable {

    @Column(name = "participant_id")
    Integer participantId;

    @Column(name = "activity_id")
    Integer activityId;


}

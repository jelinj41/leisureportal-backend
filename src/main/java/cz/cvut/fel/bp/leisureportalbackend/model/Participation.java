package cz.cvut.fel.bp.leisureportalbackend.model;

import javax.persistence.*;

@Entity
@Table(name = "PARTICIPATION")
@NamedQueries({
        @NamedQuery(name = "Participation.findByUserAndActivity", query = "SELECT p FROM Participation p WHERE p.user = :participant AND p.activity = :activity"),
        @NamedQuery(name = "Participation.findByUser", query = "SELECT p FROM Participation p WHERE p.user = :participant")
})
public class Participation{

    @EmbeddedId
    private ParticipationKey id;

    @Id
    @GeneratedValue
    @Column(name = "PARTICIPATION_ID")
    public ParticipationKey getId() {
        return id;
    }

    public void setId(ParticipationKey id) {
        this.id = id;
    }

    @ManyToOne
    @MapsId("participantId")
    @JoinColumn(name = "participant_id")
    private User user;

    @ManyToOne
    @MapsId("activityId")
    @JoinColumn(name = "activity_Id")
    private Activity activity;

    public Participation(User participant, Activity activity) {
        this.user = participant;
        this.activity = activity;
    }

    public Participation() {
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ACTIVITY_ID")
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}

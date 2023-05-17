package cz.cvut.fel.bp.leisureportalbackend.rest;

import cz.cvut.fel.bp.leisureportalbackend.model.Participation;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import cz.cvut.fel.bp.leisureportalbackend.security.SecurityUtils;
import cz.cvut.fel.bp.leisureportalbackend.service.ActivityService;
import cz.cvut.fel.bp.leisureportalbackend.service.ParticipationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/participations")
public class ParticipationController {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipationController.class);

    private final ParticipationService participationService;
    private final ActivityService activityService;
    private final SecurityUtils securityUtils;

    @Autowired
    public ParticipationController(ParticipationService participationService, ActivityService activityService, SecurityUtils securityUtils) {
        this.participationService = participationService;
        this.activityService = activityService;
        this.securityUtils = securityUtils;
    }

    @PreAuthorize("hasAnyAuthority('USERTYPE_ADMIN', 'USERTYPE_ORGANIZER')")
    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateParticipations(@RequestBody Participation participation) {
        participationService.update(participation);
        LOG.debug("Updated activity {}.", participation);
    }

    @PreAuthorize("hasAnyAuthority('USERTYPE_ADMIN', 'USERTYPE_USER', 'USERTYPE_ORGANIZER')")
    @GetMapping(value = "/myParticipations")
    public List<Participation> myParticipations() {
        final User user = securityUtils.getCurrentUser();
        final List<Participation> participations = participationService.findByUser(user);
        return participations;
    }
}

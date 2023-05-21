package cz.cvut.fel.bp.leisureportalbackend.rest;

import cz.cvut.fel.bp.leisureportalbackend.exception.NotFoundException;
import cz.cvut.fel.bp.leisureportalbackend.exception.ValidationException;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import cz.cvut.fel.bp.leisureportalbackend.rest.util.RestUtils;
import cz.cvut.fel.bp.leisureportalbackend.security.SecurityUtils;
import cz.cvut.fel.bp.leisureportalbackend.security.model.AuthenticationToken;
import cz.cvut.fel.bp.leisureportalbackend.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for Address
 */
@RestController
@RequestMapping(value = "/rest/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressController {

    private static final Logger LOG = LoggerFactory.getLogger(AddressController.class);
    @Autowired
    private final AddressService addressService;

    private final SecurityUtils securityUtils;



    public AddressController(AddressService addressService, SecurityUtils securityUtils) {
        this.addressService = addressService;
        this.securityUtils = securityUtils;
    }

    /**
     * Returns all addresses.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Address> getAddresses() {
        return addressService.findAll();
    }


    /**
     * Adds a new address.
     *
     * @param address   The address to add.
     * @param principal The authenticated principal.
     * @return ResponseEntity with HTTP status 201 CREATED and location header.
     */
    //@PermitAll
    @PreAuthorize("hasAnyAuthority('USERTYPE_ADMIN', 'USERTYPE_ORGANIZER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addAddress(@RequestBody Address address, Principal principal) {

        final AuthenticationToken auth = (AuthenticationToken) principal;
        final User user = auth.getPrincipal().getUser();
        address.setAuthor(user);
        addressService.persist(address);
        LOG.debug("Added new address {}.", address);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", address.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Returns the address with the specified ID.
     *
     * @param id The ID of the address.
     * @return The address with the specified ID.
     * @throws NotFoundException If the address is not found.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Address getAddress(@PathVariable Integer id) throws NotFoundException {
        final Address a = addressService.find(id);
        if (a == null) {
            throw NotFoundException.create("Address", id);
        }
        return a;
    }

    /**
     * Removes an address.
     *
     * @param id The address id to be removed.
     * @return ResponseEntity with HTTP status 201 CREATED and location header.
     * @throws SQLException If there is an error parsing the address data.
     */
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @PutMapping(value = "/remove/{id}")
    public ResponseEntity<Void> removeAddress(@PathVariable Integer id) throws SQLException {
        final Address address = addressService.find(id);
        if(address!=null){
            try{
                addressService.remove(address);
                LOG.debug("Removed Team {}.", address);
                final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
                return new ResponseEntity<>(headers, HttpStatus.OK);
            }
            catch (Exception e)
            {
                LOG.error("Address is being currently used");
                final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
                return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Returns addresses in a city.
     *
     * @param loc The name of the city.
     * @return The list of addresses in the city.
     */
    @GetMapping(value = "/location/{loc}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Address> getAddressByCity(@PathVariable String loc) {
        final List<Address> c = addressService.findByCity(loc);
        return c;
    }

    /**
     * Returns addresses belonging to the currently authenticated user.
     *
     * @return The list of addresses.
     */
    @PreAuthorize("hasAnyAuthority('USERTYPE_ORGANIZER')")
    @GetMapping(value = "/myAddresses", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Address> myAddresses() {
        final User user = securityUtils.getCurrentUser();
        final List<Address> addresses = addressService.findByUser(user);
        return addresses;
    }

    /**
     * Updates an address.
     *
     * @param id       The ID of the address to update.
     * @param address The new address data.
     */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Address> update(@PathVariable("id") Integer id, @RequestBody Address address) {
        Address currentAddress = addressService.find(id);
        if (currentAddress == null) {
            return ResponseEntity.notFound().build();
        }
        currentAddress.setStreet(address.getStreet());
        currentAddress.setHouseNumber(address.getHouseNumber());
        currentAddress.setCity(address.getCity());
        currentAddress.setZipCode(address.getZipCode());
        currentAddress.setCountry(address.getCountry());
        addressService.update(currentAddress);
        return ResponseEntity.ok(currentAddress);
    }
}

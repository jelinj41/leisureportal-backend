package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.LeisureportalBackendApplication;
import cz.cvut.fel.bp.leisureportalbackend.config.PersistenceConfig;
import cz.cvut.fel.bp.leisureportalbackend.config.ServiceConfig;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, PersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional(transactionManager = "transactionManager")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan(basePackageClasses = LeisureportalBackendApplication.class)
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    Address address = new Address();

    @Before
    public void newAddress(){
        address.setId(1);
        address.setCity("Bardejov");
        address.setStreet("Slovenská");
        address.setHouseNumber(22);
        address.setZipCode("08501");
        address.setCountry("Slovensko");

        addressService.persist(address);
    }

    @Test
    public void newAddress_InDatabase() {
        Address findAddress = addressService.find(1);
        assertEquals(address,findAddress);
    }
    @Test
    public void removeAddress_NotInDatabase() {
        Address findAddress = addressService.find(1);
        assertEquals(address,findAddress);

        addressService.remove(address);
        findAddress = addressService.find(1);
        assertEquals(findAddress,null);
    }

    @Test
    public void updateAddress_ValuesChanged() {
        Address findAddress = addressService.find(1);
        assertEquals(address,findAddress);

        address.setCity("Košice");
        addressService.update(address);
        findAddress = addressService.find(1);
        assertEquals("Košice",findAddress.getCity());
    }

    @Test
    public void findByCity_AddressInCity() {
        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        List<Address> findAddress = addressService.findByCity("Bardejov");
        assertEquals(addressList,findAddress);
    }

    @Test
    public void findByCity_NoSuchCityInDatabase() {
        List<Address> empty = new ArrayList<>();
        List<Address> findAddress = addressService.findByCity("Bratislava");
        assertEquals(empty,findAddress);
    }

}

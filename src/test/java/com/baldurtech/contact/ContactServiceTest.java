package com.baldurtech.contact;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertNull;

public class ContactServiceTest {
    private Long CONTACT_ID = 1L;
    private Contact contact;
    
    @Mock
    ContactRepository contactRepository;
    
    @InjectMocks
    ContactService contactService;
    
    @Before
    public void setup() {
        contact = new Contact();
        contact.setName("ShiHang");
        contact.setMobile("15235432994");
        contact.setEmail("a@qq.com");
        contact.setVpmn("652994");
        contact.setHomeAddress("TaiYuan");
        contact.setOfficeAddress("BeiZhang");
        contact.setMemo("memo");
        contact.setJob("HR");
        contact.setJobLevel(4L);
        
        initMocks();
    }
    
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void 在ContactService中的list页面中调用ContactRepository中的findAll方法() {
        contactService.list();
        verify(contactRepository).findAll();
    }
    
    @Test
    public void 在ContactService中的show方法中调用ContactRepository中的getById方法() {
        contactService.show(CONTACT_ID);
        verify(contactRepository).getById(CONTACT_ID);
    }
    
    @Test
    public void 在ContactService中的save方法中调用ContactRepository中的save方法() {
        contactService.save(contact);
        verify(contactRepository).save(any(Contact.class));
    }
    
    @Test
    public void 当contact中的name为null时不应该调用ContactRepository的update方法() {
        contact.setName(null);
        assertNull(contactService.update(contact));
        verify(contactRepository, times(0)).update(contact);
    }
    
    @Test
    public void 当contact中的name为空白时不应该调用ContactRepository的update方法() {
        contact.setName("       ");
        assertNull(contactService.update(contact));
        verify(contactRepository, times(0)).update(contact);
    }
    
    @Test
    public void 在ContactService中的update方法中调用ContactRepository中的update方法() {
        contactService.update(contact);
        verify(contactRepository).update(any(Contact.class));
    }
    
    @Test
    public void 在ContactService中的delete方法中调用ContactRepository中的delete方法() {
        contactService.delete(CONTACT_ID);
        verify(contactRepository).delete(CONTACT_ID);
    }
}
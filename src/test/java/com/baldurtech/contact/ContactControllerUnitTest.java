package com.baldurtech.contact;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.baldurtech.account.*;


public class ContactControllerUnitTest {
    private String INVALID_CONTACT_ID = "adb";
    private Long CONTACT_ID = 1L;
    private String BLANK_ID = "    ";
    private Contact contact = new Contact();
    
    @Mock
    Principal principal;
    
    @Mock
    Account account;
    
    @Mock
    AccountRepository accountRepository;
    
    @Mock
    ContactService contactService;
    
    @Mock
    Model model;
    
    @Mock
    BindingResult result;
    
    @InjectMocks
    ContactController contactController;
    
    @Before
    public void setup() {
        initMocks();
    }
    
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void 在contactController中的list方法中将会调用ContactService中的list方法并验证返回值() {
        assertEquals("contact/list", contactController.list(model));
        verify(contactService).list();
    }
    
    @Test
    public void 在show方法中当id为null时应该重定向到list页面() {
        assertEquals("redirect:list", contactController.show(null, model));
    }
    
    @Test
    public void 在show方法中当id为空白时应该重定向到list页面() {
        assertEquals("redirect:list", contactController.show(BLANK_ID, model));
    }
    
    @Test
    public void 在show方法中id合法但调用ContactService的show方法返回null时应该重定向到list页面() {
        when(contactService.show(CONTACT_ID)).thenReturn(null);
        assertEquals("redirect:list", contactController.show(String.valueOf(CONTACT_ID), model));
    }
    
    @Test
    public void 在show方法中id存在且为Long型时应该调用contactService的show方法() {
        when(contactService.show(CONTACT_ID)).thenReturn(new Contact());
        assertEquals("contact/show", contactController.show(String.valueOf(CONTACT_ID), model));
        verify(contactService, times(2)).show(CONTACT_ID);
    }
    
    @Test
    public void 在save方法中如果页面验证有错则返回create页面() {
        when(result.hasErrors()).thenReturn(true);
        assertEquals("contact/create", contactController.save(contact, result, model));
    }
    
    @Test
    public void 在save方法中当contact不为空时将会调用contactService中的save方法并验证返回值() {
        assertEquals("redirect:show", contactController.save(contact, result, model));
        verify(contactService).save(contact);
    }
    
    @Test
    public void 在save方法中当contact为空时将会返回到create页面() {
        assertEquals("contact/create", contactController.save(null, result, model));
    }
    
    @Test
    public void 在edit方法中当id为null时应该重定向到list页面() {
        assertEquals("redirect:list", contactController.edit(null, model));
    }
    
    @Test
    public void 在edit方法中当id为空白时应该重定向到list页面() {
        assertEquals("redirect:list", contactController.edit(BLANK_ID, model));
    }
    
    @Test
    public void 在edit方法中当id合法但调用ContactService的show方法返回null时应该返回到list页面() {
        when(contactService.show(CONTACT_ID)).thenReturn(null);
        assertEquals("redirect:list", contactController.edit(String.valueOf(CONTACT_ID), model));
    }
    
    @Test
    public void 在update方法中当contact为空时重定向到list页面() {
        assertEquals("redirect:list", contactController.update(null, model));
    }

    @Test
    public void 在contactController中的update方法中contact不为空时将会调用ContactService中的update方法() {
        contactController.update(contact, model);
        verify(contactService).update(any(Contact.class));
    }
    
    @Test
    public void 在delete方法中当id为空时重定向到list页面() {
        assertEquals("null", contactController.delete(null));
    }
    
    @Test
    public void 在delete方法中当id为空白时重定向到list页面() {
        assertEquals("null", contactController.delete(BLANK_ID));
    }
    
    @Test
    public void 在contactController中的delete方法中当id不为空时将会调用ContactService中的delete方法() {
        contactController.delete(String.valueOf(CONTACT_ID));
        verify(contactService).delete(CONTACT_ID);
    }
    
    @Test
    public void 当用户是ROLE_USER时创建联系人时会重定向到forbidden页面() {
        when(account.getRole()).thenReturn("ROLE_USER");
        when(accountRepository.findByEmail(any(String.class))).thenReturn(account);
        assertEquals("contact/forbidden", contactController.create(model, principal));
    }
    
    @Test
    public void 当用户是ROLE_ADMIN时创建联系人时会重定向到create页面() {
        when(account.getRole()).thenReturn("ROLE_ADMIN");
        when(accountRepository.findByEmail(any(String.class))).thenReturn(account);
        assertEquals("contact/create", contactController.create(model, principal));
    }
    
    @Test
    public void 当用户是除ROLE_USER和ROLE_ADMIN之外的其他用户时创建联系人时会重定向到forbidden页面() {
        when(account.getRole()).thenReturn("ROLE_OTHERS");
        when(accountRepository.findByEmail(any(String.class))).thenReturn(account);
        assertEquals("contact/forbidden", contactController.create(model, principal));
    }
}
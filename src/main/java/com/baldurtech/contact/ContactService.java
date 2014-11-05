package com.baldurtech.contact;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ContactService {
    ContactRepository contactRepository;
    
    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> list() {
        return contactRepository.findAll();
    }
     
    public Contact show(Long id) {
        return contactRepository.getById(id);
    }
    
    public void save(Contact contact) {
        contactRepository.save(contact);
    }
    
    public Contact update(Contact contact) {
        if(null != contact.getName()) {
            return contactRepository.update(contact);
        } else {
            return null;
        }
    }
    
    public void delete(Long id) {
        contactRepository.delete(id);
    }
}
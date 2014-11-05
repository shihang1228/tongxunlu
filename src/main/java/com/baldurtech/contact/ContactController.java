package com.baldurtech.contact;

import java.util.List;
import java.util.ArrayList;
import javax.validation.Valid;
import java.security.Principal;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;

import com.baldurtech.account.*;

@Controller
@RequestMapping("contact")
public class ContactController {
    ContactService contactService;
    AccountRepository accountRepository;
    
    @Autowired
    public ContactController(ContactService contactService, AccountRepository accountRepository) {
        this.contactService = contactService;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "list")
    public String list(Model model) {
        
        model.addAttribute("contactList", contactService.list());
        return "contact/list";
    }
    
    @RequestMapping(value = "show")
    public String show(@RequestParam(value = "id", required = true) String id, Model model) {

        if(id == null || id.trim().length() == 0) {
            return "redirect:list";
        }
        if(null != contactService.show(Long.valueOf(id))){        
                model.addAttribute("contact", contactService.show(Long.valueOf(id)));
                return "contact/show";
        } else {
            return "redirect:list";
        }
    }   
    
    @RequestMapping(value = "create")
    public String create(Model model, Principal principal) {
        Account account = accountRepository.findByEmail(principal.getName());
        if("ROLE_USER".equals(account.getRole())) {
            return "contact/forbidden";
        }else if("ROLE_ADMIN".equals(account.getRole())) {
            model.addAttribute("contact", new Contact());
            return "contact/create";
        }else {
            return "contact/forbidden";
        }
    }
    
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("contact") Contact contact, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "contact/create";
        }
        if(contact != null) {
            contactService.save(contact);
            model.addAttribute("id", contact.getId());
            return "redirect:show";          
        }else {
            return "contact/create";
        }
              
    } 
    
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public String edit(@RequestParam("id") String id, Model model) {
        if(id == null || id.trim().length() == 0) {
            return "redirect:list";
        }
        if(null != contactService.show(Long.valueOf(id))) {
            model.addAttribute("contact", contactService.show(Long.valueOf(id)));
            return "contact/update";
        } else {
            return "redirect:list";
        }
    }
    
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@ModelAttribute("contact") Contact contact, Model model) { 
        if(contact == null) {
            return "redirect:list";
        }
        if(null != contactService.update(contact)) {
            contactService.update(contact);
            model.addAttribute("id", contact.getId());
            return "redirect:show";
        } else {
            return "redirect:list";
        }
    }
    
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(@RequestParam("id") String id) {
        if(id == null || id.trim().length() == 0) {
            return "null";
        } else {
            contactService.delete(Long.valueOf(id));
            return "redirect:list";
        }      
    }
}
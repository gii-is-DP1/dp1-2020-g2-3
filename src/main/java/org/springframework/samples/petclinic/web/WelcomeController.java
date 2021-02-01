package org.springframework.samples.petclinic.web;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WelcomeController {
	
	
	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {	    
		  List<Person> persons = new ArrayList<Person>();
			 Person person1 = new Person();
			 Person person2 = new Person();
			 Person person3 = new Person();
			 Person person4 = new Person();
			 Person person5 = new Person();
			 Person person6 = new Person();
			 
		    person1.setNombre("José Manuel");
		    person1.setApellidos("Cuevas Gallardo");
		    persons.add(person1);
		    person2.setNombre("Vicente");
		    person2.setApellidos("Díaz Correa");
		    persons.add(person2);
		    person3.setNombre("Iván");
		    person3.setApellidos("Hernández Rodriguez");
		    persons.add(person3);
		    person4.setNombre("José Antonio");
		    person4.setApellidos("Macías Portillo");
		    persons.add(person4);
		    person5.setNombre("Elena");
		    person5.setApellidos("Nold Cardona");
		    persons.add(person5);
		    person6.setNombre("Manuel");
		    person6.setApellidos("Rivas Llamas");
		    persons.add(person6);
		    
		    model.put("persons", persons);
			model.put("title", "My Project");
			model.put("group", "G2-03");
	    return "welcome";
	  }
}

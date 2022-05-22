package chernovol.cursor.hw11.controller;

import chernovol.cursor.hw11.model.Person;
import chernovol.cursor.hw11.model.PersonForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

@Controller
@EnableWebMvc
public class PersonController {
    private static List<Person> persons = new ArrayList<>();

    static {
        persons.add(new Person(1, "Bill", "Gates", 50));
        persons.add(new Person(2, "Steve", "Jobs", 45));
        persons.add(new Person(3, "John", "Snow", 24));
    }

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("m", "Hello Everybody!");
        model.addAttribute("m2", "Let`s look at this HW");

        return "index";
    }

    @RequestMapping(value = {"/personList"}, method = RequestMethod.GET)
    public String personList(Model model) {

        model.addAttribute("persons", persons);

        return "personList";
    }

    @RequestMapping(value = {"/addPerson"}, method = RequestMethod.GET)
    public String showAddPersonPage(Model model) {

        PersonForm personForm = new PersonForm();
        model.addAttribute("personForm", personForm);

        return "addPerson";
    }

    @RequestMapping(value = {"/addPerson"}, method = RequestMethod.POST)
    public String savePerson(Model model, //
                             @ModelAttribute("personForm") PersonForm personForm) {
        boolean found = persons.stream().anyMatch(p -> p.getId().equals(personForm.getId()));

        if (found) {
            model.addAttribute("errorMessage", "Maybe id is taken. First an Last name are required also.");
            return "addPerson";
        } else {
            Integer id = personForm.getId();
            String firstName = personForm.getFirstName();
            String lastName = personForm.getLastName();
            int age = personForm.getAge();

            if (id > 0 && firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0) {
                Person newPerson = new Person(id, firstName, lastName, age);
                persons.add(newPerson);

                return "redirect:/personList";
            }
        }
        return "addPerson";
    }

    @RequestMapping(value = {"/updatePerson/{id}"}, method = RequestMethod.GET)
    public String showEditPersonPage(Model model, @PathVariable Integer id) {
        Person person = persons.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        PersonForm personForm = new PersonForm();

        personForm.setId(person.getId());
        personForm.setFirstName(person.getFirstName());
        personForm.setLastName(person.getLastName());
        personForm.setAge(person.getAge());
        model.addAttribute("personForm", personForm);

        model.addAttribute("personForm", personForm);
        return "updatePerson";
    }

    @RequestMapping(value = "updatePerson")
    public String updatePerson(Model model, @ModelAttribute("personForm") PersonForm personForm) {
        Person person = persons.stream().filter(p -> p.getId() == personForm.getId()).findFirst().orElse(null);

        Integer id = personForm.getId();
        String firstName = personForm.getFirstName();
        String lastName = personForm.getLastName();
        int age = personForm.getAge();

        if (firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0 && age > 0) {

            Person newPerson = new Person(id, firstName, lastName, age);
            int personId = persons.indexOf(person);
            persons.set(personId, newPerson);

            return "redirect:/personList";
        }
        model.addAttribute("errorMessage", "Error");
        return "updatePerson";
    }

    @RequestMapping(value = "deletePerson/{id}", method = RequestMethod.GET)
    public String deletePerson(@PathVariable("id") Integer id) {
        Person person = persons.stream().filter(p -> p.getId() == id).findAny().orElse(null);
        persons.remove(person);
        return "redirect:/personList";
    }
}

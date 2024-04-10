import generated.ChildrenType;
import generated.ParentsType;
import generated.PersonRef;
import generated.PersonType;
import generated.SiblingsType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, XMLStreamException {
        People people = Saver.getPeople();
        if (people == null) {
            try (InputStream inputStream = new FileInputStream("people.xml")) {
                people = Parser.parse(inputStream);
            }
            people.checkData();
            people.dataCorrection();
            Saver.savePeople(people);
        }
        generated.People peopleXML = new generated.People();

        for(Person p : people.getPeople()){
            PersonType personType = new PersonType();

            personType.setId(p.getId());
            personType.setName(p.getFullName());
            personType.setGender(p.getGender());

            peopleXML.getPerson().add(personType);
        }

        for(PersonType personType: peopleXML.getPerson()){
            Person person = people.getPerson(personType.getId());
            PersonRef spouse = new PersonRef();
            if(person.getSpouse() != null){
                spouse.setId(peopleXML.getPerson(person.getSpouse().getId()));
                spouse.setName(person.getSpouse().getFullName());
                if (Objects.equals(person.getGender(), "M")){
                    personType.setWife(spouse);
                }
                else if(Objects.equals(person.getGender(), "F")){
                    personType.setHusband(spouse);
                }
            }

            ParentsType parentsType = new ParentsType();
            if(person.getFather() != null){
                PersonRef father = new PersonRef();
                father.setId(peopleXML.getPerson(person.getFather().getId()));
                father.setName(person.getFather().getFullName());
                parentsType.setFather(father);
            }
            if(person.getMother() != null){
                PersonRef mother = new PersonRef();
                mother.setId(peopleXML.getPerson(person.getMother().getId()));
                mother.setName(person.getMother().getFullName());
                parentsType.setMother(mother);
            }
            personType.getParents().add(parentsType);

            SiblingsType siblingsType = new SiblingsType();
            for(PersonInfo sisterInfo : person.getSisters()){
                PersonRef sister = new PersonRef();
                sister.setId(peopleXML.getPerson(sisterInfo.getId()));
                sister.setName(sisterInfo.getFullName());
                siblingsType.getSister().add(sister);
            }
            for (PersonInfo brotherInfo : person.getBrothers()){
                PersonRef brother = new PersonRef();
                brother.setId(peopleXML.getPerson(brotherInfo.getId()));
                brother.setName(brotherInfo.getFullName());
                siblingsType.getBrother().add(brother);
            }
            personType.getSiblings().add(siblingsType);

            ChildrenType childrenType = new ChildrenType();
            for (PersonInfo daughterInfo : person.getDaughters()){
                PersonRef daughter = new PersonRef();
                daughter.setId(peopleXML.getPerson(daughterInfo.getId()));
                daughter.setName(daughterInfo.getFullName());
                childrenType.getDaughter().add(daughter);
            }
            for (PersonInfo sonInfo : person.getSons()){
                PersonRef son = new PersonRef();
                son.setId(peopleXML.getPerson(sonInfo.getId()));
                son.setName(sonInfo.getFullName());
                childrenType.getSon().add(son);
            }
            personType.getChildren().add(childrenType);
        }

        try {
            JAXBContext jc = null;
            ClassLoader classLoader = People.class.getClassLoader();
            jc = JAXBContext.newInstance("generated", classLoader);
            Marshaller writer = jc.createMarshaller();

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File schemaFile = new File("schema.xsd");
            writer.setSchema(schemaFactory.newSchema(schemaFile));
            writer.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            writer.marshal(peopleXML, new File("output.xml"));
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
        
    }
}


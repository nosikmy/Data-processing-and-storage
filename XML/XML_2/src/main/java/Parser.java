import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Parser {
    public static People parse(InputStream stream) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(stream);
        String lastElement = null;


        People people = null;
        Person person = null;
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                String elementName = startElement.getName().toString().trim();
                switch (elementName) {

                    case "people" -> {
                        Attribute attribute = startElement.getAttributeByName(new QName("count"));
                        people = new People(Integer.parseInt(attribute.getValue()));
                        lastElement = elementName;
                    }
                    case "person" -> {
                        person = new Person();
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            switch (attribute.getName().toString()) {
                                case "id" -> {
                                    person.setId(attribute.getValue());
                                }
                                case "name" -> {
                                    String[] fullName = attribute.getValue().trim().split("\\s+");
                                    person.setFirstName(fullName[0]);
                                    person.setLastName(fullName[1]);
                                    person.setFullName(fullName[0] + " " + fullName[1]);
                                }
                                default -> {
                                    System.out.println("NEW ATTRIBUTE " + attribute.getName() + " in " + attribute.getName().toString());
                                    return null;
                                }
                            }
                        }
                        lastElement = elementName;
                    }
                    case "mother", "fullname", "first", "family", "father", "children", "brother", "family-name", "child", "sister" -> {
                        lastElement = elementName;
                    }
                    case "surname", "firstname", "gender", "children-number", "parent", "siblings-number", "siblings", "son", "wife", "spouce", "daughter", "id", "husband" -> {
                        assert person != null;
                        assert people != null;
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            String val = attribute.getValue().trim();
                            switch (attribute.getName().toString().trim()) {
                                case "value" -> {
                                    switch (elementName) {
                                        case "surname" -> person.setLastName(val);
                                        case "firstname" -> person.setFirstName(val);
                                        case "gender" -> {
                                            switch (val) {
                                                case "F", "female" -> person.setGender("F");
                                                case "M", "male" -> person.setGender("M");
                                                default -> {
                                                    System.out.println("new gender label1 " + val);
                                                    return null;
                                                }
                                            }
                                        }
                                        case "children-number" -> person.setChildrenNumber(Integer.parseInt(val));
                                        case "siblings-number" -> person.setSiblingsNumber(Integer.parseInt(val));
                                        case "parent" -> {
                                            if (!val.equals("UNKNOWN")) {
                                                person.getParentsId().add(val);
                                            }
                                        }
                                        case "wife" -> {
                                            person.setSpouseId(val);
                                            person.setWifeId(val);
                                            person.setGender("M");
                                        }
                                        case "spouce" -> {
                                            if (Objects.equals(val, "NONE")) {
                                                continue;
                                            }
                                            if (val.charAt(1) >= '0' && val.charAt(1) <= '9') {
                                                person.setSpouseId(val);
                                            } else if (person.getSpouseName() == null) {
                                                String[] fullName = val.trim().split("\\s+");
                                                person.setSpouseName(fullName[0] + ' ' + fullName[1]);
                                            }
                                        }
                                        case "husband" -> {
                                            person.setSpouseId(val);
                                            person.setHusbandId(val);
                                            person.setGender("F");
                                        }
                                        case "id" -> person.setId(val);
                                        default ->
                                                System.out.println("NEW ELEMENT " + elementName + " with attribute " + attribute.getName());
                                    }

                                }
                                case "val" -> {
                                    if ("siblings".equals(elementName)) {
                                        String[] siblings = val.trim().split("\\s+");
                                        person.getSiblingsId().addAll(List.of(siblings));
                                    } else {
                                        System.out.println("NEW ELEMENT " + elementName + " with attribute " + attribute.getName());
                                    }
                                }
                                case "id" -> {
                                    switch (elementName) {
                                        case "son" -> {
                                            person.getChildrenId().add(val);
                                            person.getSonsId().add(val);
                                        }
                                        case "daughter" -> {
                                            person.getChildrenId().add(val);
                                            person.getDaughtersId().add(val);
                                        }
                                        default ->
                                                System.out.println("NEW ELEMENT " + elementName + " with attribute " + attribute);
                                    }
                                }
                                default -> {
                                    System.out.println("NEW ATTRIBUTE " + attribute + " in " + elementName);
                                    return null;
                                }
                            }
                        }
                        lastElement = elementName;
                    }
                    default -> {
                        System.out.println("NEW START ELEMENT " + startElement);
                        return null;
                    }
                }
            } else if (nextEvent.isCharacters()) {
                String characters = nextEvent.asCharacters().toString().trim();
                if (characters.isEmpty()) {
                    continue;
                }

                assert person != null;
                switch (lastElement) {
                    case "mother" -> {
                        String[] fullName = characters.split("\\s+");
                        person.getParentsNames().add(fullName[0] + ' ' + fullName[1]);
                        person.setMotherName(fullName[0] + ' ' + fullName[1]);
                    }
                    case "father" -> {
                        String[] fullName = characters.split("\\s+");
                        person.getParentsNames().add(fullName[0] + ' ' + fullName[1]);
                        person.setFatherName(fullName[0] + ' ' + fullName[1]);
                    }
                    case "first", "firstname" -> {
                        person.setFirstName(characters);
                    }
                    case "family", "family-name" -> person.setLastName(characters);
                    case "gender" -> {
                        switch (characters) {
                            case "F" -> person.setGender("F");
                            case "M" -> person.setGender("M");
                            default -> {
                                System.out.println("new gender label " + characters);
                                return null;
                            }
                        }
                    }
                    case "parent" -> {
                        if (!characters.equals("UNKNOWN")) {
                            String[] fullName = characters.split("\\s+");
                            person.getParentsId().add(fullName[0] + ' ' + fullName[1]);
                        }
                    }
                    case "brother" -> {
                        String[] fullName = characters.split("\\s+");
                        person.getSiblingsNames().add(fullName[0] + ' ' + fullName[1]);
                        person.getBrothersNames().add(fullName[0] + ' ' + fullName[1]);
                    }
                    case "sister" -> {
                        String[] fullName = characters.split("\\s+");
                        person.getSiblingsNames().add(fullName[0] + ' ' + fullName[1]);
                        person.getSistersNames().add(fullName[0] + ' ' + fullName[1]);
                    }
                    case "child" -> {
                        String[] fullName = characters.split("\\s+");
                        person.getChildrenNames().add(fullName[0] + ' ' + fullName[1]);
                    }
                    default -> {
                        System.out.println("NEW START ELEMENT WITH CHARACTERS " + lastElement + ": " + characters);
                        return null;
                    }
                }
            } else if (nextEvent.isEndElement()) {
                if (nextEvent.asEndElement().getName().toString().equals("person")) {
                    assert people != null;
                    people.addPerson(person);
                }
            }
        }

        return people;
    }
}


import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

@Getter
public class People implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int count;
    private final List<Person> people;
    private final Set<String> IDs;

    public People(int count) {
        this.count = count;
        IDs = new HashSet<>();
        this.people = new ArrayList<>();
    }

    public void addPerson(Person newPerson) {
        for (Person person : people) {
            if (newPerson.equals(person)) {
                person.merge(newPerson);
                return;
            }
        }
        people.add(newPerson);
    }

    public void dataCorrection() {

        for (Person p : people) {
            if ((p.getChildrenNumber() != null
                    && p.getChildrenId().size() < p.getChildrenNumber())
                    || p.getChildrenId().size() < p.getChildrenNames().size()) {


                for (String name : p.getChildrenNames()) {
                    Person ch = null;
                    for (Person c : people) {
                        if (!p.getChildrenId().contains(c.getId()) && c.getFullName().equals(name)) {
                            if (c.getParentsId().contains(p.getId()) || c.getParentsNames().contains(p.getFullName())) {
                                ch = c;
                            }
                        }
                    }
                    if (ch != null) {
                        p.getChildrenId().add(ch.getId());
                    }
                }

            }
        }
        for (Person p : people) {
            if (p.getSpouse() == null) {
                for (Person s : people) {
                    if (p.isSpouse(s)) {
                        p.setSpouse(new PersonInfo(s));
                        s.setSpouse(new PersonInfo(p));
                        break;
                    }
                }
            }
            if (p.getFather() == null) {
                for (Person f : people) {
                    if (p.isParent(f, "M")) {
                        p.setFather(new PersonInfo(f));
                        if (Objects.equals(p.getGender(), "M")) {
                            f.getSons().add(new PersonInfo(p));
                        } else {
                            f.getDaughters().add(new PersonInfo(p));
                        }
                        break;
                    }
                }
            }
            if (p.getMother() == null) {
                for (Person m : people) {
                    if (p.isParent(m, "F")) {
                        p.setMother(new PersonInfo(m));
                        if (Objects.equals(p.getGender(), "M")) {
                            m.getSons().add(new PersonInfo(p));
                        } else {
                            m.getDaughters().add(new PersonInfo(p));
                        }
                        break;
                    }
                }
            }
        }
        for (Person p : people) {
            for (Person s : people) {
                if (p.isSibling(s)) {
                    if (Objects.equals(p.getGender(), "M")) {
                        s.getBrothers().add(new PersonInfo(p));
                    } else {
                        s.getSisters().add(new PersonInfo(p));
                    }
                    if (Objects.equals(s.getGender(), "M")) {
                        p.getBrothers().add(new PersonInfo(s));
                    } else {
                        p.getSisters().add(new PersonInfo(s));
                    }
                }
            }
        }

    }

    public void checkData() {
        for (Person p : people) {
            if (p.getFullName() == null) {
                p.setFullName(p.getFirstName() + " " + p.getLastName());
            }
        }
        Scanner scanner = new Scanner(System.in);
        for (Person p : people) {
            if ((p.getChildrenNumber() != null
                    && p.getChildrenId().size() < p.getChildrenNumber())
                    || p.getChildrenId().size() < p.getChildrenNames().size()) {
                for (String name : p.getChildrenNames()) {
                    Person ch = null;
                    for (Person c : people) {
                        if (!p.getChildrenId().contains(c.getId()) && c.getFullName().equals(name)) {
                            if (c.getParentsId().contains(p.getId()) || c.getParentsNames().contains(p.getFullName())) {
                                ch = c;
                            }
                        }
                    }
                    if (ch != null) {
                        p.getChildrenId().add(ch.getId());
                    }
                }

            }
            if ((p.getSiblingsNumber() != null
                    && p.getSiblingsId().size() != p.getSiblingsNumber())
                    || p.getSiblingsId().size() < p.getSiblingsNames().size()) {
                if (p.getSiblingsNames().size() == 0) {
                    System.out.println("Problem 1 " + p);
                    System.out.println(p.getSiblingsId());
                    p.getSiblingsId().clear();
                    for (int i = 0; i < p.getSiblingsNumber(); i++) {
                        String id = scanner.next();
                        System.out.println(id);
                        p.getSiblingsId().add(id);
                        System.out.println(p);
                    }
                }
                else {
                    for (String name : p.getSiblingsNames()) {
                        List<Person> sib = new ArrayList<>();
                        for (Person s : people) {
                            if (!p.getSiblingsId().contains(s.getId()) && s.getFullName().equals(name)) {
                                if (s.getSiblingsId().contains(p.getId()) || s.getSiblingsNames().contains(p.getFullName())) {
                                    sib.add(s);
                                }
                            }
                        }
                        if (sib.size() > 1) {
                            System.out.println("--------------");
                            System.out.println(p);
                            System.out.println(sib);
                            String s = scanner.next();
                            p.getSiblingsId().add(sib.get(Integer.parseInt(s)).getId());
                            System.out.println("result");
                            System.out.println(p);
                            System.out.println("--------------");
                        } else {
                            p.getSiblingsId().add(sib.get(0).getId());
                        }
                    }
                }
            }
        }
    }
}



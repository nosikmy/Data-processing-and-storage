import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
public class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String id;
    private String fullName;
    private String gender;
    private PersonInfo spouse;
    private PersonInfo father;
    private PersonInfo mother;
    private Set<PersonInfo> sons = new HashSet<>();
    private Set<PersonInfo> daughters = new HashSet<>();
    private Set<PersonInfo> brothers = new HashSet<>();
    private Set<PersonInfo> sisters = new HashSet<>();


    private String firstName;
    private String lastName;

    private String spouseId;
    private String spouseName;
    private String husbandId;
    private String wifeId;

    private String fatherName;
    private String motherName;
    private Set<String> parentsId = new HashSet<>();
    private Set<String> parentsNames = new HashSet<>();

    private Integer childrenNumber;
    private Set<String> childrenId = new HashSet<>();
    private Set<String> childrenNames = new HashSet<>();
    private Set<String> daughtersId = new HashSet<>();
    private Set<String> sonsId = new HashSet<>();

    private Integer siblingsNumber;
    private Set<String> siblingsId = new HashSet<>();
    private Set<String> siblingsNames = new HashSet<>();
    private Set<String> sistersNames = new HashSet<>();
    private Set<String> brothersNames = new HashSet<>();

    public void updateParents(Set<String> newParentsID, Set<String> newParentsNames) {
        this.getParentsId().addAll(newParentsID);
        this.getParentsNames().addAll(newParentsNames);
    }

    public void updateChildren(Set<String> newChildrenID, Set<String> newChildrenNames) {
        this.getChildrenId().addAll(newChildrenID);
        this.getChildrenNames().addAll(newChildrenNames);
    }

    public void updateSiblings(Set<String> newSiblingsID, Set<String> newSiblingsNames) {
        this.getSiblingsId().addAll(newSiblingsID);
        this.getSiblingsNames().addAll(newSiblingsNames);
    }

    public void merge(Person newPerson) {
        if (this.getId() == null && newPerson.getId() != null) {
            this.setId(newPerson.getId());
        }
        if (this.getFirstName() == null && newPerson.getFirstName() != null) {
            this.setFirstName(newPerson.getFirstName());
        } else if (!Objects.equals(this.getFirstName(), newPerson.getFirstName()) && newPerson.getFirstName() != null) {
            System.out.println("problem in merge firstname " + this.getFirstName() + " " + newPerson.getFirstName());
            System.out.println(this);
            System.out.println(newPerson);
            System.out.println("-------------");
        }
        if (this.getLastName() == null && newPerson.getLastName() != null) {
            this.setLastName(newPerson.getLastName());
        } else if (!Objects.equals(this.getLastName(), newPerson.getLastName()) && newPerson.getLastName() != null) {
            System.out.println("problem in merge lastname " + this.getLastName() + " " + newPerson.getLastName());
            System.out.println(this);
            System.out.println(newPerson);
            System.out.println("-------------");
        }
        if (this.getGender() == null && newPerson.getGender() != null) {
            this.setGender(newPerson.getGender());
        } else if (!Objects.equals(this.getGender(), newPerson.getGender()) && newPerson.getGender() != null) {
            System.out.println("problem in merge gender " + this.getGender() + " " + newPerson.getGender());
            System.out.println(this);
            System.out.println(newPerson);
            System.out.println("-------------");
        }
        if (this.getSpouseId() == null && newPerson.getSpouseId() != null) {
            this.setSpouseId(newPerson.getSpouseId());
        } else if (!Objects.equals(this.getSpouseId(), newPerson.getSpouseId()) && newPerson.getSpouseId() != null) {
            System.out.println("problem in merge spouseId " + this.getSpouseId() + " " + newPerson.getSpouseId());
            System.out.println(this);
            System.out.println(newPerson);
            System.out.println("-------------");
        }
        if (this.getSpouseName() == null && newPerson.getSpouseName() != null) {
            this.setSpouseName(newPerson.getSpouseName());
        } else if (!Objects.equals(this.getSpouseName(), newPerson.getSpouseName()) && newPerson.getSpouseName() != null) {
            System.out.println("problem in merge SpouseName " + this.getSpouseName() + " " + newPerson.getSpouseName());
            System.out.println(this);
            System.out.println(newPerson);
            System.out.println("-------------");
        }
        if (this.getChildrenNumber() == null && newPerson.getChildrenNumber() != null) {
            this.setChildrenNumber(newPerson.getChildrenNumber());
        } else if (!Objects.equals(this.getChildrenNumber(), newPerson.getChildrenNumber()) && newPerson.getChildrenNumber() != null) {
            System.out.println("problem in merge ChildrenNumber SiblingsNumber" + this.getChildrenNumber() + " " + newPerson.getChildrenNumber());
            System.out.println(this);
            System.out.println(newPerson);
            System.out.println("-------------");
        }
        if (this.getSiblingsNumber() == null && newPerson.getSiblingsNumber() != null) {
            this.setSiblingsNumber(newPerson.getSiblingsNumber());
        } else if (!Objects.equals(this.getSiblingsNumber(), newPerson.getSiblingsNumber()) && newPerson.getSiblingsNumber() != null) {
            System.out.println("problem in merge SiblingsNumber " + this.getSiblingsNumber() + " " + newPerson.getSiblingsNumber());
            System.out.println(this);
            System.out.println(newPerson);
            System.out.println("-------------");
        }

        this.updateParents(newPerson.getParentsId(), newPerson.getParentsNames());
        this.updateChildren(newPerson.getChildrenId(), newPerson.getChildrenNames());
        this.updateSiblings(newPerson.getSiblingsId(), newPerson.getSiblingsNames());
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        if (this.id != null && person.id != null) {
            return id.equals(person.id);
        }
        if (this.id != null && Objects.equals(this.id, person.spouseId)) {
            return false;
        }
        if (person.id != null && Objects.equals(person.id, this.spouseId)) {
            return false;
        }
        if (this.gender != null && person.gender != null) {
            if (!this.gender.equals(person.gender)) {
                return false;
            }
            if ((this.gender.equals("M") && person.husbandId != null) ||
                    person.gender.equals("M") && this.husbandId != null) {
                return false;
            }
            if ((this.gender.equals("F") && person.wifeId != null) ||
                    person.gender.equals("F") && this.wifeId != null) {
                return false;
            }
        }
        if (this.wifeId != null && person.husbandId != null) {
            return false;
        }
        if (this.husbandId != null && person.wifeId != null) {
            return false;
        }
        if (this.spouseId != null && person.spouseId != null) {
            if (!this.spouseId.equals(person.spouseId)) {
                return false;
            }
        }
        if (this.siblingsNumber != null && person.siblingsNumber != null) {
            if (!this.siblingsNumber.equals(person.siblingsNumber)) {
                return false;
            }
        }


        if (this.firstName != null && person.firstName != null &&
                this.lastName != null && person.lastName != null) {
            return firstName.equals(person.firstName) && lastName.equals(person.lastName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", spouse=" + spouse +
                ", father=" + father +
                ", mother=" + mother +
                ", sons=" + sons +
                ", daughters=" + daughters +
                ", brothers=" + brothers +
                ", sisters=" + sisters +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", spouseId='" + spouseId + '\'' +
                ", spouseName='" + spouseName + '\'' +
                ", husbandId='" + husbandId + '\'' +
                ", wifeId='" + wifeId + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", parentsId=" + parentsId +
                ", parentsNames=" + parentsNames +
                ", childrenNumber=" + childrenNumber +
                ", childrenId=" + childrenId +
                ", childrenNames=" + childrenNames +
                ", daughtersId=" + daughtersId +
                ", sonsId=" + sonsId +
                ", siblingsNumber=" + siblingsNumber +
                ", siblingsId=" + siblingsId +
                ", siblingsNames=" + siblingsNames +
                ", sistersNames=" + sistersNames +
                ", brothersNames=" + brothersNames +
                '}';
    }


    public boolean isSpouse(Person s) {
        if (spouseId != null) {
            return spouseId.equals(s.id);
        }
        if (s.spouseId != null) {
            return id.equals(s.spouseId);
        }
        if (spouseName != null) {
            return spouseName.equals(s.fullName);
        }
        if (s.spouseName != null) {
            return s.spouseName.equals(fullName);
        }
        return false;
    }


    public boolean isParent(Person f, String gender) {
        if (!Objects.equals(f.gender, gender)) {
            return false;
        }
        return f.childrenId.contains(id);
    }


    public boolean isSibling(Person s) {
        if(s.id.equals(id)){
            return false;
        }
        return siblingsId.contains(s.id);

    }
}

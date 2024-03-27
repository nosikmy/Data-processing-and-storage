import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class PersonInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String fullName;
    private String gender;
    public PersonInfo(Person p) {
        this.id = p.getId();
        this.fullName = p.getFullName();
        this.gender = p.getGender();
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonInfo that = (PersonInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

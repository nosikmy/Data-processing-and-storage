import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
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
        Saver.savePeople(people);
        System.out.println(people.getPeople().size());
        for(Person p : people.getPeople()){
            if(p.getSiblingsNumber() != p.getSisters().size()+p.getBrothers().size() && p.getSiblingsNumber()!=null){
                System.out.println(p);
            }
            if(p.getChildrenNumber() != p.getDaughters().size()+p.getSons().size()&&p.getChildrenNumber()!=null){
                System.out.println(p);
            }
        }
        
    }
}


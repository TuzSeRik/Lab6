package Server.Background;

import Server.Background.Shorty;
import net.sf.jsefa.Deserializer;
import net.sf.jsefa.Serializer;
import net.sf.jsefa.csv.CsvIOFactory;
import java.io.StringReader;
import java.io.StringWriter;

class CSV {

    private static Serializer serializer = CsvIOFactory.createFactory(Shorty.class).createSerializer();
    private static Deserializer deserializer = CsvIOFactory.createFactory(Shorty.class).createDeserializer();

    static Shorty fromCSV(String full){
        StringReader reader = new StringReader(full);
        Shorty shorty = null;
        deserializer.open(reader);
        while (deserializer.hasNext()){
        shorty = deserializer.next();
        }
        deserializer.close(true);
        return shorty;
    }

    static String toCSV(Shorty shorty){
        StringWriter writer = new StringWriter();
        serializer.open(writer);
        serializer.write(shorty);
        return writer.toString();
    }
}
//+
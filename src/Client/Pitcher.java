package Client;

import java.io.*;
import java.net.Socket;

class Pitcher {
    private FileReader fromFile;
    private FileWriter toFile;
    private ObjectInputStream from;
    private ObjectOutputStream to;

    Pitcher(Socket socket) throws IOException{
        from = new ObjectInputStream(socket.getInputStream());
        to = new ObjectOutputStream(socket.getOutputStream());
        try
        {
            fromFile = new FileReader("collectionStorage.csv");
            toFile = new FileWriter("collectionStorage.csv");
        } catch (FileNotFoundException e){System.err.println("Фаил не был найден!");}
          catch (IOException e){e.printStackTrace();}
    }



    void pitch(String s) throws IOException {
        to.writeUTF(s);
        to.flush();
        System.out.println(from.readUTF());
    }

    void complexPitch(String s) throws IOException {
        to.writeUTF(s);
        to.flush();
        toFile.write(from.readUTF());
        toFile.flush();
    }

    void importPitch(String s) throws IOException {
        to.writeUTF(s+fromFile.read());
        to.flush();
        }
}
//+
package Server;

import java.io.*;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class Catcher extends Thread {
    private FileChannel channel;
    private ObjectInputStream from;
    private ObjectOutputStream to;
    private Collection collection;

    Catcher(SocketChannel socket, Collection collection) throws IOException{
            this.collection = collection;
        Socket socket1 = socket.socket();
            to = new ObjectOutputStream(socket1.getOutputStream());
            from = new ObjectInputStream(socket1.getInputStream());
            try
            {
                FileInputStream is = (FileInputStream) getClass().getResourceAsStream("/Server/collectionStorage.csv");
                channel = is.getChannel();
            }catch (Exception e){
                System.err.println("Файл хранилища отсутствует!");
            }
            setDaemon(true);
            setPriority(NORM_PRIORITY);
            start();
    }

    @Override
    public void run() {
        try {
            while (true){
            String input = from.readUTF();
            System.out.println(input);

            if (input.contains("show")) {
                to.writeUTF(collection.commands.show());
                to.flush();
            }

            if (input.contains("info")) {
                to.writeUTF(collection.commands.info());
                to.flush();
            }

            if (input.contains("load")) {
                collection.commands.load();
                to.writeUTF("Коллекция по-умолчанию загружена!");
                to.flush();
            }

            if (input.contains("initialize")) {
                collection.commands.initialise("");
                to.writeUTF("Коллекция инициализированна!");
                to.flush();
            }

            if (input.contains("start")) {

            }

            if (input.contains("remove_last")) {
                collection.commands.removeLast();
                to.writeUTF("Последний элемент удален!");
                to.flush();
            }

            if (input.contains("add ")) {
                String[] strings = input.split(" ");
                collection.commands.add(strings[1]);
                to.writeUTF("Элемент успешно записан!");
                to.flush();
            }

            if (input.contains("add_if_max ")) {
                String[] strings = input.split(" ");
                collection.commands.addIfMax(strings[1]);
                to.writeUTF("Элемент успешно записан(или не записан)!");
                to.flush();
            }

            if (input.contains("remove ")) {
                String[] strings = input.split(" ");
                collection.commands.remove(strings[1]);
                to.writeUTF("Элемент успешно удален!");
                to.flush();
            }

/*            if (input.contains("import")) {
                socket.read(buffer);
                String[] strings = StandardCharsets.UTF_8.decode(buffer).toString().split(" ");
                channel.write(StandardCharsets.UTF_8.encode(strings[1]));
                to.writeUTF("Коллекция успешно импортирована!"));
            }*/

            if (input.contains("overwrite")) {
                collection.commands.save();
                to.writeUTF("Коллекция успешно сохранена как стандартная!");
                to.flush();
            }

/*            if (input.contains("save")) {
                channel.read(buffer);
                socket.write(ByteBuffer.allocate(64*1024).put(buffer).put(StandardCharsets.UTF_8.encode("Коллекция успешно сохранена на клиенте!")));
            }*/

            if (input.contains("quitAll")) {
                to.writeUTF("Сервер завершил работу!");
                to.flush();
                throw new EOFException();
            }}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//++
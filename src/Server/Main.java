package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;

public class Main {
    public static void main(String[] args){
        ServerSocketChannel serverSocket = null;
        Collection collection = new Collection();

        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress("localhost", 2038));
            serverSocket.configureBlocking(true);
            System.out.println("Сервер запущен!");
        } catch (IOException e) {
            System.err.println("Не удалось запустить сервер :(");
        }

        while (true) {
            try {
                new Catcher(serverSocket.accept(), collection);
            } catch (IOException e){
                try{
                    serverSocket.close();
                }catch (IOException ignored){}
            }
        }
    }
}
//++
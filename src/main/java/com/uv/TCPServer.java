package com.uv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private boolean isStarted;
    private int port;

    public TCPServer(int port){
        this.port = port;
    }

    public void stop() throws IOException {
        //关闭连接
        isStarted = false;
        new Socket("localhost",port);

    }

    public void start() throws IOException {
        isStarted = true;
        //启动服务器
        new ServerThread(port).start();
    }

    public synchronized boolean isStarting(){
        return isStarted;
    }

    /**
     * 当前Server专属线程
     */

    class ServerThread extends Thread{
        private ServerSocket ss;

        public ServerThread(int port) throws IOException{
            this.ss = new ServerSocket(port);
        }

        @Override
        public void run() {

            try {
                //循环接收
                while (isStarting()) {
                    System.out.println("等待client接入");
//                    System.out.println(Thread.currentThread().getName());
                    System.out.println(InetAddress.getLocalHost().getHostAddress());
                    Socket socket = ss.accept();

                    if(isStarting()) { //最后一次连接，不载入
                        //输入输出流
                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter output = new PrintWriter(socket.getOutputStream());

                        String line = "";
                        StringBuilder info = new StringBuilder();
                        while ((line = input.readLine()) != null){
                            info.append(line);
                        }
                        System.out.println(info);

                        output.write("Have received the file\n");
                        output.flush();

                        input.close();
                        output.close();

                    }
                    socket.close();
                }
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

package com.uv;

import com.alibaba.fastjson.JSONObject;
import com.uv.client.NettyClient;
import com.uv.protocol.MsgUtil;
import com.uv.protocol.RpcRequest;
import io.netty.channel.Channel;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * <uv> [2018/10/13 19:51]
 */
public class Main {

    public static void main(String[] args) throws Exception {

//        new ClientThread("0.0.0.0", 10001, 10001).start();
//        new ClientThread("localhost", 60001, 11000).start();
        new ClientThread("localhost", 60001, 10001).start();
        new ClientThread("localhost", 60001, 10002).start();
        new ClientThread("localhost", 60001, 10003).start();

    }
    static class ClientThread extends Thread {
        private String ip;
        private int port;
        private int localPort;

        public ClientThread(String ip, int port, int localPort){
            this.ip = ip;
            this.port = port;
            this.localPort = localPort;
        }

        @Override
        public void run(){
            try {
                //1.建立客户端socket连接，指定服务器位置及端口
                Socket socket = new Socket(ip, port);
                //2.得到socket读写流
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                //输入流
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //3.利用流按照一定的操作，对socket进行读写操作
                JSONObject msg = MsgUtil.createRegister("", "localhost", localPort);
                pw.write(msg.toJSONString());
                pw.flush();
                socket.shutdownOutput();
                //接收服务器的相应
                //4.读取以及响应
                String line = "";
                StringBuilder info = new StringBuilder();
                while ( (line = br.readLine()) != null ){
                    info.append(line);
                }
                System.out.println(info);
                //4.关闭资源
                br.close();
                pw.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

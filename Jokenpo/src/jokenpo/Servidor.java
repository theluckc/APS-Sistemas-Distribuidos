package jokenpo;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author William, Guilherme, Julia, Caique, Gredi
 */

public class Servidor {

    static int vitorias, derrotas, empates;

    static void placar() {
        System.out.println("==== PLACAR =====");
        System.out.println("Vitórias: " + vitorias);
        System.out.println("Derrotas: " + derrotas);
        System.out.println("Empates: " + empates);
    }

    static void zerarPlacar() {
        vitorias = 0;
        derrotas = 0;
        empates = 0;
    }

    public static void main(String[] args) throws IOException {
        int portaServidor = 5000;
        String myMove = " ";
        String opponentMove = null;

        Scanner entrada = new Scanner(System.in);

        ServerSocket socketServidor = new ServerSocket(portaServidor);

        Socket clienteSocket = socketServidor.accept();

        System.out.println("Cliente " + clienteSocket.getInetAddress().getHostAddress() + " conectado na porta" + clienteSocket.getPort());
        DataInputStream in = new DataInputStream(clienteSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(clienteSocket.getOutputStream());

        boolean controleLoop1 = true;
        while (controleLoop1) {
            System.out.println("==== JOKENPO ====");
            System.out.println("1 - Jogador VS CPU");
            System.out.println("2 - Jogador VS Jogador");
            System.out.println("0 - Sair");
            System.out.println("Escolha uma das opções: ");
            String escolha = entrada.nextLine();
            switch (escolha) {
                case "1":
                    zerarPlacar();
                    Random gerador = new Random();
                    String[] escolhas = {"Pedra", "Papel", "Tesoura"};
                    String movimento = null;
                    boolean loopMachine = true;
                    while (loopMachine) {
                        String IAMove = escolhas[gerador.nextInt(3)];

                        boolean loopControle = true;
                        System.out.println("==== CONTRA MÁQUINA ====");
                        System.out.println("1 - JOGAR");
                        System.out.println("0 - SAIR");
                        escolha = entrada.nextLine();
                        switch (escolha) {
                            case "1":
                                System.out.print("Escolha entre Pedra, Papel e Tesoura: ");
                                while (loopControle) {
                                    movimento = entrada.nextLine();
                                    if (movimento.equalsIgnoreCase("pedra") || movimento.equalsIgnoreCase("tesoura") || movimento.equalsIgnoreCase("papel")) {
                                        movimento = movimento;
                                        loopControle = false;
                                    } else {
                                        System.out.println("Digite uma entrada válida");
                                    }
                                    if (movimento.equalsIgnoreCase("Pedra") == true && IAMove.equalsIgnoreCase("Tesoura") == true) {
                                        System.out.println("Venceu");
                                        vitorias++;
                                        System.out.println("Oponenente escolheu: " + IAMove);
                                    } else if (movimento.equalsIgnoreCase("Pedra") == true && IAMove.equalsIgnoreCase("Papel") == true) {
                                        System.out.println("Perdeu");
                                        derrotas++;
                                        System.out.println("Oponenente escolheu: " + IAMove);
                                    } else if (movimento.equalsIgnoreCase("Papel") == true && IAMove.equalsIgnoreCase("Pedra") == true) {
                                        System.out.println("Venceu");
                                        vitorias++;
                                        System.out.println("Oponenente escolheu: " + IAMove);
                                    } else if (movimento.equalsIgnoreCase("Papel") == true && IAMove.equalsIgnoreCase("Tesoura") == true) {
                                        System.out.println("Perdeu");
                                        derrotas++;
                                        System.out.println("Oponenente escolheu: " + IAMove);
                                    } else if (movimento.equalsIgnoreCase("Tesoura") == true && IAMove.equalsIgnoreCase("Papel") == true) {
                                        System.out.println("Venceu");
                                        vitorias++;
                                        System.out.println("Oponenente escolheu: " + IAMove);
                                    } else if (movimento.equalsIgnoreCase("Tesoura") == true && IAMove.equalsIgnoreCase("Pedra") == true) {
                                        System.out.println("Perdeu");
                                        derrotas++;
                                        System.out.println("Oponenente escolheu: " + IAMove);
                                    } else if (movimento.equalsIgnoreCase(IAMove)) {
                                        System.out.println("Empatou");
                                        empates++;
                                        System.out.println("Oponenente escolheu: " + IAMove);
                                    }
                                    placar();
                                }
                                break;

                            case "0":
                                loopMachine = false;
                        }
                    }
                    break;

                case "2":
                    out.writeByte(2);
                    out.writeUTF(" ");
                    boolean controleLoop = true;
                    while (controleLoop) {
                        System.out.println("Escolha entre Pedra, Papel e tesoura!");
                        myMove = entrada.nextLine();
                        if (myMove.equalsIgnoreCase("Pedra") || myMove.equalsIgnoreCase("Tesoura") || myMove.equalsIgnoreCase("Papel")) {
                            myMove = myMove;
                            controleLoop = false;
                        } else {
                            System.out.println("Digite uma entrada válida");
                        }
                    }
                    System.out.println("Sua escolha foi: " + myMove);
                    System.out.println("Aguardando cliente..");

                    do {
                        //do nothing until client sends a data
                    } while (in.readByte() == 1 && in.readUTF() == " ");

                    while (true) {
                        byte MessageType = in.readByte();

                        if (MessageType == 1) {
                            opponentMove = in.readUTF();
                        }
                        System.out.println("Adversário escolheu: " + opponentMove);
                        break;
                    }

                    out.writeByte(3);
                    out.writeUTF(myMove);

                    if (opponentMove.equalsIgnoreCase("Pedra") == true && myMove.equalsIgnoreCase("Tesoura") == true) {
                        out.writeByte(2);
                        out.writeUTF("Venceu");
                        System.out.println("Perdeu");
                        derrotas++;
                        out.flush();
                        out.writeUTF("Venceu");
                        out.flush();
                    } else if (opponentMove.equalsIgnoreCase("Pedra") == true && myMove.equalsIgnoreCase("Papel") == true) {
                        out.writeByte(2);
                        out.writeUTF("Perdeu");
                        System.out.println("Venceu");
                        vitorias++;
                        out.flush();
                        out.writeUTF("Perdeu");
                        out.flush();
                    } else if (opponentMove.equalsIgnoreCase("Papel") == true && myMove.equalsIgnoreCase("Pedra") == true) {
                        out.writeByte(2);
                        out.writeUTF("Venceu");
                        System.out.println("Perdeu");
                        derrotas++;
                        out.flush();
                        out.writeUTF("Venceu");
                        out.flush();
                    } else if (opponentMove.equalsIgnoreCase("Papel") == true && myMove.equalsIgnoreCase("Tesoura") == true) {
                        out.writeByte(2);
                        out.writeUTF("Perdeu");
                        System.out.println("Venceu");
                        vitorias++;
                        out.flush();
                        out.writeUTF("Perdeu");
                        out.flush();
                    } else if (opponentMove.equalsIgnoreCase("Tesoura") == true && myMove.equalsIgnoreCase("Papel") == true) {
                        out.writeByte(2);
                        out.writeUTF("Venceu");
                        System.out.println("Perdeu");
                        derrotas++;
                        out.flush();
                        out.writeUTF("Venceu");
                        out.flush();
                    } else if (opponentMove.equalsIgnoreCase("Tesoura") == true && myMove.equalsIgnoreCase("Pedra") == true) {
                        out.writeByte(2);
                        out.writeUTF("Perdeu");
                        System.out.println("Venceu");
                        vitorias++;
                        out.flush();
                        out.writeUTF("Perdeu");
                        out.flush();
                    } else if (opponentMove.equalsIgnoreCase(myMove)) {
                        out.writeByte(2);
                        out.writeUTF("Empate");
                        System.out.println("Empate");
                        empates++;
                        out.flush();
                        out.writeUTF("Empatou");
                        out.flush();
                    }
                    placar();
                    break;
                case "0":
                    controleLoop1 = false;
                    break;
                default:
                    System.out.println("Insira uma entrada válida");
                    break;
            }
        }
        clienteSocket.close();
    }
}

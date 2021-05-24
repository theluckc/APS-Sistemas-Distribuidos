package jokenpo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import static jokenpo.Servidor.derrotas;
import static jokenpo.Servidor.empates;
import static jokenpo.Servidor.placar;
import static jokenpo.Servidor.vitorias;
import static jokenpo.Servidor.zerarPlacar;

/**
 *
 * @author William, Guilherme, Julia, Caique, Gredi
 */
public class Cliente {

    public static void main(String[] args) throws IOException {
        String opponentMove = " ";
        String servidor = "localhost";
        int portaServidor = 5000;
        Scanner entrada = new Scanner(System.in);

        Socket socket = new Socket(servidor, portaServidor);

        System.out.println("Conectado ao " + servidor + "pela porta: " + portaServidor);

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

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

                        boolean controleLoop2 = true;
                        System.out.println("==== CONTRA MÁQUINA ====");
                        System.out.println("1 - JOGAR");
                        System.out.println("0 - SAIR");
                        escolha = entrada.nextLine();
                        switch (escolha) {
                            case "1":
                                System.out.print("Escolha entre Pedra, Papel e Tesoura: ");
                                while (controleLoop2) {
                                    movimento = entrada.nextLine();
                                    if (movimento.equalsIgnoreCase("pedra") || movimento.equalsIgnoreCase("tesoura") || movimento.equalsIgnoreCase("papel")) {
                                        movimento = movimento;
                                        controleLoop2 = false;
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
                                }
                                break;

                            case "0":
                                loopMachine = false;
                        }
                    }
                    break;
                case "2":
                    out.writeByte(1);
                    out.writeUTF(" ");
                    boolean controleLoop = true;
                    System.out.println("Escolha entre Pedra, Papel e Tesoura!");
                    while (controleLoop) {
                        opponentMove = entrada.nextLine();
                        if (opponentMove.equalsIgnoreCase("pedra") || opponentMove.equalsIgnoreCase("tesoura") || opponentMove.equalsIgnoreCase("papel")) {
                            opponentMove = opponentMove;
                            controleLoop = false;
                        } else {
                            System.out.println("Digite uma entrada válida");
                        }
                    }

                    System.out.println("Sua escolha foi: " + opponentMove);
                    System.out.println("Aguardando host..");
                    out.writeByte(1);
                    out.writeUTF(opponentMove);
                    out.flush();

                    do {

                    } while (in.readByte() == 2 && in.readUTF() == " ");

                    if (in.readByte() == 3) {
                        System.out.println("Adversário escolheu: " + in.readUTF());
                    }

                    if (in.readByte() == 2) {
                        System.out.println(in.readUTF());
                    }

                    String xis = in.readUTF();
                    switch (xis) {
                        case "Venceu":
                            vitorias++;
                            break;
                        case "Perdeu":
                            derrotas++;
                            break;
                        case "Empatou":
                            empates++;
                            break;
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
        socket.close();
    }
}

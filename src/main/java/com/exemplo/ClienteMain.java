package com.exemplo;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteMain {
    public static void main(String[] args) {
        try {
            // Pega o host do servidor das variáveis de ambiente ou usa localhost
            String serverHost = System.getenv("SERVER_HOST");
            if (serverHost == null) serverHost = "localhost";

            System.out.println("Conectando ao servidor em: " + serverHost);
            Registry registry = LocateRegistry.getRegistry(serverHost, 1099);
            IAutenticacao stub = (IAutenticacao) registry.lookup("AuthService");

            // Teste simples automático
            System.out.println("Tentando cadastrar 'aluno1'...");
            boolean cad = stub.cadastrar("aluno1", "minhasenha");
            System.out.println("Resultado cadastro: " + cad);

            System.out.println("Tentando logar 'aluno1'...");
            boolean log = stub.login("aluno1", "minhasenha");
            System.out.println("Resultado login: " + log);

        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
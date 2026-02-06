package com.exemplo;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorMain {
    public static void main(String[] args) {
        try {
            // Configuração CRÍTICA para Docker: Define o hostname externo
            String hostname = System.getenv("RMI_HOSTNAME");
            if (hostname != null) System.setProperty("java.rmi.server.hostname", hostname);

            IAutenticacao servico = new AutenticacaoImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("AuthService", servico);

            System.out.println(">>> Servidor RMI pronto na porta 1099 <<<");

            // Mantém o servidor rodando
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
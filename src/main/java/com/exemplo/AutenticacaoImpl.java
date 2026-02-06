package com.exemplo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class AutenticacaoImpl extends UnicastRemoteObject implements IAutenticacao {

    public AutenticacaoImpl() throws RemoteException {
        super();
    }


    private String gerarHash(String senhaOriginal) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(senhaOriginal.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Connection conectar() throws SQLException {
        String dbUrl = System.getenv("DB_URL");
        if (dbUrl == null) dbUrl = "jdbc:postgresql://localhost:5432/sistema_auth";
        return DriverManager.getConnection(dbUrl, "admin", "senha123");
    }

    @Override
    public boolean cadastrar(String usuario, String senha) throws RemoteException {
        // 1. Gera o hash antes de salvar
        String senhaHash = gerarHash(senha);
        if (senhaHash == null) return false;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO usuarios (login, senha) VALUES (?, ?)")) {

            stmt.setString(1, usuario);
            stmt.setString(2, senhaHash);

            stmt.executeUpdate();
            System.out.println("Servidor: Usuário " + usuario + " cadastrado com segurança.");
            return true;
        } catch (SQLException e) {
            System.err.println("Erro SQL no cadastro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean login(String usuario, String senha) throws RemoteException {
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT senha FROM usuarios WHERE login = ?")) {

            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String senhaDoBanco = rs.getString("senha");
                String senhaTentativaHash = gerarHash(senha);

                boolean match = senhaDoBanco.equals(senhaTentativaHash);

                System.out.println("Servidor: Login de " + usuario + (match ? " [SUCESSO]" : " [SENHA INCORRETA]"));
                return match;
            } else {
                System.out.println("Servidor: Login de " + usuario + " [USUARIO NAO ENCONTRADO]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
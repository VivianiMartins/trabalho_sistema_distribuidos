package com.exemplo;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAutenticacao extends Remote {
    boolean cadastrar(String usuario, String senha) throws RemoteException;
    boolean login(String usuario, String senha) throws RemoteException;
}
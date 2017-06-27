package pkginterface;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthInterface extends Remote {
    User login(String username, String password) throws RemoteException;
}

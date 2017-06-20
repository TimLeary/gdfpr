
package server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {
    private static final int PORT = 1099;
    
    public static void main(String[] args) {
        serve();
    }
    
    private static void serve() {
        try {
            LocateRegistry.createRegistry(PORT);
            Naming.bind("auth",  new Auth());
            Naming.bind("employee", new Employee());
            Naming.bind("departments", new Departments());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (AlreadyBoundException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        
    }
    
}

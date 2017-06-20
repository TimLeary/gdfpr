package server;
// https://www.cs.ucsb.edu/~cappello/lectures/rmi/helloworld.shtml


import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
import pkginterface.AuthInterface;
import pkginterface.User;

public class Auth extends UnicastRemoteObject implements AuthInterface{
    private static ArrayList<User> userList;

    public Auth() throws RemoteException {};
    
    @Override
    public User login(String username, String password) throws RemoteException {
        UserLoader userLoader = new UserLoader();
        userList = userLoader.getUserList();
        User logedUser = null;
        User loginUser = new User(username, password);
        
        for(User user : userList) {
            if(user.equals(loginUser)) {
                logedUser = user;
            }
        }
        
        return logedUser;
    }
    
}

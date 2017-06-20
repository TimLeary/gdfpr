package pkginterface;

import java.io.Serializable;
import java.security.*;
import java.math.*;

public class User implements Serializable {
    private String acl;
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }
    
    public String getAcl() {
        return acl;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User) {
            User user = (User) obj;
            if(user.username.equals(this.username) && md5(user.password).equals(this.password)) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
    
    public static String md5(String word) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(word.getBytes(),0,word.length());
            return new BigInteger(1,m.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}

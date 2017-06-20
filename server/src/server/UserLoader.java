package server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import pkginterface.User;

public class UserLoader {
    private ArrayList<User> userList = new ArrayList<>();

    public UserLoader() {
        loadXML();
    }
    
    public void loadXML() {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        
        try {
            builder = domFactory.newDocumentBuilder();
            doc = builder.parse("./src/server/Users.xml");
            doc.getDocumentElement().normalize();
            NodeList listOfUsers = doc.getElementsByTagName("user");
            int totalUsers = listOfUsers.getLength();

            for(int i=0; i<totalUsers ; i++) {
                Node userNode = listOfUsers.item(i);
                
                if(userNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) userNode;
                    String acl = userElement.getAttribute("acl");
                    
                    NodeList userNameList = userElement.getElementsByTagName("username");
                    Element userNameElement = (Element) userNameList.item(0);
                    
                    NodeList textUNList = userNameElement.getChildNodes();
                    String username = ((Node)textUNList.item(0)).getNodeValue().trim();
                    
                    NodeList passwordList = userElement.getElementsByTagName("password");
                    Element passwordElement = (Element) passwordList.item(0);
                    
                    NodeList textPWList = passwordElement.getChildNodes();
                    String password = ((Node)textPWList.item(0)).getNodeValue().trim();
                    
                    addUser(acl, username, password);
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void addUser(String acl, String username, String password) {
        User user = new User(username, password);
        user.setAcl(acl);
        userList.add(user);
    }

    public ArrayList<User> getUserList() {
        return userList;
    }
}

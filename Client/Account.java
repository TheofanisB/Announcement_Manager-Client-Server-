//Bakitas Theofanis icsd15133 Askisi 2i 
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fanis
 */
public class Account implements Serializable{ // klasi account
    //attributes
    private String Username;
    private String Password;
   

    public Account(String Username, String Password) {// constructor
        this.Username = Username;
        this.Password = Password;
       
      
    }
    //accessors
    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    

   
    
    
    
    
    
}

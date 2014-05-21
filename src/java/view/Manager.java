/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.companyFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.AccountDTO;
import model.Product;
import model.ProductDTO;

@Named("manager")
@ConversationScoped
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;
    @EJB
    private companyFacade compFacade;
    private AccountDTO loginuser;
    private AccountDTO account;
    private boolean status;
    private List<AccountDTO> accountList = new ArrayList<AccountDTO>();
    private List<ProductDTO> productList = new ArrayList<ProductDTO>();
    private List<ProductDTO> shoppingList = new ArrayList<ProductDTO>();
    private String username;
    private String pass;
    private boolean rss; //RegistrationSuccessfulStatus
    private boolean uaes; //UserAlreadyExsitsStatus
    private boolean adminStatus;
    private String amount;
    private String productname;
    private boolean loggedIn;
    private Exception transactionFailure;
    @Inject
    private Conversation conversation;

    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void stopConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    private void handleException(Exception e) {
        stopConversation();
        e.printStackTrace(System.err);
        transactionFailure = e;
    }

    /**
     * @return <code>true</code> if the latest transaction succeeded, otherwise
     * <code>false</code>.
     */
    public boolean getSuccess() {
        return transactionFailure == null;
    }

    /**
     * @return The latest thrown exception.
     */
    public Exception getException() {
        return transactionFailure;
    }

    /**
     * This return value is needed because of a JSF 2.2 bug. Note 3 on page 7-10
     * of the JSF 2.2 specification states that action handling methods may be
     * void. In JSF 2.2, however, a void action handling method plus an
     * if-element that evaluates to true in the faces-config navigation case
     * causes an exception.
     *
     * @return an empty string.
     */
    private String jsf22Bugfix() {
        return "";
    }

    public List<AccountDTO> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountDTO> accountList) {
        accountList = compFacade.getAccountList();
    }

    public List<ProductDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDTO> productList) {
        productList = compFacade.getProductList();
    }

    public List<ProductDTO> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ProductDTO> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public boolean getUserAlreadyExistsStatus() {
        return uaes;
    }

    public void setUserAlreadyExistsStatus(boolean uaes) {
        this.uaes = uaes;
    }

    public void setLoggedInStatus(boolean loggedin) {
        this.loggedIn = loggedin;
    }

    public void setRegistrationSuccessfulStatus(boolean rss) {
        this.rss = rss;
    }

    public boolean getRegistrationSuccessfulStatus() {
        return rss;
    }

    public boolean getLoggedInStatus() {
        return loggedIn;
    }

    public void setAdminStatus(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }

    public boolean getAdminStatus() {
        return adminStatus;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public void setBannedStatus(boolean status) {
        this.status = status;
    }

    public boolean getBannedStatus() {
        return status;
    }

    public String purchaseProducts() {
        try {
            shoppingList.removeAll(shoppingList);
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public String banHammer() {
        try {
            compFacade.updateStatus(getUsername(), true);
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public String update() {
        try {
            startConversation();
            compFacade.updateProduct(getProductname(), Integer.parseInt(getAmount()));
            Product product = new Product(getProductname(), Integer.parseInt(getAmount()));
            shoppingList.add(product);
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public void updateAccountList() {
        accountList = compFacade.getAccountList();
    }

    public void updateProductList() {
        productList = compFacade.getProductList();
    }

    public String unregisterProductAmount() {
        try {
            startConversation();
            compFacade.updateProduct(getProductname(), Integer.parseInt(getAmount()));
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public String logout() {
        try {
            setLoggedInStatus(false);
            setAdminStatus(false);
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public String unregisterProduct() {
        try {
            startConversation();
            compFacade.unregisterProduct(getProductname());
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public String registerProduct() {
        try {
            startConversation();
            Product product1 = new Product(getProductname(), Integer.parseInt(getAmount()));
            compFacade.registerProduct(product1);
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    /**
     * Here's where the magic is happening!
     *
     * ConvManger is makeing a call to the ejb class ConvFacade which is getting
     * the value from the javaDB through JPA (a presistant unit).
     */
    public String login() {
        try {
            startConversation();
            loginuser = compFacade.getAccount(getUsername());
            if (loginuser.getStatus() == true) {
                setBannedStatus(true);

            } else if ((loginuser.getUsername().equals("admin"))
                    && loginuser.getPassword().equals(getPass())) {
                setLoggedInStatus(true);
                setAdminStatus(true);
                account = loginuser;
                
            } else if (loginuser.getUsername().equals(getUsername())
                    && loginuser.getPassword().equals(getPass())) {
                setLoggedInStatus(true);
                account = loginuser;
            }

        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    /**
     * Here's where the magic is happening!
     *
     * ConvManger is makeing a call to the ejb class ConvFacade which is getting
     * the value from the javaDB through JPA (a presistant unit).
     */
    public String register() {

        try {
            startConversation();
            loginuser = compFacade.checkUserExists(getUsername());

            if (loginuser == null) {
                compFacade.register(getUsername(), getPass());
                setRegistrationSuccessfulStatus(true);
            } else {
                setUserAlreadyExistsStatus(true);
                setRegistrationSuccessfulStatus(false);
            }
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }
}

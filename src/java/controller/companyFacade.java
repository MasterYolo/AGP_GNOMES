package controller;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Account;
import model.AccountDTO;
import model.Product;
import model.ProductDTO;

/**
 * A controller. All calls to the model that are executed because of an action
 * taken by the cashier pass through here.
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class companyFacade {

    @PersistenceContext(unitName = "AGPGNOMESPU")
    private EntityManager em;

    private List<ProductDTO> productList = new ArrayList<ProductDTO>();
    private List<AccountDTO> accountList = new ArrayList<AccountDTO>();

    /**
     * Method to create store a new Currency into the database. Please note that
     * this method was created to test how the object storage function works.
     * (Not used anymore).
     *
     * @param currencyTo The name of the currency that is going to be converted.
     * @param currency The new exchangerate.
     * @return The newly created currency.
     */
    public AccountDTO register(String username, String password) {
        Account acc = new Account(username, password, false);
        em.persist(acc);
        return acc;
    }

    public AccountDTO checkUserExists(String username) {
        Account account = em.find(Account.class, username);
        return account;
    }

    public AccountDTO getAccount(String username) {

        Account account = em.find(Account.class, username);
        if (account == null) {
            throw new EntityNotFoundException("No account with that name: " + username);
        }
        return account;
    }

    public List<AccountDTO> getAccountList() {
        Query query = em.createQuery("SELECT a FROM Account a");
        accountList = query.getResultList();
        return accountList;
    }

    public ProductDTO updateProduct(String id, int amount) {
        Product product = em.find(Product.class, id);
        product.getAmount();
        Query query = em.createQuery("UPDATE Product p SET p.amount = :amount WHERE p.id = :id");
        query.setParameter("amount", product.getAmount() - amount);
        query.setParameter("id", id);
        query.executeUpdate();
        return product;
    }

    public List<ProductDTO> getProductList() {
        Query query = em.createQuery("SELECT p FROM Product p");
        productList = query.getResultList();
        return productList;
    }

    public ProductDTO registerProduct(Product product) {
        em.persist(product);
        return product;
    }

    public void unregisterProduct(String id) {
        Query query = em.createQuery("DELETE FROM Product p WHERE p.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public AccountDTO updateStatus(String name, boolean status) {
        Account acc = em.find(Account.class, name);
        Query query = em.createQuery("UPDATE Account a SET a.banned = :status WHERE a.username = :name");
        query.setParameter("name", name);
        query.setParameter("status", status);
        query.executeUpdate();
        return acc;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

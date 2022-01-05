package servidor.business;

public class Admin extends  User{

    public Admin() {
        new User();
    }

    public Admin(User user) {
        super(user);
    }

    public Admin(String nome, String pass) {
        super(nome, pass);
    }

    @Override
    public String toString() {
        return "Admin "+super.toString();
    }
}

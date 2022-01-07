package business;

import java.util.Objects;

public class User {
    private String nome;
    private String pass;

    public User() {
        this.nome = null;
        this.pass = null;
    }

    public User(User user){
        this.nome = user.getNome();
        this.pass = user.getPass();
    }

    public User(String nome, String pass) {
        this.nome = nome;
        this.pass = pass;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return nome.equals(user.nome) && pass.equals(user.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, pass);
    }

    @Override
    public String toString() {
        return "User{" +
                "nome='" + nome + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}

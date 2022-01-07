package business;

import java.util.*;

public class Reserva {
    private String codigo;
    private List<Voo> viagem;
    private User user;

    public Reserva() {
        this.codigo = null;
        this.viagem = null;
        this.user = new User();
        }

    public Reserva(String codigo, List<Voo> viagem, User user) {
        this.codigo = codigo;
        this.viagem = viagem;
        this.user = new User(user);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<Voo> getViagem() {
        return viagem;
    }

    public void setViagem(List<Voo> viagem) {
        this.viagem = viagem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return codigo.equals(reserva.codigo) && viagem.equals(reserva.viagem) && user.equals(reserva.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, viagem, user);
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "codigo='" + codigo + '\'' +
                ", viagem={" + viagem +
                "}}";
    }
}

package servidor.business;


import java.util.List;
import java.util.Map;

public class ServidorFacade {
    private Map<String,User> utilizadores;
    private Map<String,Reserva> reservas;
    private List<Voo> voos;

    public ServidorFacade() {
        this.utilizadores = null;
        this.reservas = null;
        this.voos = null;
    }

    public ServidorFacade(Map<String, User> utilizadores, Map<String, Reserva> reservas, List<Voo> voos) {
        this.utilizadores = utilizadores;
        this.reservas = reservas;
        this.voos = voos;
    }

    public Map<String, User> getUtilizadores() {
        return utilizadores;
    }

    public void setUtilizadores(Map<String, User> utilizadores) {
        this.utilizadores = utilizadores;
    }

    public Map<String, Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(Map<String, Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Voo> getVoos() {
        return voos;
    }

    public void setVoos(List<Voo> voos) {
        this.voos = voos;
    }

    /**adiciona um novo utilizdor**/
    public void addUtilizador(User user){

    }

    /**adiciona um novo voo**/
    public void addVoo(Voo voo){

    }

    /**adiciona uma nova reserva**/
    public void addReserva(Reserva reserva){

    }

    /**cancela uma reserva**/
    public void removeReserva(String codReserva){

    }
}

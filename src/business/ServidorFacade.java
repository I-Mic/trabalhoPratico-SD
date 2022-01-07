package business;


import business.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ServidorFacade {
    private Map<String,User> utilizadores;
    private Map<String,Reserva> reservas;
    private List<Voo> voos;
    private ReentrantLock lockserver= new ReentrantLock();

    public ServidorFacade() {
        this.utilizadores = new HashMap<>();
        this.reservas = new HashMap<>();
        this.voos = new ArrayList<>();
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
    public String registarUtilizador(User user) throws NomeJaExisteException {
        try {
            this.lockserver.lock();
            if (utilizadores.containsKey(user.getNome()))
                throw new NomeJaExisteException("Nome já existe");
            else{
                this.utilizadores.put(user.getNome(), user);
                return "Utilizador criado com sucesso";
            }
        }
        finally{
            this.lockserver.unlock();
        }
    }

    public User login(String nome, String password) throws NomeNaoExisteException,PalavraPasseIncorretaException{
        try{
            this.lockserver.lock();
            if(utilizadores.get(nome) == null)
                throw new NomeNaoExisteException("Conta nao encontrada");
            else
            if(!utilizadores.get(nome).getPass().equals(password))
                throw new PalavraPasseIncorretaException("Palavra-passe incorrecta!");
            else return utilizadores.get(nome);
        }
        finally{
            this.lockserver.unlock();
        }
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

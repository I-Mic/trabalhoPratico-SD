package Server;


import business.*;
import Server.Exceptions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ServidorFacade {
    private Map<String, User> utilizadores;
    private Map<String, Reserva> reservas;
    private List<Voo> voos;
    private final ReentrantLock lockserver= new ReentrantLock();

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

    /**adiciona um novo utilizdor**/ //Identificador 1
    public int registarUtilizador(String nome, String pass, int isadmin) {
            this.lockserver.lock();
            if(isadmin==0) {
                if (utilizadores.containsKey(nome)) {
                    //Nome ja existe
                    this.lockserver.unlock();
                    return 0;
                } else {
                    this.utilizadores.put(nome, new User(nome, pass));
                    this.lockserver.unlock();
                    return 1;
                }
            }
            else{
                if (utilizadores.containsKey(nome)) {
                    //Nome ja existe
                    this.lockserver.unlock();
                    return 0;
                } else {
                    this.utilizadores.put(nome, new Admin(nome, pass));
                    this.lockserver.unlock();
                    return 1;
                }
            }
    }

    /**Faz LogIn**/ //Identificador 2
    public int login(String nome, String password){
            if (utilizadores.get(nome) == null)
                //Conta nao Encontrada
                return -1;
            else if (!utilizadores.get(nome).getPass().equals(password))
                //Pass incorreta
                return -1;
            else{
                if (utilizadores.get(nome) instanceof Admin) return 1;
                return 0;
            }
    }



    /**adiciona uma nova reserva**/ //Identificador 3: Devolve codigo de reserva se possivel, n/a se impossivel
    public String addReserva(List<String> percurso, LocalDate dataInicio,LocalDate daataFim,String nome) {
        try {
            User user = this.utilizadores.get(nome);
            List<Voo> viagem = new ArrayList<>();
            String codigo;
            for (int i = 0; i < percurso.size() - 1; i++)
                viagem.add(getVooOrigemDestinoIntervalo(percurso.get(i), percurso.get(i + 1), dataInicio, daataFim));

            this.lockserver.lock();

            codigo = reservationCodeGenerator();
            Reserva reserva = new Reserva(codigo, viagem, user);
            this.reservas.put(codigo, reserva);

            this.lockserver.unlock();
            return codigo;
        }
        catch (VooNaoEncontradoException e){
            return "n/a";
        }
    }

    public String reservationCodeGenerator(){
        return String.valueOf((this.reservas.size() + 1));
    }

    public Voo getVooOrigemDestinoIntervalo(String origem,String destino,LocalDate dataInicio,LocalDate dataFim) throws VooNaoEncontradoException{
        try {
            this.lockserver.lock();
            for (int i= 0;i<this.voos.size();i++) {
                Voo voo = this.voos.get(i);
                if(voo.getOrigem().equals(origem) && voo.getDestino().equals(destino) && voo.getCapacidade()>0 && voo.getData().isAfter(dataInicio) && voo.getData().isBefore(dataFim)) {
                    this.voos.get(i).decrementCapacidade();
                    return voo;
                }
            }
            throw new VooNaoEncontradoException("Voo nao encontrado!");

        } finally {
            this.lockserver.unlock();
        }
    }

    /**cancela uma reserva**/ //Identificador 4
    public int removeReserva(String codReserva,String utilizador) {
        this.lockserver.lock();
            if (this.reservas.get(codReserva) == null) {
                this.lockserver.unlock();
                return 0;
            }
            else if (!this.reservas.get(codReserva).getUser().getNome().equals(utilizador)){
                this.lockserver.unlock();
                return 0;
            }
            else{
                this.reservas.remove(codReserva);
                this.lockserver.unlock();
                return 1;
            }
    }

    /**Devolve a lista de todos os voos existentes**/ //Identificador 5
    public String listaVoosExistentes(){
        return this.voos.toString();
    }

    /**adiciona um novo voo**/ //identifador 6
    public void addVoo(String origem,String destino,int capacidade,LocalDate data){
        Voo voo = new Voo(origem,destino,capacidade,data);
        this.lockserver.lock();
        voos.add(voo);
        this.lockserver.unlock();
    }
}

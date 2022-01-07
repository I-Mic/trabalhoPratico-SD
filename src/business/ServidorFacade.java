package business;


import business.exceptions.*;

import java.time.LocalDate;
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
        try {
            if (utilizadores.get(nome) == null)
                throw new NomeNaoExisteException("Conta nao encontrada");
            else if (!utilizadores.get(nome).getPass().equals(password))
                throw new PalavraPasseIncorretaException("Palavra-passe incorrecta!");
            else return utilizadores.get(nome);
        } finally {

        }

    }

    /**adiciona um novo voo**/
    public void addVoo(Voo voo){
        this.lockserver.lock();
        voos.add(voo);
        this.lockserver.unlock();
    }

    /**adiciona uma nova reserva**/
    public String addReserva(List<String> percurso, LocalDate dataInicio,LocalDate daataFim,User user)throws VooNaoEncontradoException{
        try {
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
            return null;
        }
    }

    public String reservationCodeGenerator(){
        return String.valueOf((this.reservas.size() + 1));
    }

    public Voo getVooOrigemDestinoIntervalo(String origem,String destino,LocalDate dataInicio,LocalDate daataFim) throws VooNaoEncontradoException{
        try {
            this.lockserver.lock();
            for (Voo voo:this.voos) {
                if(voo.getOrigem() == origem && voo.getDestino() == destino)
                    return voo;
            }
            throw new VooNaoEncontradoException("Voo nao encontrado!");

        } finally {
            this.lockserver.unlock();
        }
    }

    /**cancela uma reserva**/
    public void removeReserva(String codReserva,String utilizador) throws  ReservaNaoEncontradaException{
        try{
            this.lockserver.lock();
            if(this.reservas.get(codReserva)==null)
                throw  new ReservaNaoEncontradaException("Reserva Nao encontrada");
            else if(this.reservas.get(codReserva).getUser().getNome() != utilizador)
                throw  new ReservaNaoEncontradaException("Reserva Nao encontrada");
            else this.reservas.remove(codReserva);
        }finally {
            this.lockserver.unlock();
        }
    }

    public String listaVoosExistentes(){
        return this.voos.toString();
    }
}

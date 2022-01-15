package Server;


import business.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


public class ServidorFacade {
    private Map<String, User> utilizadores;
    private Map<String, Reserva> reservas;
    private Map<String, Voo> voos;
    private boolean isClosed;
    private final ReentrantLock lockserver= new ReentrantLock();
    private int codigoREserva;
    private int codVoo;

    public ServidorFacade() {
        this.utilizadores = new HashMap<>();
        this.reservas = new HashMap<>();
        this.voos = new HashMap<>();
        this.isClosed = false;
        this.codigoREserva = 0;
        this.codVoo = 0;
    }

    public ServidorFacade(Map<String, User> utilizadores, Map<String, Reserva> reservas, Map<String,Voo> voos,int codigoREserva, int codVoo) {
        this.utilizadores = utilizadores;
        this.reservas = reservas;
        this.voos = voos;
        this.isClosed=false;
        this.codVoo = codVoo;
        this.codigoREserva = codigoREserva;
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

    public Map<String,Voo> getVoos() {
        return voos;
    }

    public void setVoos(Map<String,Voo> voos) {
        this.voos = voos;
    }
    public void setcodVoo(int codVOO) {
        this.codVoo = codVOO;
    }
    public void setCodigoREserva(int codRes) {
        this.codigoREserva = codRes;
    }
    public int getCodigoREserva(){
        return codigoREserva;
    }
    public int getCodVoo(){
        return codVoo;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
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
        if(this.isClosed) return "closed";
            User user = this.utilizadores.get(nome);
            List<Voo> viagem = new ArrayList<>();
            String codigo;
            for (int i = 0; i < percurso.size() - 1; i++){
                Voo voo;
                voo = getVooOrigemDestinoIntervalo(percurso.get(i),percurso.get(i+1),dataInicio,daataFim);
                if(voo != null)
                viagem.add(voo);
            }

            if(viagem.size()==0) return "n/a";

            this.lockserver.lock();

            codigo = reservationCodeGenerator();
            Reserva reserva = new Reserva(codigo, viagem, user);
            System.out.println(reserva);
            this.reservas.put(codigo, reserva);

            this.lockserver.unlock();
            return codigo;


    }

    public String reservationCodeGenerator(){
        return String.valueOf((this.codigoREserva ++));
    }

    public Voo getVooOrigemDestinoIntervalo(String origem,String destino,LocalDate dataInicio,LocalDate dataFim) {
        this.lockserver.lock();
        for (Voo voo:this.voos.values() ) {
            if (voo.getOrigem().equals(origem) && voo.getDestino().equals(destino) && voo.getCapacidade() > 0 && (voo.getData().equals(dataInicio) || voo.getData().isAfter(dataInicio)) && (voo.getData().equals(dataFim) || voo.getData().isBefore(dataFim))) {
                this.voos.get(voo.getCodigo()).decrementCapacidade();
                this.lockserver.unlock();
                return voo;
            }
        }
        this.lockserver.unlock();
        return null;
    }

    /**cancela uma reserva**/ //Identificador 4
    public int removeReserva(String codReserva,String utilizador) {
        if(this.isClosed) return -1;
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
                Reserva reserva = this.reservas.get(codReserva);
                List<Voo> lista = reserva.getViagem();
                for(int i=0;i<lista.size();i++){
                    this.voos.get(lista.get(i).getCodigo()).incrementCapacidade();
                }
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
        this.lockserver.lock();
        String codigo = vooCodeGenerator();
        Voo voo = new Voo(codigo,origem,destino,capacidade,data);
        voos.put(codigo,voo);
        this.lockserver.unlock();
    }

    public String vooCodeGenerator(){
        return String.valueOf((this.codVoo ++ ));
    }

    /**Fecha servidor**/ //identificador 7
    public int closeServer(){
        if(this.isClosed) return 0;
        setClosed(true);
        return 1;
    }

    /**Reabre o servidor**/ //identificador 8
    public int openServer(){
        if(!this.isClosed) return 0;
        setClosed(false);
        return 1;
    }

}

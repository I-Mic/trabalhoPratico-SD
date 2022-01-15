package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiConsumer;

public class Voo {
    private String codigo;
    private String origem;
    private String destino;
    private int capacidade;
    private LocalDate data;

    public Voo() {
        this.codigo = null;
        this.origem = null;
        this.destino = null;
        this.capacidade = 0;
        this.data = null;
    }

    public Voo(String codigo,String origem, String destino, int capacidade, LocalDate data) {
        this.codigo = codigo;
        this.origem = origem;
        this.destino = destino;
        this.capacidade = capacidade;
        this.data = data;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void decrementCapacidade(){
        this.capacidade--;
    }

    public void incrementCapacidade(){
        this.capacidade++;
    }



    @Override
    public String toString() {
        return  origem + "->" + destino + "\n";
    }
}

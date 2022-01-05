package servidor.business;

import java.time.LocalDate;
import java.util.Objects;

public class Voo {
    private String origem;
    private String destino;
    private int capacidade;
    private LocalDate data;

    public Voo() {
        this.origem = null;
        this.destino = null;
        this.capacidade = 0;
        this.data = null;
    }

    public Voo(String origem, String destino, int capacidade, LocalDate data) {
        this.origem = origem;
        this.destino = destino;
        this.capacidade = capacidade;
        this.data = data;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voo voo = (Voo) o;
        return capacidade == voo.capacidade && origem.equals(voo.origem) && destino.equals(voo.destino) && data.equals(voo.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origem, destino, capacidade, data);
    }

    @Override
    public String toString() {
        return  origem + "->" + destino + '\'';
    }
}

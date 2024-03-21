package cursoPratique;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Evento implements Serializable {
    private LocalDateTime dataHora;
    private String endereco;
    private String tipo;
    private String descricao;
    private List<String> participantes;
    

    public Evento(LocalDateTime dataHora, String endereco, String tipo, String descricao) {
        this.dataHora = dataHora;
        this.endereco = endereco;
        this.tipo = tipo;
        this.descricao = descricao;
        this.participantes = new ArrayList<>();
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getEndereco() {
        return endereco;
    }
    
    public String getTipo() {
    	return tipo;
    	
    }
    
    public String getDescricao() {
    	return descricao;
    }
    
    public List<String> getParticipantes(){
    	return participantes;
    }
    
    public void adicionarParticipante(String participante) {
        participantes.add(participante);
    }

    public void removerParticipante(String participante) {
        participantes.remove(participante);
    }
    
    

    @Override
    public String toString() {
        return "Data e hora: " + dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm ")) +
                " Endereço: " + endereco +
                " / Tipo: " + tipo +
                " / Descrição: " + descricao;
    }
}
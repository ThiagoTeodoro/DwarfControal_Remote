package br.com.dwarfcontrol.model.DTO;

public class StatusDTO {

    private boolean status;

    //Construtor Vazio
    public StatusDTO() {
    }

    //Construtor recebendo Atributo
    public StatusDTO(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

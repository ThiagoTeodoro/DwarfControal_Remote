package br.com.dwarfcontrol.model.enums;

public enum NivelUsuario {

    Comum(0),
    Administrador(1);

    //Atributos
    public int value;

    //Construtor padrão da ENUM seguindo ordens dos atributos ENUMS
    NivelUsuario(int value) {
        this.value = value;
    }
}

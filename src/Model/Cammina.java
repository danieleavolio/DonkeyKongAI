package Model;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("cammina")
public class Cammina {

    @Param(0)
    public int colonna;
    @Param(1)
    public int riga;
    @Param(2)
    public int salta;

    public Cammina(int colonna, int riga, int salta) {
        this.colonna = colonna;
        this.riga = riga;
        this.salta = salta;
    }

    public Cammina() {
    }

    public int getColonna() {
        return colonna;
    }

    public void setColonna(int colonna) {
        this.colonna = colonna;
    }

    public int getRiga() {
        return riga;
    }

    public void setRiga(int riga) {
        this.riga = riga;
    }

    public int getSalta() {
        return salta;
    }

    public void setSalta(int salta) {
        this.salta = salta;
    }
}

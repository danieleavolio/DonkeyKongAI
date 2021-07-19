package Model;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("barile")
public class WrapperBarile {


    @Param(0)
    public int colonna;

    @Param(1)
    public int riga;

    public WrapperBarile() {
    }

    public WrapperBarile(int colonna, int riga) {
        this.colonna = colonna;
        this.riga = riga;
    }

    public int getRiga() {
        return riga;
    }

    public void setRiga(int riga) {
        this.riga = riga;
    }

    public int getColonna() {
        return colonna;
    }

    public void setColonna(int colonna) {
        this.colonna = colonna;
    }


}

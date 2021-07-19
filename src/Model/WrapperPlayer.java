package Model;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("start")
public class WrapperPlayer {


    @Param(0)
    public int colonna;

    @Param(1)
    public int riga;

    public WrapperPlayer() {
    }

    public WrapperPlayer(int colonna, int riga) {
        this.colonna = colonna;
        this.riga = riga;
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


}

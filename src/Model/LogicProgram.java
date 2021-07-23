package Model;

import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class LogicProgram {
    private DesktopHandler handler;
    private InputProgram factsFissi;
    private InputProgram factsVariabili;
    private AnswerSets as;
    private String encodingResource;


    /**
     * Costruttore della classe per la gestione del programma logico
     * @param enc è la directory del file delle regole e dei fatti
     */
    public LogicProgram(String enc) {
        this.encodingResource = enc;
        factsFissi = new ASPInputProgram();
        factsFissi.addFilesPath(encodingResource);
        factsVariabili = new ASPInputProgram();

        //per fargli prendere l'eseguibile
        handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));
        //per le opzioni di DLV
        OptionDescriptor option = new OptionDescriptor("--printonlyoptimum ");
        //gli diamo le opzioni all'handler di dlv
        handler.addOption(option);
        //Registrazione delle classi per gestire gli AS
        try {
            ASPMapper.getInstance().registerClass(WrapperPlayer.class);
            ASPMapper.getInstance().registerClass(WrapperBarile.class);
            ASPMapper.getInstance().registerClass(Cammina.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Facts fissi: i fatti che non cambiano
        //Facts variabili: i fatti che vengono calcolati durante l'esecuzione per capire dove spostarsi
        handler.addProgram(factsFissi);
        handler.addProgram(factsVariabili);
    }
    public void addFatti(Player player, ArrayList<Barile> bariles){
        //Cancella i fatti che avevamo prima, in fatti variabili
        factsVariabili.clearAll();
        //Prende la posizione del player
        try {
            WrapperPlayer luca = new WrapperPlayer(player.getPosX(), player.getPosY());
            factsVariabili.addObjectInput(luca);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Barile barile : bariles) {
            try {
                //Per ogni barile, prende la posizione del singolo barile
                WrapperBarile barileW = new WrapperBarile(barile.getPosX(), barile.getPosY());
                factsVariabili.addObjectInput(barileW);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        handler.addProgram(factsVariabili);
    }

    public Cammina getAnswerSet(){
        Output output = handler.startSync();
        //Conversione di output in A
        as = (AnswerSets) output;
        //tutti gli as

        for (AnswerSet a: as.getAnswersets()){
            try {
                for (Object o: a.getAtoms() ){
                    if (o instanceof Cammina){
                        return (Cammina)o;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

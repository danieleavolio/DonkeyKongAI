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

import javax.print.DocFlavor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class LogicProgram {
    private DesktopHandler handler;
    private InputProgram factsFissi;
    private InputProgram factsVariabili;
    private AnswerSets as;
    private String encodingResource;


    public LogicProgram(String enc) {
        //dove sono le regole
        this.encodingResource = enc;
        factsFissi = new ASPInputProgram();
        factsFissi.addFilesPath(enc);
        //fatti principali prima di uscire pazzi

        //per fargli prendere l'eseguibile
        handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));
        //per le opzioni di DLV
        OptionDescriptor option = new OptionDescriptor("-n 0, --printonlyoptimum ");
        //gli diami le regole all'handler di dlv
        handler.addOption(option);
        try {
            ASPMapper.getInstance().registerClass(WrapperPlayer.class);
            ASPMapper.getInstance().registerClass(WrapperBarile.class);
            ASPMapper.getInstance().registerClass(Cammina.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.addProgram(factsFissi);
    }
    public void addFatti(Player player, ArrayList<Barile> bariles){
        if (factsVariabili!=null)
            factsVariabili.clearAll();

        factsVariabili= new ASPInputProgram();
        try {
            WrapperPlayer luca = new WrapperPlayer(player.getPosX(), player.getPosY());
            factsVariabili.addObjectInput(luca);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Barile barile : bariles) {
            try {
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
        /*//tutti gli as ma solo quelli ottimi
        for (AnswerSet a: as.getOptimalAnswerSets()){
            System.out.println(a.toString());
        }*/
        return null;
    }

}

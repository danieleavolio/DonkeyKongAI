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

import java.util.ArrayList;

public class LogicProgram {
    private DesktopHandler handler;
    private InputProgram facts;
    private AnswerSets answer;
    private String encodingResource;


    public LogicProgram(String enc) {
        //dove sono le regole
        this.encodingResource = enc;

        //per fargli prendere l'eseguibile
        handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));
        //per le opzioni di DLV
        OptionDescriptor option = new OptionDescriptor("-n 0, --filter=cammina/3, --printonlyoptimum ");
        //gli diami le regole all'handler di dlv
        handler.addOption(option);
        try {
            ASPMapper.getInstance().registerClass(WrapperPlayer.class);
            ASPMapper.getInstance().registerClass(WrapperBarile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addFatti(GameObj[][] tavola) {
        //PER ESSERE EVOCATO IN MANIERA SINCRONA -- BLOCCANTE

        //per avere i fatti presi da subito
        ASPInputProgram fatti = new ASPInputProgram();
        //proviamo ad aggiungere la matrice del mondo come fatti
        /*for (int i = 0; i < tavola.length; i++) {
            for (int j = 0; j < tavola.length; j++) {
                try {
                    if (tavola[i][j] instanceof Player)
                        fatti.addObjectInput(new WrapperPlayer(i, j));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
        ASPInputProgram fattiDaFile = new ASPInputProgram();
        fattiDaFile.addFilesPath(encodingResource);
        handler.addProgram(fattiDaFile);

        //cioò ceh varia durante l'esecuzione e viene ripulito ogni esecuzinoe
        ASPInputProgram variabili = new ASPInputProgram();
    }
    public String getAs(){
        Output output = handler.startSync();
        //Conversione di output in A
        AnswerSets as = (AnswerSets) output;
        //tutti gli as
        for (AnswerSet a: as.getOptimalAnswerSets()){
            return a.getAnswerSet().toString();
        }
        /*//tutti gli as ma solo quelli ottimi
        for (AnswerSet a: as.getOptimalAnswerSets()){
            System.out.println(a.toString());
        }*/
        return null;
    }

}

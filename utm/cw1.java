package utm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Myclass {
    public UniversalTuringMachine myutm;
    public String initialState;
    public String acceptState;
    public String rejectState;
    public String[] rules;
    //Constructor for Myclass
    public Myclass()
    {
        myutm=new UniversalTuringMachine();
        this.initialState="";
        this.acceptState="";
        this.rejectState="";
    }
    public void displayWindow() {
        //Code for displaying a window using java.awt and utm.UniversalTuringMachine
        myutm.display();

    }
    public TuringMachine createTMFromFile(String tmDescriptionFilePath) throws IOException {

        //Use Java properties and java.io to parse a TM description File
        String rule="";
        String str1;
        BufferedReader in = new BufferedReader(new FileReader(tmDescriptionFilePath));
        String str;
        while ((str = in.readLine()) != null) {
            if(str.indexOf("initialState=")!=-1) {
                str1=str.substring(0, str.indexOf("initialState="));
                initialState=str.substring(str1.length()+1+"initialState".length(), str.length());}
            if(str.indexOf("acceptState=")!=-1) {
                str1=str.substring(0, str.indexOf("acceptState="));
                acceptState=str.substring(str1.length()+1+"acceptState".length(), str.length());}
            if(str.indexOf("rejectState=")!=-1) {
                str1=str.substring(0, str.indexOf("rejectState="));
                rejectState=str.substring(str1.length()+1+"rejectState".length(), str.length());}
            if(str.indexOf("rules=")!=-1) {
                str1=str.substring(0, str.indexOf("rules="));
                rule=str.substring(str1.length()+1+"rules".length(), str.length());}
        }

        this.rules=rule.split("<>");
        TuringMachine tm=new TuringMachine(rules.length,initialState,acceptState,rejectState);
        return tm;
    }

    public void loadTM(TuringMachine tm,String input) {
        String[] detail;
        for(int i=0;i<rules.length;i++) {
            detail=rules[i].split(",");
            if(detail[4].equals("RIGHT")) {
                tm.addRule(detail[0], detail[1].charAt(0), detail[2], detail[3].charAt(0), MoveClassical.RIGHT);
            }
            else {
                tm.addRule(detail[0], detail[1].charAt(0), detail[2], detail[3].charAt(0), MoveClassical.LEFT);
            }
        }
        myutm.loadTuringMachine(tm);
        myutm.loadInput(input);

    }
    public void executeTM(UniversalTuringMachine utm) {
        int index=0;
        String[][] R;
        R=utm.getTuringMachine().getRules();
        while(true)
        {
            for(int i=0;i<R.length;i++) {
                index=utm.getTuringMachine().getHead().getCurrentCell();
                if(utm.getTuringMachine().getHead().getCurrentState().equals(R[i][0])&&String.valueOf(utm.getTuringMachine().getTape().get(index)).equals(R[i][1]))
                {
                    utm.writeOnCurrentCell(R[i][3].charAt(0));
                    if(R[i][4].equals("LEFT"))
                        utm.moveHead(MoveClassical.LEFT,true);
                    if(R[i][4].equals("RIGHT")) {
                        utm.moveHead(MoveClassical.RIGHT,true);
                    }
                    utm.updateHeadState(R[i][2]);
                    if(utm.getTuringMachine().getHead().getCurrentState().equals(utm.getTuringMachine().getAcceptState())) {
                        utm.displayHaltState(HaltState.ACCEPTED);
                        break;
                    }
                    if(utm.getTuringMachine().getHead().getCurrentState().equals(utm.getTuringMachine().getRejectState())) {
                        utm.displayHaltState(HaltState.REJECTED);
                        break;
                    }
                }
            }

        }
    }
    public static void main(String[] args) throws IOException {
        Myclass OneUtm=new Myclass();
        if(args.length!=3) {
            System.out.println("Error!,please input 3 args");
            System.exit(0);
        }
        if(args[2].equals("--animation"))
            OneUtm.displayWindow();
        TuringMachine tm=OneUtm.createTMFromFile(args[0]);
        OneUtm.loadTM(tm,args[1]);
        OneUtm.executeTM(OneUtm.myutm);
    }



}
